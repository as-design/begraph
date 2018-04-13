package de.begraph.model;

public class GraphSetting {
	private int graphRadius;
	private int graphRadiusEdgeCollect;
	private int nodeContainerLength;
	private int nodeMinLength, nodeMaxLength;
	private int accuracy;
	private int nSpace;
	
	public GraphSetting(){
		this.graphRadius = 10;
		this.graphRadiusEdgeCollect = 5;
		this.nodeContainerLength = 3;
		this.nodeMinLength = 2;
		this.nodeMaxLength = 4;
	}
	
	public int getGraphRadius() {
		return graphRadius;
	}
	public void setGraphRadius(int graphRadius) {
		this.graphRadius = graphRadius;
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

	public int getnSpace() {
		return nSpace;
	}

	public void setnSpace(int nSpace) {
		this.nSpace = nSpace;
	}
}
