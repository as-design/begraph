package de.begraph.model.abstracts;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import de.begraph.model.interfaces.NodeContainerInterface;
import de.begraph.model.interfaces.NodeInterface;


public abstract class AbstractNodeContainer implements NodeContainerInterface {
	private String name;
	ArrayList<NodeInterface> children = new ArrayList<NodeInterface>();
	ArrayList<NodeContainerInterface> subNodeContainer = new ArrayList<NodeContainerInterface>();
	private int position;
	private int size;
	private int depth;
	private NodeContainerInterface parent;
	int sumClasses;
	boolean visible, selected, minimized, highlighted;
	private int gap = 1;
	private FloatBuffer positionBuffer;
	private FloatBuffer positionBufferFrame;

	public AbstractNodeContainer(String name, NodeContainerInterface parent) {
		this.name = name;
		this.parent = parent;
		setVisible(true);
		setSelected(false);
		if (parent != null)
			parent.addNodeContainer(this);
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	public void setSize(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isSelected() {
		return this.selected;
	}
	
	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	public boolean isHighlighted() {
		return this.highlighted;
	}

	public FloatBuffer getPositionBuffer() {
		return this.positionBuffer;
	}

	public final void setPositionBuffer(FloatBuffer positionBuffer) {
		this.positionBuffer = positionBuffer;
	}

	public FloatBuffer getPositionBufferFrame() {
		return this.positionBufferFrame;
	}

	public final void setPositionBufferFrame(FloatBuffer positionBuffer) {
		this.positionBufferFrame = positionBuffer;
	}

	public void addNode(NodeInterface node) {
		children.add(node);
	}

	public void addNodeContainer(NodeContainerInterface nodecontainer) {
		subNodeContainer.add(nodecontainer);
	}
	public int sumNodes(int position, int depth) {
		depth++;
		this.setDepth(depth);
		this.setPosition(position);
		if (visible) {
			if (minimized) {
				this.size = 3;
				for (NodeContainerInterface nc : subNodeContainer) {
					nc.sumNodes(position, depth);
				}
				for (NodeInterface n : children) {
					n.setParent(this);
					n.setPosition(position + 1);
				}
			} else {
				int sum = 0;
				for (NodeContainerInterface nc : subNodeContainer) {
					sum += gap + nc.sumNodes(position + sum + gap, depth);
				}
				int i = 0;

				for (NodeInterface n : children) {
					n.setParent(this);
					n.setPosition(position + sum + gap + i);
					i++;
				}
				if (children.isEmpty()) {
					this.size = children.size() + sum + gap;
				} else {
					this.size = children.size() + sum + (gap * 2);
				}
			}
		} else {
			this.size = 0;
			for (NodeContainerInterface nc : subNodeContainer) {
				nc.sumNodes(position, depth);
			}
			for (NodeInterface n : children) {
				n.setParent(this);
				n.setPosition(position);
			}
		}
		return (this.size);
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public int getDepth() {
		return depth;
	}
	
	final public int getMaxDepth() {
		if (subNodeContainer.size() == 0) {
			return 0;
		} else {
			int max = 0;
			for (NodeContainerInterface nc : subNodeContainer) {
				int s = nc.getMaxDepth();
				if (s > max) {
					max = s;
				}
			}
			return 1 + max;
		}
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPosition() {
		return position;
	}

	public NodeContainerInterface getParent() {
		// TODO Auto-generated method stub
		return this.parent;
	}

	public void setParent(NodeContainerInterface parent) {
		this.parent = parent;
	}

	public void toogleMinimize() {
		if (this.getParent() != null) {
			for (NodeContainerInterface nc : subNodeContainer) {
				nc.setVisible(minimized);
			}
			for (NodeInterface n : children) {
				n.setVisible(minimized);
			}
			this.minimized = !this.minimized;
		}
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
		if (!(visible && this.minimized)) {
			for (NodeContainerInterface nc : subNodeContainer) {
				nc.setVisible(visible);
			}
			for (NodeInterface n : children) {
				n.setVisible(visible);
			}
		}
	}

	public boolean isVisible() {
		return this.visible;
	}

	public ArrayList<NodeInterface> getAllNodes() {
		ArrayList<NodeInterface> allNodes = new ArrayList<NodeInterface>();
		for (NodeInterface c : children) {
			allNodes.add(c);
		}

		for (NodeContainerInterface p : subNodeContainer) {
			allNodes.addAll(p.getAllNodes());
		}
		return allNodes;
	}

	public ArrayList<NodeContainerInterface> getAllNodeContainers() {
		ArrayList<NodeContainerInterface> allNodeContainers = new ArrayList<NodeContainerInterface>();
		for (NodeContainerInterface p : subNodeContainer) {
			allNodeContainers.addAll(p.getAllNodeContainers());
		}
		allNodeContainers.addAll(subNodeContainer);
		return allNodeContainers;
	}

}
