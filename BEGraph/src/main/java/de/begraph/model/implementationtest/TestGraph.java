package de.begraph.model.implementationtest;

import de.begraph.model.implementationtest.TestPackage;
import de.begraph.model.abstracts.AbstractGraph;

public class TestGraph extends AbstractGraph {
	public TestGraph(String name){
		super(new TestPackage(name, null));
	}
}
