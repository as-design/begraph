package de.begraph.model.interfaces;

import java.nio.FloatBuffer;

public interface NodeInterface extends ElementInterface {
	public String getName();
	public void setName(String name);
	
	public int getPosition();
	public void setPosition(int position);
	
	public float getLength();
	public void setLength(float length);
	
	public float getWidth();
	public void setWidth(float width);
	
	public float getTinge();
	public void setTinge(float tinge);
	
	public void setParent(NodeContainerInterface nc);
	public NodeContainerInterface getParent();
	
	public void onDoubleClick();
}
