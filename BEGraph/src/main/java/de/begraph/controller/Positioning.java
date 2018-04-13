package de.begraph.controller;

import java.nio.FloatBuffer;

import de.begraph.model.GraphSetting;
import de.begraph.model.interfaces.EdgeInterface;
import de.begraph.model.interfaces.GraphInterface;
import de.begraph.model.interfaces.NodeContainerInterface;
import de.begraph.model.interfaces.NodeInterface;
import de.begraph.util.Constants;


public class Positioning {
	public static void calcPositionBuffer(NodeInterface node, GraphInterface graph){
		int a = node.getPosition();
		
		float r = (float)(graph.getGraphRadius() + graph.getNodeMinLength() + (node.getLength()*(graph.getNodeMaxLength()-graph.getNodeMinLength())));
		float angle = 2 * Constants.PI * 1 * (a + (0.5f-(0.1f+0.4f*node.getWidth())))  / graph.getnSpace();
		float angle2 = 2 * Constants.PI * (a + 0.5f + (0.1f+0.4f*node.getWidth())) / graph.getnSpace();
		FloatBuffer pointBuffer = FloatBuffer.allocate(8);
		pointBuffer.put(r * (float) Math.cos(angle));
		pointBuffer.put(r * (float) Math.sin(angle));
		pointBuffer.put(r * (float) Math.cos(angle2));
		pointBuffer.put(r * (float) Math.sin(angle2));
		pointBuffer.put((graph.getGraphRadius()) * (float) Math.cos(angle2));
		pointBuffer.put((graph.getGraphRadius()) * (float) Math.sin(angle2));
		pointBuffer.put((graph.getGraphRadius()) * (float) Math.cos(angle));
		pointBuffer.put((graph.getGraphRadius()) * (float) Math.sin(angle));
		node.setPositionBuffer(pointBuffer);
	}
	
	public static void calcPositionBuffer(NodeContainerInterface nc, GraphInterface graph){
		int breite = 1;
		FloatBuffer pointBuffer = FloatBuffer.allocate(4*(nc.getSize()+1));
		float r = graph.getGraphRadius()+ graph.getNodeMaxLength() + ((1+graph.getMaxDepth()-nc.getDepth())*breite);
		float angle;
		for (int i = 0; i <= nc.getSize(); i++) {
			angle = (float) (2 * Constants.PI * (i + nc.getPosition()) / graph.getnSpace());
			pointBuffer.put(graph.getGraphRadius() * (float) Math.cos(angle));
			pointBuffer.put(graph.getGraphRadius() * (float) Math.sin(angle));
			pointBuffer.put((r + breite) * (float) Math.cos(angle));
			pointBuffer.put((r + breite)
					* (float) Math.sin(angle));
		}
		nc.setPositionBuffer(pointBuffer);
	}
	
