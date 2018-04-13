package de.begraph.model.implementationtest;


import de.begraph.model.abstracts.AbstractNodeContainer;
import de.begraph.model.interfaces.NodeContainerInterface;

public class TestPackage extends AbstractNodeContainer {
	
	public TestPackage(String name, NodeContainerInterface parent){
		super(name, parent);
	}

	public String getTooltip() {
		return getName();
	}

	@Override
	public String getSearchString() {
		return getName();
	}
}
