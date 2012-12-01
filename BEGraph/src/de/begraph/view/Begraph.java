package de.begraph.view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GLContext;

import de.begraph.controller.Drawing;
import de.begraph.layout.HSplitLayout;
import de.begraph.model.GLColor4f;
import de.begraph.model.interfaces.EdgeInterface;
import de.begraph.model.interfaces.ElementInterface;
import de.begraph.model.interfaces.GraphInterface;
import de.begraph.model.interfaces.NodeContainerInterface;
import de.begraph.model.interfaces.NodeInterface;
import de.begraph.test.Test;
import de.begraph.util.Constants;

import static org.lwjgl.opengl.GL11.*;

public class Begraph {

	ByteBuffer buffer = BufferUtils.createByteBuffer(1); // a buffer to check
															// the color
	// values

	private GLCanvas glcanvas; // Used to get OpenGL object that we need to
								// access OpenGL functions

	private GraphInterface graph; // the graph that holds all the elements

	private boolean hold = false; // if mouse is hold or not
	private boolean moved = false; // if mouse was moved or not
	private boolean orthoChange = true; // set to true if the perspective is
										// changed
	private boolean isTooglingContainer = false; // set to true if a node
													// container is toogling

	private float x = 0; // current x-position of the view
	private float y = 0; // current y-position of the view
	private float oldx, oldy; // old values of x and y for comparison
	private float zoom = 20f; // current zoom

	private Text filterText;
	private Label infoTextTop;
	private Label infoTextBottom;

	private float selectionLineWidth = 2f; // lineWidth of edges at
											// checkSelection

	private static byte whiteByte = 127; // for comparison

	private int tUnit = 0;

	NodeInterface nodeFound;
	NodeContainerInterface ncFound;
	private ArrayList<EdgeInterface> edgesFound;
	
