package de.begraph.test;

import java.util.ArrayList;

import de.begraph.model.GLColor3f;
import de.begraph.model.GLColor4f;
import de.begraph.model.implementationtest.TestClass;
import de.begraph.model.implementationtest.TestEdge;
import de.begraph.model.implementationtest.TestEdgeType;
import de.begraph.model.implementationtest.TestGraph;
import de.begraph.model.implementationtest.TestPackage;
import de.begraph.model.interfaces.EdgeTypeInterface;
import de.begraph.model.interfaces.GraphInterface;
import de.begraph.model.interfaces.NodeContainerInterface;
import de.begraph.model.interfaces.NodeInterface;


public class Test {
	
	public static GraphInterface getTestgraph(int countNodes, int countNodeContainer, int countEdges) {

		GraphInterface testgraph = new TestGraph("Test");

		ArrayList<NodeContainerInterface> tmp = new ArrayList<NodeContainerInterface>();
		for(int i=0; i<countNodeContainer;i++){
			tmp.add(new TestPackage("P "+i, testgraph.getHead()));
		}
		
		ArrayList<NodeInterface> tmpc = new ArrayList<NodeInterface>();
		
		for (int i = 1; i < countNodes; i++) {
			int r = (int) (Math.pow(Math.random(),2) * tmp.size());

			
			NodeInterface n = new TestClass("Class " + i, (float) Math.random(), (float) Math.random(), (float) Math.random());
			//n.setPath("/pathtoclass/Class"+i);
			tmpc.add(n);
			tmp.get(r).addNode(n);
		}
		ArrayList<EdgeTypeInterface> tmpTypes = new ArrayList<EdgeTypeInterface>();
		tmpTypes.add(new TestEdgeType("Type1", new GLColor4f(0.f, 0.69f, .39f,0.3f), null));
		//tmpTypes.add(new TestEdgeType("Type1", new GLColor4f(0.f,1.f,0.0f,0.3f), null));
		//tmpTypes.add(new TestEdgeType("Type1", new GLColor4f(1.f,(float)215/256,0.0f,0.3f), null));
		
		for (int i = 0; i < countEdges; i++) {
			int r1 = (int) (Math.random() * tmpc.size());
			int r2 = (int) (Math.random() * tmpc.size());
			int r3 = (int) (Math.random() * tmpTypes.size());
			testgraph.addEdge(new TestEdge(tmpc.get(r1), tmpc.get(r2),tmpTypes.get(r3)));
		}
		 
		return testgraph;
	}
}
