package de.begraph.model.implementationtest;

import de.begraph.model.abstracts.AbstractEdge;
import de.begraph.model.interfaces.EdgeTypeInterface;
import de.begraph.model.interfaces.NodeInterface;

public class TestEdge extends AbstractEdge{
	public TestEdge(NodeInterface first, NodeInterface second, EdgeTypeInterface type) {
		super(first, second, type);
	}
	public String toString(){
		return this.getFirstNode().getName()+" "+this.getSecondNode().getName();
	}

	public String getTooltip() {
		return getFirstNode().getName()+"-->"+getSecondNode().getName();
	}
	@Override
	public String getSearchString() {
		return toString();
	}
}
