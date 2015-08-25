// Imports
// $.getScript('js/desc.js');

function DescList() 
{
	this.descA = [];

	this.add = function(descriptiveSentence, mappingId)
	{
		this.descA.push(new Desc(descriptiveSentence, mappingId));
	}

	this.checkCover = function()
	{
		var ret = [];

		for(var i = 0; i < this.descA.length; i++)
		{
			for(var j = 0; j < this.descA.length; j++)
			{
				if(i != j && $.inArray(j, ret) < 0 && $.inArray(i, ret) < 0)
				{
					//console.log(i + " couvre " + j + " ?");
					var iCoversJ = true;

					iGen = this.descA[i].getGenIds();
					jGen = this.descA[j].getGenIds();

					if(iGen == null && jGen == null)
					{
						if(this.descA[i].getGeneralizedSentence() != this.descA[j].getGeneralizedSentence())
						{
							iCoversJ = false;
							//console.log("KO (Null)");
						}
					}
					else if(iGen == null || jGen == null)
					{
						iCoversJ = false;
						//console.log("KO (Null 2)");
					}
					else
					{
						if(iGen.length < jGen.length)
						{
							iCoversJ = false;
							//console.log("KO (Length)");
						}
						else
						{
							for(var k = 0; k < jGen.length; k++)
							{
								var ind = $.inArray(jGen[k], iGen)
								if(ind < 0)
								{
									iCoversJ = false;
									//console.log("KO (Gen cover)");
								}
								else
								{
									var genId = jGen[k].substr(4, jGen[k].length - 5);
									iGenA = this.descA[i].getGen(genId);
									//console.log("jSen " + this.descA[j].descriptiveSentence.string);
									//console.log("genId " + genId);
									jGenA = this.descA[j].getGen(genId);

									for(var l = 0; l < jGenA.length; l++)
									{
										if($.inArray(jGenA[l], iGenA) < 0)
										{
											iCoversJ = false;
											//console.log("KO (List cover)");
										}
									}
								}

								k++;
							}
						}
					}

					if(iCoversJ)
					{
						this.descA[i].addCovered(this.descA[j].getCovered());

						if($.inArray(j, ret) < 0)
							ret.push(j);
					}

					//console.log(iCoversJ);
					//console.log("========================");
				}
			}
		}

		return ret;
	}

	this.getGeneralizedSentence = function(id)
	{
		return this.descA[id].getGeneralizedSentence(this.descA);
	}

	this.getMaxDescent = function(id)
	{
		return this.descA[id].getMaxDescent();
	}

	this.getCovered = function(id)
	{
		return this.descA[id].getCovered();
	}
}