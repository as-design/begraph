package de.begraph.controller;

import static org.lwjgl.opengl.GL11.*;

import de.begraph.model.interfaces.EdgeInterface;
import de.begraph.model.interfaces.EdgeTypeInterface;
import de.begraph.model.interfaces.NodeContainerInterface;
import de.begraph.model.interfaces.NodeInterface;
import de.begraph.util.Constants;

public class Drawing {

	public static void drawPackage(NodeContainerInterface nc) {
		nc.getPositionBuffer().position(0);
		glBegin(GL_TRIANGLE_STRIP);
		for (int i = 0; i < nc.getPositionBuffer().capacity() / 4; i++) {
			glVertex2f(nc.getPositionBuffer().get(), nc.getPositionBuffer()
					.get());
			glVertex2f(nc.getPositionBuffer().get(), nc.getPositionBuffer()
					.get());
		}
		glEnd();
	}

	public static void drawNodeArea(NodeInterface node) {
		node.getPositionBuffer().position(0);
		glBegin(GL_TRIANGLE_STRIP);
		glVertex2f(node.getPositionBuffer().get(2), node.getPositionBuffer().get(3));
		glVertex2f(node.getPositionBuffer().get(0), node.getPositionBuffer().get(1));
		glVertex2f(node.getPositionBuffer().get(4), node.getPositionBuffer().get(5));
		glVertex2f(node.getPositionBuffer().get(6), node.getPositionBuffer().get(7));
		glEnd();
	}

	public static void drawNodeFrame(NodeInterface node) {
		node.getPositionBuffer().position(0);
		glBegin(GL_LINE_LOOP);
		glVertex2f(node.getPositionBuffer().get(), node.getPositionBuffer().get());
		glVertex2f(node.getPositionBuffer().get(), node.getPositionBuffer().get());
		glVertex2f(node.getPositionBuffer().get(), node.getPositionBuffer().get());
		glVertex2f(node.getPositionBuffer().get(), node.getPositionBuffer().get());
		glEnd();
	}

	public static void drawEdge(EdgeInterface edge, boolean checkSelection) {
		edge.getPositionBuffer().position(0);
		EdgeTypeInterface type = edge.getEdgeType();
		boolean twocolors = (type.getSecondColor() == null) ? false : true;
		float dr = 0, dg = 0, db = 0;
		if (!checkSelection) {
			if (twocolors) {
				dr = type.getSecondColor().getRed()
						- type.getFirstColor().getRed();
				dg = type.getSecondColor().getGreen()
						- type.getFirstColor().getGreen();
				db = type.getSecondColor().getBlue()
						- type.getFirstColor().getBlue();
			} else {
				glColor4f(type.getFirstColor().getRed(), type.getFirstColor()
						.getGreen(), type.getFirstColor().getBlue(), type
						.getFirstColor().getTransparency());
			}
		}

		glBegin(GL_LINE_STRIP);
		for (int i = 0; i < edge.getPositionBuffer().capacity() / 2; i++) {
			if (!checkSelection && twocolors) {
				float f = (float) i / (edge.getPositionBuffer().capacity() / 2);

				glColor4f(type.getFirstColor().getRed() + dr * f, type
						.getFirstColor().getGreen() + dg * f, type
						.getFirstColor().getBlue() + db * f, type
						.getFirstColor().getTransparency());
			}
			glVertex2f(edge.getPositionBuffer().get(), edge.getPositionBuffer().get());
		}
		glEnd();
	}

	public static void drawCircle(int r) {
		int edges = 512;
		float angle, angle2;
		for (int i = 0; i < edges; i++) {
			if (i % 2 == 1) {
				glBegin(GL_LINE_STRIP);
				angle = 2 * Constants.PI * i / edges;
				angle2 = 2 * Constants.PI * (i + 1) / edges;
				glVertex2f(r * (float) Math.cos(angle), r
						* (float) Math.sin(angle));
				glVertex2f(r * (float) Math.cos(angle2), r
						* (float) Math.sin(angle2));
				glEnd();
			}
		}
	}
}
