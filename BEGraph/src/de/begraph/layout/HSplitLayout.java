package de.begraph.layout;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;

/**
 * 
 * @author andreas
 *
 * This is the layout for the begraph
 */
public class HSplitLayout extends Layout{

	@Override
	protected Point computeSize(Composite composite, int wHint, int hHint,
			boolean flushCache) {
            return new Point(200, 100);
	}

	@Override
	protected void layout(Composite composite, boolean flushCache) {
		int hTop = 22;
		int hButton = 18;
		int h = (composite.getBounds().height)-(hTop+hButton);
        composite.getChildren()[0].setBounds(0, 0, composite.getBounds().width, hTop);
        composite.getChildren()[1].setBounds(0, hTop, composite.getBounds().width, h);
        composite.getChildren()[2].setBounds(0, h+hTop, composite.getBounds().width, hButton);
	}
}
