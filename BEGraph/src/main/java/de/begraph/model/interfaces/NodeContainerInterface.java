package de.begraph.model.interfaces;

import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * The NodeContainerInterface 
 * 
 * @author andreas
 *
 */
public interface NodeContainerInterface extends ElementInterface {
	public String getName();
	public void setName(String name);
	
	public int getPosition();
	public void setPosition(int position);
	
	public int getSize();
	public void setSize(int size);
	
	public int getDepth();
	public void setDepth(int depth);
	
	public int getMaxDepth();

	public int sumNodes(int position, int depth);

	public FloatBuffer getPositionBuffer();
	public void setPositionBuffer(FloatBuffer positionBuffer);

	public void addNode(NodeInterface node);
	public void addNodeContainer(NodeContainerInterface nodecontainer);
	
	public ArrayList<NodeInterface> getAllNodes();
	public ArrayList<NodeContainerInterface> getAllNodeContainers();

	public void toogleMinimize();
}
