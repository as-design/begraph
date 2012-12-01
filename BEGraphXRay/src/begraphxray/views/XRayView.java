package begraphxray.views;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;


import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.part.ResourceTransfer;
import org.malnatij.svplugin.model.ClassRepresentation;
import org.malnatij.svplugin.model.PackageRepresentation;
import org.malnatij.svplugin.model.ProjectRepresentation;
import org.malnatij.svplugin.model.core.Model;
import org.malnatij.svplugin.model.core.ModelNotPreviouslyScheduled;

import model.implementation.XRayClass;
import model.implementation.XRayEdge;
import model.implementation.XRayEdgeType;
import model.implementation.XRayGraph;
import model.implementation.XRayPackage;
import de.begraph.model.GLColor3f;
import de.begraph.model.GLColor4f;
import de.begraph.model.interfaces.EdgeInterface;
import de.begraph.model.interfaces.EdgeTypeInterface;
import de.begraph.model.interfaces.GraphInterface;
import de.begraph.model.interfaces.NodeContainerInterface;
import de.begraph.model.interfaces.NodeInterface;
import de.begraph.test.Test;
import de.begraph.view.Begraph;



/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class XRayView extends ViewPart {
	private GraphInterface testgraph;
	
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "testview.views.SampleView";


	/**
	 * The constructor.
	 */
	public XRayView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {

		// For testing use Test.getTestgraph()
		//final Begraph begraph = new Begraph(parent, Test.getTestgraph());
		final Begraph begraph = new Begraph(parent, Test.getTestgraph(2000, 200, 3000));
		
		Transfer[] types = new Transfer[] { org.eclipse.ui.part.ResourceTransfer.getInstance() };
		
		int operations = DND.DROP_MOVE;
		DropTarget target = new DropTarget(begraph.getGLCanvas(), operations);
		target.setTransfer(types);
				
		target.addDropListener(new DropTargetListener() {
			
			@Override
			public void dropAccept(DropTargetEvent event) {
				
			}
			
			@Override
			public void drop(DropTargetEvent event) {
				if(ResourceTransfer.getInstance().isSupportedType(event.currentDataType)){
					if(event.data instanceof IResource[]){
						final IResource[] data = (IResource[]) event.data;
						
						if(data.length==1){
							if(data[0] instanceof IFolder){
								IFolder ifolder = (IFolder) data[0];
								String path = ifolder.getProjectRelativePath().toString().replace("/", ".");
								
								for(NodeInterface node:testgraph.getAllNodes()){
									node.setSelected(false);
								}
								
								for(NodeContainerInterface nc:testgraph.getAllNodeContainers()){
									nc.setSelected(false);
									if(path.endsWith(nc.getName())){
										nc.setSelected(true);
									}
								}
								
								begraph.calcEdgeVisibility();
								begraph.render();
							}
							if(data[0] instanceof IFile){
								for(NodeContainerInterface nc:testgraph.getAllNodeContainers()){
									nc.setSelected(false);
								}
								
								for(NodeInterface node:testgraph.getAllNodes()){
									node.setSelected(false);
									if(((XRayClass)node).getRelpath().compareTo(data[0].getFullPath().toOSString())==0){
										node.setSelected(true);
									}
								}
								begraph.calcEdgeVisibility();
								begraph.render();
							}
							if(data[0] instanceof IProject){
								final Job job = new Job("Creating the graph...") {
									@Override
									protected IStatus run(IProgressMonitor monitor) {
										IProject project =(IProject) data[0];
										testgraph = new XRayGraph("Project: "+project.getName(), project);
		
										Model me = new Model(project, "");
										
										me.schedule();
										try {
											me.join();
										} catch (InterruptedException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
										ProjectRepresentation mproject=null;
										try {
											mproject = me.getModeledProject();
										} catch (ModelNotPreviouslyScheduled e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										
										monitor.beginTask("Building the graph", 100);
										Vector <NodeContainerInterface> packages = new Vector<NodeContainerInterface>();
										Vector <NodeInterface> classes = new Vector<NodeInterface>();
										for(PackageRepresentation p :mproject.getPackages()){
											NodeContainerInterface pt = new XRayPackage(p.getName(), testgraph.getHead());
											packages.add(pt);
										}
										
										HashMap<String,NodeContainerInterface> packageHash = new HashMap<String,NodeContainerInterface>();
										for (NodeContainerInterface p: packages){
											packageHash.put(p.getName(),p);
										}

										int maxLOC = mproject.getMaxLOC();
										int maxMeth = mproject.getMaxNOM();
										
										for(ClassRepresentation c: mproject.getAllClasses()){
											IFile f = null;
											String path = c.getRelpath();
											int index = path.indexOf(project.getName());
											
											if(index>0){
												path = path.substring(index+project.getName().length()+1, path.length());
												f = project.getFile(path);
											}
											NodeInterface node = new XRayClass(c.getName(), c.getRelpath(), f,c.getLinesOfCode(),c.getNumberOfMethods(), (float)c.getLinesOfCode()/maxLOC, (float) c.getNumberOfMethods()/maxMeth);
											NodeContainerInterface p = packageHash.get(c.getPackageContainer().getName());
											
											if(p != null){
												p.addNode(node);
												classes.add(node);
											}
										}
										
										monitor.worked(20);
										
										HashMap<String,NodeInterface> classHash = new HashMap<String,NodeInterface>();
										for (NodeInterface c: classes){
											classHash.put(((XRayClass)c).getRelpath(),c);
										}
										
										monitor.worked(20);
										
										EdgeTypeInterface type = new XRayEdgeType("Type1", new GLColor4f(1.f,(float)215/256,0.f,0.3f), new GLColor3f(0.0f,0.0f,1.f));
										
										for(ClassRepresentation c: mproject.getAllClasses()){
											Set<ClassRepresentation> keySet = c.getUsedClasses().keySet();
											Iterator<ClassRepresentation> classIterator = keySet.iterator();
											
											while(classIterator.hasNext()){
												ClassRepresentation currentClass = classIterator.next();
												testgraph.addEdge(new XRayEdge(classHash.get(c.getRelpath()), classHash.get(currentClass.getRelpath()), type));
											}
										}
										
										monitor.worked(20);
										XRayClass ctmp;
										for(EdgeInterface c:testgraph.getAllEdges()){
											ctmp = (XRayClass) c.getFirstNode();
											ctmp.setNEdges(ctmp.getNEdges()+1);
											ctmp = (XRayClass) c.getSecondNode();
											ctmp.setNEdges(ctmp.getNEdges()+1);
										}
										
										monitor.worked(20);
										
										int maxEdges=0;
										for(NodeInterface node:testgraph.getAllNodes()){
											ctmp = (XRayClass) node;
											if(ctmp.getNEdges()>maxEdges)
												maxEdges = ctmp.getNEdges();
										}
										
										monitor.worked(20);
										
										testgraph.setInfoText("PROJECT: "+mproject.getName()+" (Packages: "+mproject.getPackages().size()+" Classes: "+mproject.getNumberOfClasses()+") | METRICS: Length->LOC ("+maxLOC+") Width->Number of Methods ("+maxMeth+") Tinge->Number of Dependencies ("+maxEdges+")");

										for(NodeInterface node:testgraph.getAllNodes()){
												node.setTinge((float)((XRayClass)node).getNEdges()/maxEdges);
										}
										System.out.println(testgraph.getAllEdges().size());
										monitor.done();
										
										begraph.setGraph(testgraph);
										return Status.OK_STATUS;
										}
									};
								job.schedule();
							}
						}
					}
				}
				event.detail = DND.DROP_NONE;
			}
			
			@Override
			public void dragOver(DropTargetEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void dragOperationChanged(DropTargetEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void dragLeave(DropTargetEvent event) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void dragEnter(DropTargetEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	public void setFocus() {
	}
}