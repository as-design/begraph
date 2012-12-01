package de.begraph.model.abstracts;

import java.nio.FloatBuffer;

import de.begraph.model.interfaces.NodeContainerInterface;
import de.begraph.model.interfaces.NodeInterface;


public abstract class AbstractNode implements NodeInterface{
	private String name;
	private int position, size;
	private float length, width, tinge;
	private boolean visible, selected, highlighted;
	private NodeContainerInterface parent;
	private FloatBuffer positionBuffer;
	
	public AbstractNode(float length, float width, float tinge){
		this.length = length;
		this.width = width;
		this.tinge = tinge;
		this.size = 1;
		setVisible(true);
		setHighlighted(false);
		setSelected(false);
		
	}
	
	public void createNode(String name, int position, NodeContainerInterface nc){
		this.setName(name);
		this.parent = nc;
	}
	
	public void setParent(NodeContainerInterface nc){
		this.parent = nc;
	}
	
	public NodeContainerInterface getParent(){
		return this.parent;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPosition() {
		return position;
	}

	public boolean isHighlighted(){
		return this.highlighted;
	}

	public void setHighlighted(boolean highlighted){
		this.highlighted = highlighted;
	}
	
	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}	

	public FloatBuffer getPositionBuffer() {
		return this.positionBuffer;
	}

	public final void setPositionBuffer(FloatBuffer positionBuffer) {
		this.positionBuffer = positionBuffer;
	}

	public float getLength() {
		return this.length;
	}

	public void setLength(float length) {
		this.length = length;
	}
	
	public float getWidth(){
		return this.width;
	}
	
	public void setWidth(float width){
		this.width=width;
	}
	
	public float getTinge() {
		return this.tinge;
	}

	public void setTinge(float tinge) {
		this.tinge = tinge;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return this.selected;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return this.visible;
	}
	
	public abstract String getTooltip();
}
