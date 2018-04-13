package de.begraph.model.implementationtest;

import de.begraph.model.GLColor3f;
import de.begraph.model.GLColor4f;
import de.begraph.model.abstracts.AbstractEdgeType;

public class TestEdgeType extends AbstractEdgeType {

	public TestEdgeType(String name, GLColor4f firstColor, GLColor3f secondColor){
		super(name, firstColor, secondColor);
	}
	
}
