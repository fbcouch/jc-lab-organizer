package com.LabOrganizer;

import javax.swing.tree.DefaultMutableTreeNode;

public class Hypothesis extends DefaultMutableTreeNode {
	public int ID = -1;
	public String name = "";
	public String content = "";
	
	@Override
	public String toString() {
		return (name.equals("")?""+this.ID:this.name);
	}
}
