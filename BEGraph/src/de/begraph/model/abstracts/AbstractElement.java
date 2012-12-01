package de.begraph.model.abstracts;

import de.begraph.model.interfaces.ElementInterface;

public abstract class AbstractElement implements ElementInterface {
	boolean highlighted;
	
	public boolean isHighlighted(){
		return this.highlighted;
	}

	public void setHighlighted(boolean highlighted){
		this.highlighted = highlighted;
	}
}
