package model.implementation;

import de.begraph.model.GLColor3f;
import de.begraph.model.GLColor4f;
import de.begraph.model.abstracts.AbstractEdgeType;

public class XRayEdgeType extends AbstractEdgeType{

	public XRayEdgeType(String name, GLColor4f firstColor, GLColor3f secondColor){
		super(name, firstColor, secondColor);
	}
	
}

