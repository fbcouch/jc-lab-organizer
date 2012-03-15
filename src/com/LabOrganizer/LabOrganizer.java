package com.LabOrganizer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.LabOrganizer.Experiment.Subexperiment;

public class LabOrganizer extends JFrame implements TreeSelectionListener {
	
	JSplitPane splitPane = new JSplitPane();
	DefaultMutableTreeNode top = new DefaultMutableTreeNode("Jami's Lab Organizer");
	JTree mainTree = new JTree(top);
	JTabbedPane mainView = new JTabbedPane(JTabbedPane.TOP);
	
	ArrayList<Experiment> experiments = new ArrayList<Experiment>();
	ArrayList<Hypothesis> hypotheses = new ArrayList<Hypothesis>();
	
	
	public LabOrganizer() {
		setTitle("Jami's Lab Organizer");
		setBounds(100,100,700,600);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		getContentPane().add(splitPane, BorderLayout.CENTER);
				
		splitPane.setRightComponent(mainView);
				
		splitPane.setLeftComponent(mainTree);
		mainTree.setPreferredSize(new Dimension(200,mainTree.getPreferredSize().height));
		setVisible(true);
		mainTree.addTreeSelectionListener(this);
		
		loadHypotheses();
		loadExperiments();
		
		DefaultMutableTreeNode experimentParent = new DefaultMutableTreeNode("Experiments");
		DefaultMutableTreeNode hypothesisParent = new DefaultMutableTreeNode("Hypotheses");
		
		top.add(experimentParent);
		top.add(hypothesisParent);
		for(int i=0;i<experiments.size();i++)
		{
			Experiment expt = experiments.get(i);
			experimentParent.add(expt);
			System.out.println(expt.name + " - " + expt.title);
			for(int j=0;j<expt.subexperiments.size();j++)
			{
				Subexperiment sub = expt.subexperiments.get(j);
				expt.add(sub);
			}
		}
		
		for(int i=0;i<hypotheses.size();i++) { hypothesisParent.add(hypotheses.get(i)); }
	}
	
	public void loadExperiments()
	{
		
		try {
			XMLReader reader = XMLReaderFactory.createXMLReader();
			OrganizerXMLHandler handler = new OrganizerXMLHandler();
			reader.setContentHandler(handler);
		
			handler.hypotheses = hypotheses;
			handler.whatToParse = "experiments";
			reader.parse(new InputSource("data/experiments.xml"));
			experiments = handler.experiments;
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public void loadHypotheses()
	{
		try {
			XMLReader reader = XMLReaderFactory.createXMLReader();
			OrganizerXMLHandler handler = new OrganizerXMLHandler();
			reader.setContentHandler(handler);
		
			handler.whatToParse = "hypotheses";
			reader.parse(new InputSource("data/hypotheses.xml"));
			hypotheses = handler.hypotheses;
		} catch (Exception e) { e.printStackTrace(); }
	}

	@Override
	public void valueChanged(TreeSelectionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource() instanceof Hypothesis)
		{
			System.out.println("Tree selection - Hypothesis " + ((Hypothesis)arg0.getSource()).name);
		}
		else if(arg0.getSource() instanceof Experiment)
		{
			System.out.println("Tree selection - Experiment " + ((Experiment)arg0.getSource()).name);
		}
		else if(arg0.getSource() instanceof Subexperiment)
		{
			System.out.println("Tree selection - Subexperiment " + ((Subexperiment)arg0.getSource()).title);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
            // Set cross-platform Java L&F (also called "Metal")
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (UnsupportedLookAndFeelException e) {
			// handle exception
		}
		catch (ClassNotFoundException e) {
			// handle exception
		}
		catch (InstantiationException e) {
			// handle exception
		}
		catch (IllegalAccessException e) {
			// handle exception
		}
		new LabOrganizer();
	}



}
