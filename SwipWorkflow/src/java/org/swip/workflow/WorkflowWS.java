package org.swip.workflow;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.json.JSONWithPadding;
import com.sun.jersey.api.representation.Form;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.text.ParseException;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.swip.exchange.DependencyTree;
import org.swip.exchange.NlToPivotResult;

@Path("/rest/")
public class WorkflowWS {

    private static final Logger logger = Logger.getLogger(WorkflowWS.class);
//    private final static String serverIP = "http://localhost:8080/";
//    private final static String serverIP = "http://192.168.250.91/";
    private final static String serverIP = "http://localhost/";

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    @Path("nlToPivot")
    public NlToPivotResult nlToPivot(
            @QueryParam("nlQuery") @DefaultValue("") String nlQuery,
            @QueryParam("kb") @DefaultValue("") String kb,
            @QueryParam("lang") @DefaultValue("fr") String lang,
            @QueryParam("pos") @DefaultValue("treeTagger") String posTagger,
            @QueryParam("dep") @DefaultValue("malt") String depParser,
            @QueryParam("gazwebservice") @DefaultValue("") String gazwebservice,
            @QueryParam("dpwebservice") @DefaultValue("") String dpwebservice,
            @QueryParam("ruleswebservice") @DefaultValue("") String ruleswebservice) throws ParseException, UniformInterfaceException, JAXBException, IOException {

        logger.info("User nl query: " + nlQuery);
        logger.info("Target knowledge base: " + kb);
        logger.info("Query language: " + lang);
        logger.info("POS tagger: " + posTagger);
        logger.info("Dependency parser: " + depParser);
        logger.info("gazwebservice: " + gazwebservice);
        logger.info("dpwebservice: " + dpwebservice);
        logger.info("ruleswebservice: " + ruleswebservice);

        NlToPivotResult result = new NlToPivotResult();
//        result.setGazetteedQuery("gazetteedQuery");
//        result.setPivotQuery("?person: The_Heroes_of_Telemark");

        String gazetteedQuery = null;
        if (kb.equals("dbpedia")) {
            gazetteedQuery = nlQuery;
        } else {
            gazetteedQuery = new NlToPivotGazetteerWS_JerseyClient(gazwebservice).gatherNamedEntities(nlQuery, "false");
        }
        result.setGazetteedQuery(gazetteedQuery);
        logger.info("Gazetteed query: " + gazetteedQuery);

        DependencyTree dependencyTree = new NlToPivotParserWS_JerseyClient(dpwebservice).nlToDependenciesJson(lang, gazetteedQuery, depParser, posTagger);
        result.setDependencyTree(dependencyTree);
        logger.info("Dependency tree: " + dependencyTree);

        // marshal dependencyTree into JSON
        ObjectMapper mapper = new ObjectMapper();
        Writer strWriter = new StringWriter();
        mapper.writeValue(strWriter, dependencyTree);
        String dependencyTreeJSON = strWriter.toString();

        Form f = new Form();
        f.add("dependencyTree", dependencyTreeJSON);
        f.add("lang", "en");
        f.add("pos", "treeTagger");
        f.add("dep", "malt");

        WebResource resource = Client.create().resource(ruleswebservice);
        String pivotQuery = resource.type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).post(String.class, f);
        result.setPivotQuery(pivotQuery);
        logger.info("pivot query: " + pivotQuery);

        return result;
    }

    @GET
    @Produces({"application/x-javascript", MediaType.APPLICATION_JSON})
    @Path("nlToPivotJSONP")
    public JSONWithPadding nlToPivotJSONP(
            @QueryParam("nlQuery") @DefaultValue("") String nlQuery,
            @QueryParam("kb") @DefaultValue("") String kb,
            @QueryParam("lang") @DefaultValue("fr") String lang,
            @QueryParam("pos") @DefaultValue("treeTagger") String posTagger,
            @QueryParam("dep") @DefaultValue("malt") String depParser,
            @QueryParam("gazwebservice") @DefaultValue("") String gazwebservice,
            @QueryParam("dpwebservice") @DefaultValue("") String dpwebservice,
            @QueryParam("ruleswebservice") @DefaultValue("") String ruleswebservice,
            @QueryParam("callback") @DefaultValue("fn") String callback) throws ParseException, UniformInterfaceException, JAXBException, IOException {

        return new JSONWithPadding(nlToPivot(nlQuery, kb, lang, posTagger, depParser, gazwebservice, dpwebservice, ruleswebservice), callback);
    }

    static class NlToPivotGazetteerWS_JerseyClient {

        private WebResource webResource;
        private Client client;

        public NlToPivotGazetteerWS_JerseyClient(String wsUrl) {
            com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig();
            client = Client.create(config);
            webResource = client.resource(wsUrl);
        }

        public String gatherNamedEntities(String text, String tagWithClass) throws UniformInterfaceException {
            WebResource resource = webResource;
            if (tagWithClass != null) {
                resource = resource.queryParam("tagWithClass", tagWithClass);
            }
            if (text != null) {
                resource = resource.queryParam("text", text);
            }
            return resource.accept(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
        }

        public void close() {
            client.destroy();
        }
    }

    static class NlToPivotParserWS_JerseyClient {

        private WebResource webResource;
        private Client client;

        public NlToPivotParserWS_JerseyClient(String wsUrl) {
            com.sun.jersey.api.client.config.ClientConfig config = new com.sun.jersey.api.client.config.DefaultClientConfig();
            client = Client.create(config);
            webResource = client.resource(wsUrl);
        }

        public DependencyTree nlToDependenciesJson(String lang, String nlQuery, String dep, String pos) throws UniformInterfaceException {
            WebResource resource = webResource;
            if (lang != null) {
                resource = resource.queryParam("lang", lang);
            }
            if (nlQuery != null) {
                resource = resource.queryParam("nlQuery", nlQuery);
            }
            if (dep != null) {
                resource = resource.queryParam("dep", dep);
            }
            if (pos != null) {
                resource = resource.queryParam("pos", pos);
            }
            return resource.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(DependencyTree.class);
        }

        public void close() {
            client.destroy();
        }
    }
}
