package de.begraph.model.interfaces;

import de.begraph.model.GLColor3f;
import de.begraph.model.GLColor4f;

public interface EdgeTypeInterface {
	public String getName();
	public void setFirstColor(GLColor4f color);
	public GLColor4f getFirstColor();
	public void setSecondColor(GLColor3f color);
	public GLColor3f getSecondColor();
}
