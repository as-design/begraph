package model.implementation;

import de.begraph.model.abstracts.AbstractEdge;
import de.begraph.model.interfaces.EdgeTypeInterface;
import de.begraph.model.interfaces.NodeInterface;

public class XRayEdge extends AbstractEdge{
	public XRayEdge(NodeInterface first, NodeInterface second, EdgeTypeInterface type) {
		super(first, second, type);
	}
	public String toString(){
		return this.getFirstNode().getName()+" "+this.getSecondNode().getName();
	}

	@Override
	public String getTooltip() {
		return getFirstNode().getName()+" uses "+getSecondNode().getName();
	}
	
	@Override
	public String getSearchString() {
		return toString();
	}
}
