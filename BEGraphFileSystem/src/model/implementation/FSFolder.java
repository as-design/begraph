package model.implementation;


import de.begraph.model.abstracts.AbstractNodeContainer;
import de.begraph.model.interfaces.NodeContainerInterface;

public class FSFolder extends AbstractNodeContainer {
	
	public FSFolder(String name, NodeContainerInterface parent){
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
