package de.begraph.model.interfaces;

import java.nio.FloatBuffer;

public interface EdgeInterface extends ElementInterface {
	
	/**
	 * Set the first node of the edge
	 * 
	 * @param n1 (source)
	 */
	public void setFirstNode(NodeInterface n1);
	public NodeInterface getFirstNode();
	
	/**
	 * Set the second node of the edge
	 * 
	 * @param n2 (destination)
	 */
	public void setSecondNode(NodeInterface n2);
	public NodeInterface getSecondNode();
	
	public void setEdgeType(EdgeTypeInterface type);
	public EdgeTypeInterface getEdgeType();
}