	/**
	 * Creates a bundle edges graph of the given graph in form of a glcanvas
	 * with a simple menu
	 * 
	 * @param parent
	 *            a widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param graph
	 *            a implementation of the GraphInterface. Set to null if it is
	 *            loaded later
	 */
	public Begraph(Composite parent, GraphInterface graph) {

		GLData gldata = new GLData();
		gldata.doubleBuffer = true;

		Composite mainFrame = new Composite(parent, SWT.NONE);
		mainFrame.setLayout(new HSplitLayout());

		Composite filterBar = new Composite(mainFrame, SWT.NONE);
		filterBar.setLayout(new RowLayout());

		Label filterLabel = new Label(filterBar, SWT.NONE);
		filterLabel.setText("Search (Regular Expression):");

		filterText = new Text(filterBar, SWT.NONE);
		filterText.setLayoutData(new RowData(200, 15));
		filterText.addKeyListener(regularExpressionListener);

		int operations = DND.DROP_MOVE;
		DropTarget target = new DropTarget(filterText, operations);
		target.setTransfer(new Transfer[] { FileTransfer.getInstance() });

		target.addDropListener(new DropTargetListener() {

			@Override
			public void dropAccept(DropTargetEvent event) {

			}

			@Override
			public void drop(DropTargetEvent event) {
				if (FileTransfer.getInstance().isSupportedType(
						event.currentDataType)) {

					if (event.data instanceof String[]) {
						String[] files = (String[]) event.data;
						if (files.length == 1) {
							String filename = files[0];
							filterText.setText(filename);
						}

					}
				}
				event.detail = DND.DROP_NONE;
			}

			@Override
			public void dragOver(DropTargetEvent event) {

			}

			@Override
			public void dragOperationChanged(DropTargetEvent event) {

			}

			@Override
			public void dragLeave(DropTargetEvent event) {

			}

			@Override
			public void dragEnter(DropTargetEvent event) {

			}
		});

		infoTextTop = new Label(filterBar, SWT.NONE);
		infoTextTop
				.setText("No Project loaded. Please Drag and Drop a Project into the white area.");

		/*Button selectionTest = new Button(filterBar, SWT.NONE);
		selectionTest.setText("Selectiontest");
		selectionTest.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {

			}

			@Override
			public void mouseDown(MouseEvent e) {
				PerformanceTest();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {

			}
		});*/
		
		/*Button renderTest = new Button(filterBar, SWT.NONE);
		renderTest.setText("Rendertest");
		renderTest.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {

			}

			@Override
			public void mouseDown(MouseEvent e) {
				PerformanceRenderTest();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {

			}
		});*/

		/*
		 * bScreen = new Button(filterBar, SWT.NONE); bScreen.setSize(120, 20);
		 * bScreen.setText("Take a screenshot"); bScreen.addMouseListener(new
		 * MouseListener() {
		 * 
		 * public void mouseUp(MouseEvent e) { render(); canvasToImage(0, 0); }
		 * 
		 * public void mouseDown(MouseEvent e) { }
		 * 
		 * public void mouseDoubleClick(MouseEvent e) { } });
		 */

		glcanvas = new GLCanvas(mainFrame, SWT.NO_BACKGROUND, gldata);
		glcanvas.setCurrent();
		try {
			GLContext.useContext(glcanvas);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		Composite infoBar = new Composite(mainFrame, SWT.NONE);
		infoBar.setLayout(new org.eclipse.swt.layout.FillLayout());
		infoBar.setBackground(parent.getShell().getDisplay()
				.getSystemColor(SWT.COLOR_WHITE));

		infoTextBottom = new Label(infoBar, SWT.NONE);
		infoTextBottom.setBackground(parent.getShell().getDisplay()
				.getSystemColor(SWT.COLOR_WHITE));
		infoTextBottom.setText("");

		glcanvas.addMouseWheelListener(new MouseWheelListener() {
			public void mouseScrolled(MouseEvent e) {
				if (e.count > 0){
					zoom = zoom*0.8f;
				}
				if (e.count < 0) {
					zoom = zoom/0.8f;
				}
				/*
				if (e.count > 0 && zoom > 0.6f) {
					if (zoom <= 1) {
						zoom = zoom - 0.1f;
					} else {
						if (zoom <= zoomAdjustment) {
							zoom = zoom - 0.5f;
						} else {
							zoom = zoom - zoomAdjustment;
						}
					}
				}
				if (e.count > 0 && zoom <= 0.6f && zoom > 0.209f) {
					zoom = zoom - 0.01f;
				}
				if (e.count < 0) {
					if (zoom < 1) {
						zoom = zoom + 0.1f;
					} else {
						zoom = zoom + zoomAdjustment;
					}
				}*/
				orthoChange = true;
				render();
			}
		});

		/** Called when the size of the windows is changed */
		glcanvas.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				Rectangle bounds = glcanvas.getBounds();
				float fAspect = (float) bounds.width / (float) bounds.height;
				glcanvas.setCurrent();
				try {
					GLContext.useContext(glcanvas);
				} catch (LWJGLException e1) {
					e1.printStackTrace();
				}

				glViewport(0, 0, bounds.width, bounds.height);
				glMatrixMode(GL_PROJECTION);
				glLoadIdentity();
				// GLU glu = new GLU();
				glFrustum(-0.2 * fAspect, 0.2 * fAspect, -0.2f, 0.2f, 0.2f,
						400f);
				// glu.gluPerspective(45.0f,fAspect , 0.2f, 400.0f);
				// gl.glViewport(0, 0, bounds.width, bounds.height);
				glMatrixMode(GL_MODELVIEW);
				glLoadIdentity();
				orthoChange = true;
				render();
			}
		});

		glcanvas.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				if (hold) {
					moved = true;
					float level = 400/zoom;
					x = x + ((e.x - oldx) / level);
					y = y - ((e.y - oldy) / level);
					oldx = e.x;
					oldy = e.y;
					orthoChange = true;
					render();
				} else {
					checkSelection(e.x, e.y, Constants.ONHOVER);
				}
				//
			}
		});

		glcanvas.addMouseListener(new MouseListener() {
			public void mouseUp(MouseEvent e) {
				hold = false;
				if (!moved) {
					if ((e.stateMask & SWT.CTRL) != 0) {
						checkSelection(e.x, e.y, Constants.HOLDCTRL
								| Constants.ONCLICK);
					} else {
						checkSelection(e.x, e.y, Constants.ONCLICK);
					}
				}
			}

			public void mouseDown(MouseEvent e) {
				if (!hold) {
					hold = true;
					moved = false;
					oldx = e.x;
					oldy = e.y;
				}
				glcanvas.setFocus();
			}

			public void mouseDoubleClick(MouseEvent e) {
				checkSelection(e.x, e.y, Constants.ONDOUBLECLICK);
			}
		});

		// gl.setSwapInterval(1);
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		glColor3f(1.0f, 0.0f, 0.0f);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		glClearDepth(1.0);
		glLineWidth(1.0f);
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		// glEnable(GL2.GL_MAP1_VERTEX_3);
		glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
		glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		glColor3f(0.9f, 0.9f, 0.9f);

		// Activate Antialiasing for rendering
		glEnable(GL_LINE_SMOOTH);

		this.graph = graph;

		recalc();

		// Needed to zoom with mousewheel
		glcanvas.setFocus();
	}

	public void render() {
		render(false);
	}

	/**
	 * Renders every object of the graph in a particular order
	 */
	public void render(final boolean print) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				if (!glcanvas.isDisposed() && graph == null) {
					glcanvas.setCurrent();
					try {
						GLContext.useContext(glcanvas);
					} catch (LWJGLException e) {
						e.printStackTrace();
					}

					// Clears the whole screen
					glClear(GL_COLOR_BUFFER_BIT);

					glcanvas.setToolTipText("No graph loaded!");

					// Display everything
					glcanvas.swapBuffers();
				}

				if (!glcanvas.isDisposed() && graph != null) {
					infoTextTop.setText("");
					// Save current time for displaying the fps
					long fTime = System.currentTimeMillis();

					glcanvas.setCurrent();
					try {
						GLContext.useContext(glcanvas);
					} catch (LWJGLException e) {
						e.printStackTrace();
					}

					// Clears the whole screen
					glClear(GL_COLOR_BUFFER_BIT);

					// Activates Antialiasing for rendering
					glEnable(GL_LINE_SMOOTH);

					glLineWidth(1);

					// Change the section of the picture
					if (orthoChange) {
						glLoadIdentity();
						glOrtho(-zoom - x, zoom - x, -zoom - y, zoom - y, 0, 1);
						orthoChange = false;
					}

					glColor4f(0.3f, 0.3f, 0.3f, .2f);
					Drawing.drawCircle(10 + 2);
					Drawing.drawCircle(10 + 4);

					// Draws the area of the nodecontainers
					glColor4f(0.3f, 0.3f, 0.3f, 0.05f);
					for (NodeContainerInterface p : graph
							.getAllNodeContainers()) {
						if (p.isVisible()) {
							Drawing.drawPackage(p);
						}
					}
					// Draws the highlighted nodecontainers
					glColor4f(1.f, 0f, 0f, 0.2f);
					for (NodeContainerInterface p : graph
							.getAllNodeContainers()) {
						if (p.isVisible() && p.isHighlighted()) {
							Drawing.drawPackage(p);
						}
					}

					// Draws the selected nodecontainers
					glColor4f(0.3f, 0.3f, 0.3f, 0.2f);
					for (NodeContainerInterface p : graph
							.getAllNodeContainers()) {
						if (p.isVisible() && p.isSelected()) {
							Drawing.drawPackage(p);
						}
					}
					// Draws the frame of the nodecontainers
					glColor4f(0.3f, 0.3f, 0.3f, 0.2f);
					for (NodeContainerInterface p : graph
							.getAllNodeContainers()) {
						if (p.isVisible()) {
							// Drawing.drawPackageFrame(p);
						}
					}

					// Overpaints the area of the nodes with white
					glColor3f(1f, 1f, 1f);
					for (NodeInterface c : graph.getAllNodes()) {
						if (c.isVisible()) {
							Drawing.drawNodeArea(c);
						}
					}

					// Draws the area of the nodes with a particular tinge (4th
					// argument
					// of glColor4f)
					for (NodeInterface c : graph.getAllNodes()) {
						if (c.isVisible()) {
							glColor4f(0.f, 0.8f, 0.2f, c.getTinge());
							Drawing.drawNodeArea(c);
						}
					}

					// Draws the highlighted nodes
					glColor4f(1f, 0f, 0f, 1f);
					for (NodeInterface c : graph.getAllNodes()) {
						if (c.isVisible() && c.isHighlighted()) {
							Drawing.drawNodeArea(c);
						}
					}

					// Draws the selected nodes
					for (NodeInterface node : graph.getAllNodes()) {
						if (node.isSelected()) {
							glColor3f(0.0f, 0.70f, 1.f);
							Drawing.drawNodeArea(node);
						}
					}

					// Draws the frame of the nodes
					glColor3f(0.5f, 0.5f, 0.5f);
					for (NodeInterface c : graph.getAllNodes()) {
						if (c.isVisible()) {
							Drawing.drawNodeFrame(c);
						}
					}

					// Set the lineWidth for the edges
					glLineWidth(graph.getLineWidth());

					// Draws the unselected edges
					if (graph.getEdgeUnselectedColor() != null) {
						setGLColor4f(graph.getEdgeUnselectedColor());
						for (EdgeInterface edge : graph.getAllEdges()) {
							if (edge.isVisible() && !edge.isSelected()) {
								Drawing.drawEdge(edge, true);
							}
						}
					}

					// Draws the selected edges
					for (EdgeInterface edge : graph.getAllEdges()) {
						if (edge.isVisible() && edge.isSelected()) {
							Drawing.drawEdge(edge, false);
						}
					}

					// Draws the highlighted nodes
					glColor4f(1f, 0f, 0f, 1f);
					for (EdgeInterface edge : graph.getAllEdges()) {
						if (edge.isVisible() && edge.isSelected()
								&& edge.isHighlighted()) {
							Drawing.drawEdge(edge, false);
						}
					}

					if (print)
						canvasToImage(0, 0);

					// Display everything
					glcanvas.swapBuffers();

					// Show the fps in the infoText
					infoTextBottom.setText(graph.getInfoText() + " | "
							+ "FPS: " + (int) 1000
							/ (System.currentTimeMillis() - fTime));
					System.out.print((System.currentTimeMillis() - fTime)+"  ");
					infoTextBottom.pack();

				}
			}
		});
	}

	private void setGLColor4f(GLColor4f c) {
		glColor4f(c.getRed(), c.getGreen(), c.getBlue(), c.getTransparency());
	}

	/**
	 * Checks which elements are at the given position and executes a particular
	 * action like showing a tooltip.
	 * 
	 * @param ex
	 *            x-position of the mouse
	 * @param ey
	 *            y-position of the mouse
	 * @param mask
	 *            a bitmask with the information about the event
	 */
	private void checkSelection(int ex, int ey, int mask) {
		if (graph != null) {
			if (isTooglingContainer) {
				isTooglingContainer = false;
			} else {
				// Holds the text for the tooltip
				String tooltiptext = "";

				// Unselect all nodes and nodecontainers if needed
				if (mask == Constants.ONCLICK) {
					for (NodeInterface node : graph.getAllNodes()) {
						node.setSelected(false);
					}
					for (NodeContainerInterface nc : graph
							.getAllNodeContainers()) {
						nc.setSelected(false);
					}
				}

				// Deactivate Antialiasing for detection
				glDisable(GL_LINE_SMOOTH);

				// Set the linewidth to a user-defined value for detection
				glLineWidth(selectionLineWidth);

				// Get the the distance between the center and the position of
				// the mouse
				Rectangle bounds = glcanvas.getBounds();
				float fAspect = (float) bounds.width / (float) bounds.height;
				float hx = (zoom * 2) / bounds.height;
				float wx = ((zoom * 2) / (bounds.width)) * fAspect;
				float cx = -1 * (x - ((ex * wx) - (fAspect * zoom)));
				float cy = -1 * (y - (-((ey * hx) - zoom)));
				float r = (float) Math
						.sqrt((Math.pow(cx, 2) + Math.pow(cy, 2)));

				// Sets the color to a color that differs from white
				glColor3b((byte) 64, (byte) 0, (byte) 0);

				// check if position is in or out of the circle
				if (r > this.graph.getGraphRadius()) {
					// check the nodes
					glClear(GL_COLOR_BUFFER_BIT);
					ArrayList<NodeInterface> nodes = graph.getAllNodes();
					for (NodeInterface node:nodes) {
						if (node.isVisible())
							Drawing.drawNodeArea(node);
					}
					
					if (isInBuffer(ex, ey)) {
						nodeFound =null;
						findNode(nodes, ex, ey, 0, nodes.size()-1);
						if (nodeFound!=null) {
							if (mask == Constants.ONHOVER) {
								tooltiptext += nodeFound.getTooltip() + "\n";
							}
							if ((mask & Constants.ONCLICK) != 0) {
								nodeFound.setSelected(!nodeFound.isSelected());
								calcEdgeVisibility();
							}
							if ((mask & Constants.ONDOUBLECLICK) != 0) {
								nodeFound.onDoubleClick();
								calcEdgeVisibility();
							}
						}
					}
					// check the nodecontainers
					else {
						NodeContainerInterface hit = null;
						int i = graph.getHead().getMaxDepth() + 1;
						while (hit == null && i >= 0) {
							ArrayList<NodeContainerInterface> check = new ArrayList<NodeContainerInterface>();
							for (NodeContainerInterface nc : graph
									.getAllNodeContainers()) {
								if (nc.isVisible()) {
									if (nc.getDepth() == i) {
										check.add(nc);
									}
								}
							}
							hit = ncAddToVector(check, ex, ey);
							i--;
						}
						if (hit != null) {

							if (mask == Constants.ONHOVER) {
								tooltiptext += hit.getTooltip() + "\n";
							}
							if ((mask & Constants.ONCLICK) != 0) {
								hit.setSelected(!hit.isSelected());
								calcEdgeVisibility();
							}
							if ((mask & Constants.ONDOUBLECLICK) != 0) {
								calcEdgeVisibility();
								hit.toogleMinimize();
								recalc();
								isTooglingContainer = true;
							}
						}
					}
				}
				// Check the edges
				else {
					ArrayList<EdgeInterface> edgesCheck = new ArrayList<EdgeInterface>();
					// Only the edges which are shown need to be checked
					for (EdgeInterface edge : graph.getAllEdges()) {
						if (edge.isVisible() && edge.isSelected()) {
							edgesCheck.add(edge);
						}
					}
					// A new vector for the edges which are found
					edgesFound = new ArrayList<EdgeInterface>();
					findEdges(edgesCheck, ex, ey, 0, edgesCheck.size()-1);

					if (mask == Constants.ONCLICK) {
						for (EdgeInterface edge : graph.getAllEdges()) {
							if (edgesFound.size() == 0) {
								edge.setSelected(true);
							} else {
								edge.setSelected(false);
							}
						}
					}

					int co = 0;
					for (EdgeInterface edge : edgesFound) {
						if ((mask & Constants.ONCLICK) != 0) {
							edge.setSelected(true);
						}
						if (co < 15) {
							tooltiptext += edge.getTooltip() + "\n";
						}
						co++;
					}
					// limit the tooltip lines
					if (edgesFound.size() >= 15) {
						tooltiptext += "...";
					}

				}
				// on click or doubleclick the image has to be rendered
				if ((mask & (Constants.ONCLICK | Constants.ONDOUBLECLICK)) != 0) {
					render();
				}
				// on hover the tooltiptext has to be set
				if (mask == Constants.ONHOVER) {
					glcanvas.setToolTipText(tooltiptext);
				}
			}
		}
	}

	private void findNode(ArrayList<NodeInterface> nodes, int ex, int ey, int start, int end) {
		glClear(GL_COLOR_BUFFER_BIT);

		int middle=start+((end-start)/2);
		for (int i=start;i<=middle;i++) {
			if (nodes.get(i).isVisible())
				Drawing.drawNodeArea(nodes.get(i));
		}
		if (isInBuffer(ex, ey)) {
			if (start==end) {
				nodeFound=nodes.get(start);
			}else{
				findNode(nodes, ex, ey, start, middle);
				
			}
		}else{
			findNode(nodes, ex, ey, middle+1, end);
		}
	}

	private NodeContainerInterface ncAddToVector(
			ArrayList<NodeContainerInterface> nodes, int ex, int ey) {
		NodeContainerInterface hit = null;

		glClear(GL_COLOR_BUFFER_BIT);

		for (NodeContainerInterface nc : nodes) {
			Drawing.drawPackage(nc);
		}

		if (isInBuffer(ex, ey)) {
			if (nodes.size() == 1) {
				hit = nodes.get(0);
			}
			if (nodes.size() > 1) {
				ArrayList<NodeContainerInterface> firstHalf = new ArrayList<NodeContainerInterface>();
				ArrayList<NodeContainerInterface> secondHalf = new ArrayList<NodeContainerInterface>();

				for (int i = 0; i < nodes.size() / 2; i++) {
					firstHalf.add(nodes.get(i));
				}
				for (int i = nodes.size() / 2; i < nodes.size(); i++) {
					secondHalf.add(nodes.get(i));
				}
				NodeContainerInterface tmp;
				tmp = ncAddToVector(firstHalf, ex, ey);
				if (tmp != null) {
					return tmp;
				}
				tmp = ncAddToVector(secondHalf, ex, ey);
				if (tmp != null) {
					return tmp;
				}
			}
		}
		return hit;
	}

	private void findEdges(ArrayList<EdgeInterface> edges, int ex, int ey, int start, int end) {
		glClear(GL_COLOR_BUFFER_BIT);

		for (int i=start;i<=end;i++) {
			Drawing.drawEdge(edges.get(i), true);
		}

		if (isInBuffer(ex, ey)) {
			if (start==end) {
				edgesFound.add(edges.get(start));
			}else{
				findEdges(edges, ex, ey, start, start+((end-start)/2));
				findEdges(edges, ex, ey, start+((end-start)/2)+1, end);
			}
		}
	}

	/**
	 * Checks if the color at the given position has change
	 * 
	 * @param ex
	 *            x-Position
	 * @param ey
	 *            y-Position
	 * @return true if color has changed else false
	 */
	private boolean isInBuffer(int ex, int ey) {
		glReadPixels(ex, glcanvas.getBounds().height - ey, 1, 1, GL_RED,
				GL_BYTE, buffer);
		return buffer.get(0) != whiteByte;
	}

	public void calcEdgeVisibility() {
		ArrayList<NodeInterface> selected = new ArrayList<NodeInterface>();
		for (NodeContainerInterface nc : this.graph.getAllNodeContainers()) {
			if (nc.isSelected()) {
				for (NodeInterface node : nc.getAllNodes()) {
					selected.add(node);
				}
			}
		}
		for (NodeInterface node : this.graph.getAllNodes()) {
			if (node.isSelected()) {
				selected.add(node);
			}
		}

		if (selected.isEmpty()) {
			for (EdgeInterface edge : graph.getAllEdges()) {
				edge.setSelected(true);
			}
		} else {
			for (EdgeInterface edge : graph.getAllEdges()) {
				edge.setSelected(false);
				for (NodeInterface node : selected) {
					if (edge.getFirstNode().equals(node)
							|| edge.getSecondNode().equals(node)) {
						edge.setSelected(true);
					}
				}
			}
		}
	}

	public void recalc() {
		if (this.graph != null) {
			this.graph.calc();
		}
	}

	public GLCanvas getGLCanvas() {
		return glcanvas;
	}

	public void setGraph(GraphInterface graph) {
		this.graph = graph;
		recalc();
		render();
	}

	private BufferedImage canvasToImage(int x, int y) {
		int pWidth = glcanvas.getBounds().width;
		int pHeight = glcanvas.getBounds().height;

		FloatBuffer vPixelBuffer = BufferUtils.createFloatBuffer(pWidth
				* pHeight * 4);

		// First read the frame buffer
		glReadPixels(x, y, pWidth, pHeight, GL_RGB, GL_FLOAT, vPixelBuffer);

		BufferedImage vBufferedImage = new BufferedImage(pWidth, pHeight,
				BufferedImage.TYPE_INT_RGB);

		// Copy pixel data into buffered image
		for (int vRow = 0; vRow < pHeight; vRow++) {
			for (int vColumn = 0; vColumn < pWidth; vColumn++) {
				// Get separate color components
				int vIndex = ((vRow * pWidth) + vColumn) * 3;
				int r = (int) (vPixelBuffer.get(vIndex) * 255);
				int g = (int) (vPixelBuffer.get(vIndex + 1) * 255);
				int b = (int) (vPixelBuffer.get(vIndex + 2) * 255);

				/*
				 * Set rgb color by shifting components into corresponding
				 * integer bits.
				 */
				int vRgb = (r << 16) + (g << 8) + b;

				// Set buffer image pixel -- flip y coordinate
				vBufferedImage.setRGB(vColumn, pHeight - vRow - 1, vRgb);
			}
		}

		FileDialog fd = new FileDialog(this.glcanvas.getShell(), SWT.SAVE);

		String fileName = fd.open();
		String ext = "png";
		File file = new File(fileName + "." + ext);
		try {
			ImageIO.write(vBufferedImage, ext, file); // ignore returned boolean
		} catch (IOException e) {

		}
		return vBufferedImage;
	}
	
	KeyListener regularExpressionListener = new KeyListener() {
		public void keyReleased(KeyEvent e) {
			for (ElementInterface elem : graph.getAllElements()) {
				elem.setHighlighted(false);
				if (Pattern.matches(filterText.getText(),
						elem.getSearchString())) {
					elem.setHighlighted(true);
				}
			}
			calcEdgeVisibility();
			render();
		}

		public void keyPressed(KeyEvent e) {

		}
	};

