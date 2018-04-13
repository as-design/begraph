package model.implementation;

import de.begraph.model.GLColor3f;
import de.begraph.model.GLColor4f;
import de.begraph.model.abstracts.AbstractEdgeType;

public class EdgeType extends AbstractEdgeType {

	public EdgeType(String name, GLColor4f firstColor, GLColor3f secondColor) {
		super(name, firstColor, secondColor);
	}

}
