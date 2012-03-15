package com.LabOrganizer;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.LabOrganizer.Experiment.Subexperiment;

public class OrganizerXMLHandler extends DefaultHandler {
	
	
	ArrayList<Hypothesis> hypotheses = new ArrayList<Hypothesis>();
	ArrayList<Experiment> experiments = new ArrayList<Experiment>();
	boolean inSubexpt = false;
	
	String whatToParse = "hypotheses"; //"hypotheses", "experiments"
	
	StringBuilder stringBuilder = new StringBuilder();
	
	public void startElement (String uri, String name, String qName, Attributes atts) 
			throws SAXException {
		if(name.equals("")) name = qName;
		if(whatToParse.equals("hypotheses"))
		{
			startHypothesisElement(name, atts);
		}
		else if(whatToParse.equals("experiments"))
		{
			startExperimentElement(name, atts);
		}
	}
			
	public void endElement (String uri, String name, String qName)
			throws SAXException {
		if(name.equals("")) name = qName;
		if(whatToParse.equals("hypotheses"))
		{
			endHypothesisElement(name);
		}
		else if(whatToParse.equals("experiments"))
		{
			endExperimentElement(name);
		}
	}
		
	public void characters (char ch[], int start, int length)
			throws SAXException
	{
		stringBuilder.append(ch);
	}
	
	public void startHypothesisElement (String name, Attributes atts) throws SAXException
	{
		if(name.equals("hypothesis"))
		{
			hypotheses.add(new Hypothesis());
			hypotheses.get(hypotheses.size()-1).ID = SAXUtils.getIntAttributeOrThrow(atts, "id");
			hypotheses.get(hypotheses.size()-1).name = SAXUtils.getAttribute(atts, "name", "" + hypotheses.get(hypotheses.size()-1).ID);
			
		}
	}
	
	public void endHypothesisElement (String name) throws SAXException
	{
		if(name.equals("hypothesis"))
		{
			hypotheses.get(hypotheses.size()-1).content = stringBuilder.toString();
			stringBuilder = new StringBuilder();
		}
	}
	
	public void startExperimentElement (String name, Attributes atts) throws SAXException
	{
		if(name.equals("experiment"))
		{
			Experiment exp = new Experiment();
			experiments.add(exp);
			exp.ID = SAXUtils.getIntAttributeOrThrow(atts, "id");
			exp.name = SAXUtils.getAttribute(atts, "name", "EXPT_ID_" + exp.ID);
			exp.title = SAXUtils.getAttribute(atts, "title", "");
			exp.date = SAXUtils.getAttribute(atts, "date", "");
			exp.infofile = SAXUtils.getAttribute(atts, "infofile", "");
		}
		else if(name.equals("hypothesis"))
		{
			boolean foundHypothesis = false;
			for(int i=0;i<hypotheses.size();i++)
			{
				if(hypotheses.get(i).ID == SAXUtils.getIntAttributeOrThrow(atts, "id")) {
					experiments.get(experiments.size()-1).hypotheses.add(hypotheses.get(i));
					foundHypothesis = true;
				}
			}
			if(!foundHypothesis) {
				Hypothesis h = new Hypothesis();
				h.ID = SAXUtils.getIntAttributeOrThrow(atts, "id");
				h.name = "Unknown Hypothesis ID: " + h.ID;
				experiments.get(experiments.size()-1).hypotheses.add(h);
			}
		}
		else if(name.equals("subexpt"))
		{
			Experiment e = experiments.get(experiments.size()-1);
			Subexperiment s = e.new Subexperiment();
			s.ID = SAXUtils.getIntAttribute(atts, "id", -1);
			s.title = SAXUtils.getAttribute(atts, "title", "");
			inSubexpt = true;
			e.subexperiments.add(s);
		}
		else if(name.equals("datafile"))
		{
			if(inSubexpt)
			{
				Experiment e = experiments.get(experiments.size()-1);
				Subexperiment s = e.subexperiments.get(e.subexperiments.size()-1);
				s.datafile.add(SAXUtils.getAttribute(atts, "path", ""));
			}
			else
			{
				Experiment e = experiments.get(experiments.size()-1);
				e.datafiles.add(SAXUtils.getAttribute(atts, "path", ""));
			}
		}
	}
	
	public void endExperimentElement (String name) throws SAXException
	{
		if(name.equals("conclusion"))
		{
			if(inSubexpt)
			{
				Experiment e = experiments.get(experiments.size()-1);
				e.subexperiments.get(e.subexperiments.size()-1).conlcusions.add(stringBuilder.toString());
				stringBuilder = new StringBuilder();
			}
			else
			{
				Experiment e = experiments.get(experiments.size()-1);
				e.conclusions.add(stringBuilder.toString());
				stringBuilder = new StringBuilder();
			}
		}
		else if(name.equals("subexpt"))
		{
			inSubexpt = false;
		}
	}
}