//	public void PerformanceTest() {
//		int max = 1000;
//		int rounds = 10;
//		long maxTime = 0, time = 0;
//
//		int x[] = new int[max];
//		int y[] = new int[max];
//		System.out.println("\n Testsuite started...");
//		// Init position-arrays
//		for (int i = 0; i < max; i++) {
//			x[i] = (int) (Math.random() * glcanvas.getBounds().height);
//			y[i] = (int) (Math.random() * glcanvas.getBounds().width);
//		}
//
//		for (int j = 1; j <= rounds; j++) {
//			setGraph(Test.getTestgraph(200 * j, 20 * j, 0 * j));
//			long completeTime = System.currentTimeMillis();
//			long dTime = System.currentTimeMillis();
//			for (int i = 0; i < max; i++) {
//				dTime = System.currentTimeMillis();
//				checkSelection(x[i], y[i], Constants.ONHOVER);
//				time = System.currentTimeMillis() - dTime;
//				if (time > maxTime) {
//					maxTime = time;
//				}
//			}
//			System.out.print("Time:\t" + (System.currentTimeMillis() - completeTime));
//			System.out.println("\tMaxTime(" + j + "):\t" + maxTime);
//
//			System.out.println();
//			maxTime = 0;
//		}
//	}
//	
//	public void PerformanceRenderTest(){
//		tUnit++;
//		System.out.println("\n"+tUnit);
//		setGraph(Test.getTestgraph(200 * 10 * tUnit , 20 * 10 * tUnit, 300 * 10 * tUnit));	
//		for (int i = 0; i < 10; i++) {
//			render();
//		}
//	}
}
