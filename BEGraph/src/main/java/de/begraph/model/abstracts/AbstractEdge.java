package de.begraph.model.abstracts;

import java.nio.FloatBuffer;

import de.begraph.model.interfaces.EdgeInterface;
import de.begraph.model.interfaces.EdgeTypeInterface;
import de.begraph.model.interfaces.NodeInterface;


public abstract class AbstractEdge implements EdgeInterface {
	NodeInterface first_, second_;
	private boolean visible, selected, highlighted;
	private FloatBuffer positionBuffer;
	private EdgeTypeInterface type;
	
	public AbstractEdge(NodeInterface first, NodeInterface second, EdgeTypeInterface type){
		first_ = first;
		second_ = second;
		this.type = type;
		setVisible(true);
		setSelected(true);
	}
	
	@Override
	public void setFirstNode(NodeInterface n1){
		first_ = n1;
	}
	
	@Override
	public NodeInterface getFirstNode(){
		return first_;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return this.visible;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return this.selected;
	}
	
	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	public boolean isHighlighted() {
		return this.highlighted;
	}
	
	public void setSecondNode(NodeInterface n2){
		second_ = n2;
	}
	
	public NodeInterface getSecondNode(){
		return second_;
	}
	
	public FloatBuffer getPositionBuffer() {
		return this.positionBuffer;
	}

	public final void setPositionBuffer(FloatBuffer positionBuffer) {
		this.positionBuffer = positionBuffer;
	}
	
	public void setEdgeType(EdgeTypeInterface type){
		this.type = type;
	}
	public EdgeTypeInterface getEdgeType(){
		return this.type;
	}
}
