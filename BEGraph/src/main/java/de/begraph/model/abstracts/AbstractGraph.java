package de.begraph.model.abstracts;

import java.util.ArrayList;

import de.begraph.controller.Positioning;
import de.begraph.model.GLColor4f;
import de.begraph.model.interfaces.EdgeInterface;
import de.begraph.model.interfaces.ElementInterface;
import de.begraph.model.interfaces.GraphInterface;
import de.begraph.model.interfaces.NodeContainerInterface;
import de.begraph.model.interfaces.NodeInterface;


public abstract class AbstractGraph implements GraphInterface {
	NodeContainerInterface head;
	ArrayList<EdgeInterface> edges = new ArrayList<EdgeInterface>();
	
	private int nSpace;
	private int maxDepth;
	private int graphRadius;
	private int graphRadiusEdgeCollect;
	private int nodeContainerLength;
	private int nodeMinLength, nodeMaxLength;
	private int accuracy;
	
	private String info;
	private float lineWidth;
	private GLColor4f EdgeSelectedColor, EdgeUnselectedColor;
	
	public AbstractGraph(NodeContainerInterface head){
		this.head = head;
		setnSpace(0);
		setMaxDepth(0);
		setLineWidth(1);
		setGraphRadius(10);
		setGraphRadiusEdgeCollect(4);
		setNodeContainerLength(3);
		setNodeMinLength(2); 
		setNodeMaxLength(4);
		setAccuracy(100);
		setEdgeSelectedColor(new GLColor4f(1.f, 0.57f, 0.f, 0.4f));
		setEdgeUnselectedColor(null);
		info = "";
	}
	
	final public int getGraphRadius(){
		return graphRadius;
	}
	
	final public void setGraphRadius(int radius){
		this.graphRadius = radius;
	}
	
	final public int getnSpace(){
		return nSpace;
	}
	
	final public void setnSpace(int space){
		this.nSpace = space;
	}
	
	final public int getMaxDepth(){
		return maxDepth;
	}
	
	final public void setMaxDepth(int maxDepth){
		this.maxDepth = maxDepth;
	}
	
	final public float getLineWidth(){
		return this.lineWidth;
	}
	
	final public void setLineWidth(float width){
		this.lineWidth = width;
	}
	
	final public String getInfoText(){
		return this.info;
	}
	final public void setInfoText(String info){
		this.info = info;
	}
	
	final public void calc(){
		setnSpace(head.sumNodes(0, 0));
		setMaxDepth(head.getMaxDepth());
		
		for (NodeContainerInterface nc : this.getAllNodeContainers()) {
			Positioning.calcPositionBuffer(nc, this);
		}

		for (EdgeInterface edge : this.getAllEdges()) {
			Positioning.calcPositionBuffer(edge, this);
		}

		for (NodeInterface c : this.getAllNodes()) {
			Positioning.calcPositionBuffer(c, this);
		}
	}
	
	final public NodeContainerInterface getHead(){
		return this.head;
	}
	
	final public void addEdge(EdgeInterface e){
		edges.add(e);
	}

	final public void addNodeContainer(NodeContainerInterface nc){
		head.addNodeContainer(nc);
	}

	final public ArrayList<EdgeInterface> getAllEdges(){
		return edges;
	}
	
	final public ArrayList<NodeContainerInterface> getAllNodeContainers(){
		ArrayList<NodeContainerInterface> nc = new ArrayList<NodeContainerInterface>();
		nc.addAll(head.getAllNodeContainers());
		nc.add(head);
		return nc;
	}
	
	final public ArrayList<NodeInterface> getAllNodes(){
		return head.getAllNodes();
	}
	
	final public ArrayList<ElementInterface> getAllElements(){
		ArrayList<ElementInterface> allElements = new ArrayList<ElementInterface>();
		allElements.addAll(getAllNodes());
		allElements.addAll(getAllNodeContainers());
		allElements.addAll(getAllEdges());
		return allElements;
	}

	public void setEdgeSelectedColor(GLColor4f edgeSelectedColor) {
		EdgeSelectedColor = edgeSelectedColor;
	}

	public GLColor4f getEdgeSelectedColor() {
		return EdgeSelectedColor;
	}

	public void setEdgeUnselectedColor(GLColor4f edgeUnselectedColor) {
		EdgeUnselectedColor = edgeUnselectedColor;
	}

	public GLColor4f getEdgeUnselectedColor() {
		return EdgeUnselectedColor;
	}

	public int getGraphRadiusEdgeCollect() {
		return graphRadiusEdgeCollect;
	}

	public void setGraphRadiusEdgeCollect(int graphRadiusEdgeCollect) {
		this.graphRadiusEdgeCollect = graphRadiusEdgeCollect;
	}

	public int getNodeContainerLength() {
		return nodeContainerLength;
	}

	public void setNodeContainerLength(int nodeContainerLength) {
		this.nodeContainerLength = nodeContainerLength;
	}

	public int getNodeMinLength() {
		return nodeMinLength;
	}

	public void setNodeMinLength(int nodeMinLength) {
		this.nodeMinLength = nodeMinLength;
	}

	public int getNodeMaxLength() {
		return nodeMaxLength;
	}

	public void setNodeMaxLength(int nodeMaxLength) {
		this.nodeMaxLength = nodeMaxLength;
	}

	public int getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}
}
