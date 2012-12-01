package model.implementation;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.ResourceUtil;

import de.begraph.model.abstracts.AbstractNode;


public class XRayClass extends AbstractNode{

	private String relpath;
	private IFile file;
	private int loc, nom, nEdges;
	
	public XRayClass(String name, String relpath, IFile file, int loc, int nom, float length, float width){
		super(length, width, 0.5f);
		setName(name);
		this.relpath = relpath;
		this.file = file;
		this.loc = loc;
		this.nom = nom;
		this.nEdges = 0;
	}

	public String getTooltip() {
		String tooltip = "Name: "+ getName()+"\n";
		tooltip += "Path: "+ getRelpath()+"\n";
		tooltip += "Lines of code: "+ loc+"\n";
		tooltip += "Number of Methods: "+ nom+"\n";
		tooltip += "Number of Dependencies: "+ nEdges+"\n";
		return tooltip;
	}

	public void onDoubleClick() {
		if(this.file!=null)
			openFileInEditor(this.file);
	}
	
	public void openFileInEditor(IFile file){
		IWorkbenchPage activePage =
			PlatformUI.getWorkbench().
			getActiveWorkbenchWindow().getActivePage();
		if(file!=null){
			try {
				IDE.openEditor(activePage, file , true);
			} catch (PartInitException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public String getRelpath(){
		return relpath;
	}
	
	public int getNEdges(){
		return this.nEdges;
	}
	
	public void setNEdges(int edges){
		this.nEdges = edges;
	}

	@Override
	public String getSearchString() {
		String search = "";
		search += this.getName()+" ";
		search += "loc="+this.loc;
		return search;
	}
	
}
