package begraphfilesystem.views;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.part.*;
import org.eclipse.core.resources.IFile;
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
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.part.ResourceTransfer;
import org.malnatij.svplugin.model.ClassRepresentation;
import org.malnatij.svplugin.model.PackageRepresentation;
import org.malnatij.svplugin.model.ProjectRepresentation;
import org.malnatij.svplugin.model.core.Model;
import org.malnatij.svplugin.model.core.ModelNotPreviouslyScheduled;

import model.implementation.FSFile;
import model.implementation.FSLink;
import model.implementation.FSGraph;
import model.implementation.FSFolder;
import de.begraph.model.interfaces.EdgeInterface;
import de.begraph.model.interfaces.GraphInterface;
import de.begraph.model.interfaces.NodeContainerInterface;
import de.begraph.model.interfaces.NodeInterface;
import de.begraph.test.Test;
import de.begraph.view.Begraph;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class FileSystemView extends ViewPart {
	private GraphInterface testgraph;

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "testview.views.SampleView";

	/**
	 * The constructor.
	 */
	public FileSystemView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {

		// For testing use Test.getTestgraph()
		// final Begraph begraph = new Begraph(parent, Test.getTestgraph());
		final Begraph begraph = new Begraph(parent, null);

		int operations = DND.DROP_MOVE;
		DropTarget target = new DropTarget(begraph.getGLCanvas(), operations);
		target.setTransfer( new Transfer[] { FileTransfer.getInstance() });

		target.addDropListener(new DropTargetListener() {

			@Override
			public void dropAccept(DropTargetEvent event) {

			}

			@Override
			public void drop(DropTargetEvent event) {
				System.out.println(event);
				if (FileTransfer.getInstance().isSupportedType(
						event.currentDataType)) {
					
					if (event.data instanceof String[]) {
						String[] files = (String[]) event.data;
						if(files.length==1){
							final String filename = files[0];
							
							final Job job = new Job("Creating the graph...") {
								@Override
								protected IStatus run(IProgressMonitor monitor) {

									monitor.beginTask("Building the graph", 100);

									File f = new File(filename);
									testgraph = new FSGraph(f.getName());
									
									testgraph.getHead().setVisible(true);
									
									if (f.isDirectory()) {
										for (File file : f.listFiles()) {
											iterateFolder(file, testgraph.getHead());
										}
									}

									long max = 0;
									long minmod = 0, maxmod = 0;
									for (NodeInterface node : testgraph.getAllNodes()) {
										if (((FSFile) node).getFilesize() > max) {
											max = ((FSFile) node).getFilesize();
										}
										if (minmod == 0
												|| minmod > ((FSFile) node).getLastmodified()) {
											minmod = ((FSFile) node).getLastmodified();
										}
										if (maxmod < ((FSFile) node).getLastmodified()) {
											maxmod = ((FSFile) node).getLastmodified();
										}
									}

									System.out.println(testgraph.getAllNodes().size());

									System.out.println(testgraph.getAllNodeContainers().size());

									for (NodeInterface node : testgraph.getAllNodes()) {
										node.setLength((float) ((FSFile) node).getFilesize() / max);
										node.setTinge((float) (((FSFile) node).getLastmodified() - minmod)
												/ (maxmod - minmod));
									}

									for(NodeContainerInterface nc: testgraph.getAllNodeContainers()){
										nc.toogleMinimize();
									}
									
									monitor.worked(100);

									begraph.setGraph(testgraph);

									monitor.done();

									return Status.OK_STATUS;
								}
							};
							job.schedule();
							
						}

						event.detail = DND.DROP_NONE;
					}
				}
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

	public void iterateFolder(File f, NodeContainerInterface parent) {
		if (f.isFile()) {
			parent.addNode(new FSFile(f.getName(), f.getAbsolutePath(), f
					.length(), f.lastModified()));
		}

		if (f.isDirectory()) {
			FSFolder folder = new FSFolder(f.getName(), parent);

			for (File file : f.listFiles()) {
				iterateFolder(file, folder);
			}
		}
	}
}