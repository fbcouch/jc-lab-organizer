package com.LabOrganizer;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;

public class Experiment extends DefaultMutableTreeNode {
	public int ID = -1;
	public String name = "";
	public String title = "";
	public String date = "";
	public String infofile = "";
	public String description = "";
	public ArrayList<Hypothesis> hypotheses = new ArrayList<Hypothesis>();
	public ArrayList<String> datafiles = new ArrayList<String>();
	public ArrayList<String> conclusions = new ArrayList<String>();
	public ArrayList<Subexperiment> subexperiments = new ArrayList<Subexperiment>();
	
	@Override
	public String toString() {
		return this.name + ": " + this.title;
	}
	
	public class Subexperiment extends DefaultMutableTreeNode {
		public int ID = -1;
		public String title = "";
		public ArrayList<String> datafile = new ArrayList<String>();
		public ArrayList<String> conlcusions = new ArrayList<String>();
		
		@Override
		public String toString() {
			return ("".equals(title)?""+ID:title);
		}
	}
}
