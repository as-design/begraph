package de.begraph.model.interfaces;

import java.nio.FloatBuffer;

public interface ElementInterface {
	
	public void setVisible(boolean visible);
	public boolean isVisible();
	
	public void setSelected(boolean selected);
	public boolean isSelected();
	
	public void setHighlighted(boolean highlighted);
	public boolean isHighlighted();
	
	public FloatBuffer getPositionBuffer();
	public void setPositionBuffer(FloatBuffer positionBuffer);
	
	public String getTooltip();
	public String getSearchString();
}
