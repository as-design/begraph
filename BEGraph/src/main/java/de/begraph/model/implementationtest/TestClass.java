package de.begraph.model.implementationtest;

import de.begraph.model.abstracts.AbstractNode;

public class TestClass extends AbstractNode{

	AbstractNode node;
	
	public TestClass(String name, float length, float width, float tinge){
		super(length, width,  tinge);
		setName(name);
	}

	public String getTooltip() {
		String tooltip = "Tooltip ";
		tooltip += getName();
		return tooltip;
	}

	public void onDoubleClick() {
	}

	@Override
	public String getSearchString() {
		return this.getName();
	}
}
