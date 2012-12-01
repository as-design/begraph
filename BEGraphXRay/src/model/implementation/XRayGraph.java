package model.implementation;

import org.eclipse.core.resources.IProject;

import de.begraph.model.abstracts.AbstractGraph;
import model.implementation.XRayPackage;


public class XRayGraph extends AbstractGraph {
	 private IProject project;
	 private boolean loaded;
	
	public XRayGraph(String name, IProject project){
		super(new XRayPackage(name, null));
		this.project = project;
		loaded = true;
	}
	
	public IProject getProject(){
		return this.project;
	}
	
	public boolean isLoaded(){
		return this.loaded;
	}
	
	public void setLoaded(boolean loaded){
		this.loaded = loaded;
	}
}
