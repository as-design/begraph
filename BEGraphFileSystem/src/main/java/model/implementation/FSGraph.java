package model.implementation;

import de.begraph.model.abstracts.AbstractGraph;
import model.implementation.FSFolder;

public class FSGraph extends AbstractGraph {
	
	public FSGraph(String name){
		super(new FSFolder(name, null));
	}
}
