/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.swip.nlToPivotGatePipeline;

import gate.*;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.apache.log4j.Logger;

/**
 *
 * @author camille
 */
@WebService(serviceName = "NlToPivotGatePipeline")
public class NlToPivotGatePipelineWS {

    /** The Corpus Pipeline application */
    static private CorpusController corpusControllerGazetteer = null;
    private static final Logger logger = Logger.getLogger(NlToPivotGatePipelineWS.class);

    private CorpusController getCorpusControllerGazetteer() {
        if (corpusControllerGazetteer == null) {
            try {
                initGate();
                logger.info("Loading Gate process pipeline.......");
//                corpusControllerGazetteer = (CorpusController) PersistenceManager.loadObjectFromFile(new File(this.getClass().getClassLoader().getResource("../NlToPivotGazetteerMusicbrainz.gapp").getPath()));
                corpusControllerGazetteer = (CorpusController) PersistenceManager.loadObjectFromFile(new File("/home/operateur/gate/NlToPivotGazetteerMusicbrainz.gapp"));
                logger.info("Process pipeline loaded");
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
            logger.info("corpusControllerGazetteer: " + corpusControllerGazetteer);
        }
        return corpusControllerGazetteer;
    }

    /**
     * initialize the GATE library
     * @throws GateException 
     */
    private void initGate() throws GateException {
        logger.info("Initialising GATE...");
        File userGateFile = new File(this.getClass().getClassLoader().getResource("../user-gate.xml").getPath());
//        File userGateFile = new File("../gate/user-gate.xml");
        File gateHomeFile = userGateFile.getParentFile();
        logger.info("userGateFile: " + userGateFile.getAbsolutePath());
        logger.info("gateHomeFile: " + gateHomeFile.getAbsolutePath());
        Gate.setGateHome(gateHomeFile);
        Gate.setUserConfigFile(userGateFile);
        Gate.init();
        logger.info("default user config file: " + Gate.getDefaultUserConfigFileName());
        logger.info("default user session file: " + Gate.getDefaultUserSessionFileName());
        logger.info("user config file: " + Gate.getUserConfigFile());
        logger.info("user session file: " + Gate.getUserSessionFile());
        logger.info("site config file: " + Gate.getSiteConfigFile());
        logger.info("plugins home: " + Gate.getPluginsHome());
        logger.info("original user config: " + Gate.getOriginalUserConfig());
        logger.info("known plugins: " + Gate.getKnownPlugins());
        logger.info("built in creole dir: " + Gate.getBuiltinCreoleDir());
    }
    
    /**
     * Web service operation
     */
    @WebMethod(operationName = "getQueryWithGatheredNamedEntities")
    public String getQueryWithGatheredNamedEntities(@WebParam(name = "adaptedNlQuery") String adaptedNlQuery,
            @WebParam(name = "getClass") boolean getClass) {

        // FIXME: list of phrases appearing as labels of the KB we don't want to consider as named entities
        //        should be added in /home/camille/gate/dictionary-local-musicbrainz/ignoreList.txt and removed from here
        //        before next indexation in LKBGazetteer
        // pb: Which singles did the_Dead Kennedys release?
        List<String> dirtyStopList = new ArrayList<String>(Arrays.asList(new String[]{"the birthday", "broke up", "all live", "How long",
                    "the song", "many members", "all members", "the_same_day", "For how long", "the_names", "the_children",
                    "What is", "the third", "the most", "the lyrics", "rock_album", "the daughter", "Who is", "the single",
                    "more than once", "Show me", "the same day", "the names", "the children",
                    "in_July", "all_artists", "the_name", "in_2000", "work_together", "kind_of",
                    "the_death", "many_pieces", "When_was_That", "all_artists", "in_a_band", //                    "solo_album" ???
                }));

        logger.info("NL query for Gate pipeline: " + adaptedNlQuery);
        try {
            CorpusController cont = this.getCorpusControllerGazetteer();
            Document doc = (Document) Factory.newDocument(adaptedNlQuery);
            Corpus corpus = (Corpus) Factory.createResource("gate.corpora.CorpusImpl");
            ((List<Document>) corpus).add(doc);
            cont.setCorpus(corpus);
            cont.execute();
            AnnotationSet annotSet = doc.getAnnotations();
            AnnotationSet lookupToKeepAnnotSet = annotSet.get("LookupToKeep");
            String resultNLQuery = adaptedNlQuery;
            for (Annotation lookupToKeepAnnot : lookupToKeepAnnotSet) {
                int start = lookupToKeepAnnot.getStartNode().getOffset().intValue();
                int end = lookupToKeepAnnot.getEndNode().getOffset().intValue();
                String substring = adaptedNlQuery.substring(start, end);
                if (!dirtyStopList.contains(substring)) {
                    String replacingSubstring = substring.replaceAll(" ", "_");
                    if (getClass) {
                        replacingSubstring = replacingSubstring + "(" + lookupToKeepAnnot.getFeatures().get("class") + ")";
                    }
                    resultNLQuery = resultNLQuery.replaceAll(substring, replacingSubstring);
                }
            }
            logger.info("adapted query for Gate pipeline with named entities: " + resultNLQuery);
            return resultNLQuery;
        } catch (ExecutionException ex) {
            logger.error(ex);
        } catch (ResourceInstantiationException ex) {
            logger.error(ex);
        }
        logger.info("fail to parse adapted nl query");
        logger.info("\n\n\n");
        return "fail to parse adapted nl query";
    }
}