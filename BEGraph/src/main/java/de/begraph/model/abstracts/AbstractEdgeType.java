package de.begraph.model.abstracts;

import de.begraph.model.GLColor3f;
import de.begraph.model.GLColor4f;
import de.begraph.model.interfaces.EdgeTypeInterface;

public abstract class AbstractEdgeType implements EdgeTypeInterface {
	private String name;
	private GLColor4f firstColor;
	private GLColor3f secondColor;

	/**
	 * Class constructor specifying a type for edges
	 * 
	 * @param name
	 *            the name of type
	 * @param firstColor
	 *            the color of the edges of this type
	 * @param secondColor
	 *            second color for a color gradient. Set to null for no color
	 *            gradient.
	 */
	public AbstractEdgeType(String name, GLColor4f firstColor,
			GLColor3f secondColor) {
		this.name = name;
		this.firstColor = firstColor;
		this.secondColor = secondColor;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setFirstColor(GLColor4f color) {
		this.firstColor = color;
	}

	@Override
	public GLColor4f getFirstColor() {
		return this.firstColor;
	}

	@Override
	public void setSecondColor(GLColor3f color) {
		this.secondColor = color;
	}

	@Override
	public GLColor3f getSecondColor() {
		return this.secondColor;
	}

}