	/*
	 * calcPositionBuffer calculates the curve of the edge between two edges. The points of the curve are
	 * stored into the CurveBuffer
	 */
	public static void calcPositionBuffer(EdgeInterface edge, GraphInterface graph) {
		float s1x, s1y, s2x, s2y, s3x, s3y, s4x, s4y;
		float d1x, d1y, d2x, d2y, d3x, d3y, d4x, d4y;
		FloatBuffer pointBuffer;
		FloatBuffer curveBuffer;
		int r = graph.getGraphRadius();
		float mid1, mid2;
		
		// In case the edge has the same source and destination
		if(edge.getFirstNode()==edge.getSecondNode()){
			pointBuffer = FloatBuffer.allocate(14);
			float angle = 2 * Constants.PI * 1
			* (edge.getFirstNode().getPosition() + 0.5f) / graph.getnSpace();
			float angle2 = 2 * Constants.PI * 1
			* (edge.getFirstNode().getPosition()-.5f) / graph.getnSpace();
			float angle3 = 2 * Constants.PI * 1
			* (edge.getFirstNode().getPosition() + 1.5f) / graph.getnSpace();
			s1x = r * (float) Math.cos(angle);
			s1y = r * (float) Math.sin(angle);
			s2x = (r-2) * (float) Math.cos(angle2);
			s2y = (r-2) * (float) Math.sin(angle2);
			s3x = (r - 2) * (float) Math.cos(angle);
			s3y = (r - 2) * (float) Math.sin(angle);
			s4x = (r-2) * (float) Math.cos(angle3);
			s4y = (r-2) * (float) Math.sin(angle3);
			
			pointBuffer.put(0, s1x);
			pointBuffer.put(1, s1y);
			pointBuffer.put(3, s2x);
			pointBuffer.put(4, s2y);
			pointBuffer.put(6, s3x);
			pointBuffer.put(7, s3y);
			pointBuffer.put(9, s4x);
			pointBuffer.put(10, s4y);
			pointBuffer.put(12, s1x);
			pointBuffer.put(13, s1y);
			
			curveBuffer = FloatBuffer.allocate((graph.getAccuracy()*2)+2);
			float a = 1, b = 0, ac=(float)1/(graph.getAccuracy()/2);
			for (int i = 0; i <= graph.getAccuracy()/2 ; i++) {
				curveBuffer.put(i * 2, pointBuffer.get(0) * a * a
						+ pointBuffer.get(3) * 2 * a * b
						+ pointBuffer.get(6) * b * b);
				curveBuffer.put((i * 2) + 1, pointBuffer.get(1) * a * a
						+ pointBuffer.get(4) * 2 * a * b
						+ pointBuffer.get(7) *b * b);
				a -= ac;
				b = 1 - a;
			}
			a = 1;
			b = 0;
			ac=(float)1/(graph.getAccuracy()/2);
			for (int i = graph.getAccuracy()/2+1; i <= graph.getAccuracy() ; i++) {
				curveBuffer.put(i * 2,pointBuffer.get(6) * a * a
						+ pointBuffer.get(9) * 2 * a * b
						+ pointBuffer.get(12) * b * b);
				curveBuffer.put((i * 2) + 1, pointBuffer.get(7) * a * a
						+ pointBuffer.get(10) * 2 * a * b
						+ pointBuffer.get(13) * b * b);
				a -= ac;
				b = 1 - a;
			}
			
		}else{
			pointBuffer = FloatBuffer.allocate(14);
			float mid1tmp = edge.getFirstNode().getParent().getPosition() + (edge
					.getFirstNode().getParent().getSize() / 2);
			float mid2tmp = edge.getSecondNode().getParent().getPosition() + (edge
					.getSecondNode().getParent().getSize() / 2);
			if (mid1tmp - mid2tmp > 0) {
				mid1 = mid1tmp + ((mid1tmp - mid2tmp) - (graph.getnSpace() / 2)) / 50;
			} else {
				mid1 = mid1tmp + ((mid1tmp - mid2tmp) + (graph.getnSpace() / 2)) / 50;
			}
			if (mid2tmp - mid1tmp > 0) {
				mid2 = mid2tmp + ((mid2tmp - mid1tmp) - (graph.getnSpace() / 2)) / 50;
			} else {
				mid2 = mid2tmp + ((mid2tmp - mid1tmp) + (graph.getnSpace() / 2)) / 50;
			}
			
			float angle = 2 * Constants.PI * 1
			* (edge.getFirstNode().getPosition() + 0.5f) / graph.getnSpace();
			float angle2 = 2 * Constants.PI * (mid1tmp) / graph.getnSpace();
			
			s1x = r * (float) Math.cos(angle);
			s1y = r * (float) Math.sin(angle);
			s2x = (r - graph.getGraphRadiusEdgeCollect()/2) * (float) Math.cos(angle);
			s2y = (r - graph.getGraphRadiusEdgeCollect()/2) * (float) Math.sin(angle);
			s3x = (r - graph.getGraphRadiusEdgeCollect()/2) * (float) Math.cos(angle2);
			s3y = (r - graph.getGraphRadiusEdgeCollect()/2) * (float) Math.sin(angle2);
			s4x = (r - graph.getGraphRadiusEdgeCollect()) * (float) Math.cos(angle2);
			s4y = (r - graph.getGraphRadiusEdgeCollect()) * (float) Math.sin(angle2);
	
			angle2 = 2 * Constants.PI * (mid2tmp) / graph.getnSpace();
			angle = 2 * Constants.PI * (edge.getSecondNode().getPosition() + 0.5f)
					/ graph.getnSpace();
			
			d1x = r * (float) Math.cos(angle);
			d1y = r * (float) Math.sin(angle);
			d2x = (r - graph.getGraphRadiusEdgeCollect()/2) * (float) Math.cos(angle);
			d2y = (r - graph.getGraphRadiusEdgeCollect()/2) * (float) Math.sin(angle);
			d3x = (r - graph.getGraphRadiusEdgeCollect()/2) * (float) Math.cos(angle2);
			d3y = (r - graph.getGraphRadiusEdgeCollect()/2) * (float) Math.sin(angle2);
			d4x = (r - graph.getGraphRadiusEdgeCollect()) * (float) Math.cos(angle2);
			d4y = (r - graph.getGraphRadiusEdgeCollect()) * (float) Math.sin(angle2);
	
			pointBuffer.put(s1x);
			pointBuffer.put(s1y);
			pointBuffer.put(s2x);
			pointBuffer.put(s2y);
			pointBuffer.put(s3x);
			pointBuffer.put(s3y);
			pointBuffer.put(0);
			pointBuffer.put(0);
			pointBuffer.put(d3x);
			pointBuffer.put(d3y);
			pointBuffer.put(d2x);
			pointBuffer.put(d2y);
			pointBuffer.put(d1x);
			pointBuffer.put(d1y);
	
			curveBuffer = FloatBuffer.allocate((graph.getAccuracy()*2)+2);
			float a = 1, b = 0, ac=(float)1/graph.getAccuracy();
			float q = 0.2f;
			for (int i = 0; i <= graph.getAccuracy() ; i++) {
				curveBuffer.put(i * 2, (float) (pointBuffer.get(0) * Math.pow(a, 6)
						+ pointBuffer.get(2) * 6 * Math.pow(a, 5) * b
						+ pointBuffer.get(4) * 15 * Math.pow(a, 4) * Math.pow(b, 2)
						+ pointBuffer.get(6) * 20 * Math.pow(a, 3) * Math.pow(b, 3)
						+ pointBuffer.get(8) * 15 * Math.pow(a, 2) * Math.pow(b, 4)
						+ pointBuffer.get(10) * 6 * a * Math.pow(b, 5)
						+ pointBuffer.get(12) * Math.pow(b, 6)));
				curveBuffer.put((i * 2) + 1, (float) (pointBuffer.get(1) * Math.pow(a, 6)
						+ pointBuffer.get(3) * 6 * Math.pow(a, 5) * b
						+ pointBuffer.get(5) * 15 * Math.pow(a, 4) * Math.pow(b, 2)
						+ pointBuffer.get(7) * 20 * Math.pow(a, 3) * Math.pow(b, 3)
						+ pointBuffer.get(9) * 15 * Math.pow(a, 2) * Math.pow(b, 4)
						+ pointBuffer.get(11) * 6 * a * Math.pow(b, 5)
						+ pointBuffer.get(13) * Math.pow(b, 6)));
				a -= ac;
				b = 1 - a;
			}
		}
		edge.setPositionBuffer(curveBuffer);
	}
}