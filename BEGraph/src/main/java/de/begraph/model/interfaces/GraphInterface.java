package de.begraph.model.interfaces;

import java.util.ArrayList;
import java.util.Vector;

import de.begraph.model.GLColor4f;
import de.begraph.model.GraphSetting;


public interface GraphInterface {
	public int getGraphRadius();
	public void setGraphRadius(int radius);
	
	public int getnSpace();
	public void setnSpace(int space);
	
	public int getMaxDepth();
	public void setMaxDepth(int maxDepth);
	
	public float getLineWidth();
	public void setLineWidth(float width);
	
	public String getInfoText();
	public void setInfoText(String info);
	
	public int getGraphRadiusEdgeCollect();
	public void setGraphRadiusEdgeCollect(int graphRadiusEdgeCollect);

	public int getNodeContainerLength();
	public void setNodeContainerLength(int nodeContainerLength);

	public int getNodeMinLength();
	public void setNodeMinLength(int nodeMinLength);

	public int getNodeMaxLength();
	public void setNodeMaxLength(int nodeMaxLength);

	public int getAccuracy();
	public void setAccuracy(int accuracy);
	
	public void addEdge(EdgeInterface e);
	public void addNodeContainer(NodeContainerInterface nc);
	
	public ArrayList<EdgeInterface> getAllEdges();
	public ArrayList<NodeInterface> getAllNodes();
	public ArrayList<NodeContainerInterface> getAllNodeContainers();
	public ArrayList<ElementInterface> getAllElements();
	
	public void calc();
	public NodeContainerInterface getHead();

	public void setEdgeSelectedColor(GLColor4f edgeSelectedColor);
	public GLColor4f getEdgeSelectedColor();
	
	public void setEdgeUnselectedColor(GLColor4f edgeUnselectedColor);
	public GLColor4f getEdgeUnselectedColor();
}