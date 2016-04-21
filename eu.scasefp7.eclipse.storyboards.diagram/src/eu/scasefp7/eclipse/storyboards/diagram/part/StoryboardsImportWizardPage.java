package eu.scasefp7.eclipse.storyboards.diagram.part;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.FileSystemElement;
import org.eclipse.ui.internal.wizards.datatransfer.WizardFileSystemResourceImportPage1;
import org.eclipse.ui.part.FileEditorInput;

/**
 * @generated NOT
 */
@SuppressWarnings({ "restriction", "rawtypes" })
public class StoryboardsImportWizardPage extends WizardFileSystemResourceImportPage1 {

	private final String fileExtension;

	public StoryboardsImportWizardPage(IWorkbench workbench, IStructuredSelection selection, String fileImportMask) {
		super(workbench, selection);
		fileExtension = fileImportMask;
		if (selection != null && selection.isEmpty() == false && selection instanceof IStructuredSelection) {
			IProject project = ProjectLocator.getProjectOfSelectionList((IStructuredSelection) selection);
			String requirementsFolderLocation = null;
			try {
				requirementsFolderLocation = project.getPersistentProperty(new QualifiedName("",
						"eu.scasefp7.eclipse.core.ui.rqsFolder"));
			} catch (CoreException e) {
				StoryboardsDiagramEditorPlugin.log("Error retrieving project property (requirements folder location)",
						e);
			}
			String compositionsFolderLocation = requirementsFolderLocation;
			try {
				compositionsFolderLocation = project.getPersistentProperty(new QualifiedName("",
						"eu.scasefp7.eclipse.core.ui.compFolder"));
			} catch (CoreException e) {
				StoryboardsDiagramEditorPlugin.log("Error retrieving project property (compositions folder location)",
						e);
			}
			IContainer container = project;
			if (fileExtension.equals("scd") && compositionsFolderLocation != null) {
				if (project.findMember(new Path(compositionsFolderLocation)).exists())
					container = (IContainer) project.findMember(new Path(compositionsFolderLocation));
			} else if (fileExtension.equals("sbd") && requirementsFolderLocation != null) {
				if (project.findMember(new Path(requirementsFolderLocation)).exists())
					container = (IContainer) project.findMember(new Path(requirementsFolderLocation));
			}
			setContainerFieldValue(container.getFullPath().toString());
		}
	}

	@Override
	public boolean finish() {
		if (!ensureSourceIsValid()) {
			return false;
		}

		saveWidgetValues();

		Iterator resourcesEnum = getSelectedResources().iterator();
		ArrayList<File> fileSystemObjects = new ArrayList<File>();
		while (resourcesEnum.hasNext()) {
			File fileSystemObject = (File) ((FileSystemElement) resourcesEnum.next()).getFileSystemObject();
			String filename = fileSystemObject.getName();
			int i = filename.lastIndexOf('.');
			if (i <= 0 || !filename.substring(i + 1).equals(fileExtension)) {
				setErrorMessage("All files imported must have the " + fileExtension + " extension!");
				return false;
			}
			fileSystemObjects.add(fileSystemObject);
		}
		for (final File file : fileSystemObjects) {

			IRunnableWithProgress op = new WorkspaceModifyOperation(null) {

				protected void execute(IProgressMonitor monitor) throws CoreException, InterruptedException {

					String fileName = file.getName();

					ArrayList<String> datalines = new ArrayList<String>();
					BufferedReader brlocal;
					try {
						brlocal = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
						String line;
						while ((line = brlocal.readLine()) != null) {
							datalines.add(line);
						}
						brlocal.close();
					} catch (FileNotFoundException e) {
						StoryboardsDiagramEditorPlugin.log("Error reading storyboard diagram from file system", e);
					} catch (IOException e) {
						StoryboardsDiagramEditorPlugin.log("Error reading storyboard diagram from file system", e);
					}
					String filedata = "";
					for (String dataline : datalines) {
						filedata += dataline + "\n";
					}
					InputStream stream = new ByteArrayInputStream(filedata.getBytes(StandardCharsets.UTF_8));
					IPath resourcePath = getResourcePath();
					IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
					IContainer container = (IContainer) root.findMember(resourcePath);
					container.getFile(new Path(fileName)).create(stream, true, monitor);

					IEditorDescriptor desc = PlatformUI.getWorkbench().getEditorRegistry()
							.getDefaultEditor(file.getName());
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					page.openEditor(new FileEditorInput(container.getFile(new Path(fileName))), desc.getId());
				}
			};
			try {
				getContainer().run(false, true, op);
			} catch (InterruptedException e) {
				StoryboardsDiagramEditorPlugin.log("Error importing storyboard diagram", e);
				return false;
			} catch (InvocationTargetException e) {
				StoryboardsDiagramEditorPlugin.log("Error importing storyboard diagram", e);
				if (e.getTargetException() instanceof CoreException) {
					ErrorDialog.openError(getContainer().getShell(), "Error in file creation", null,
							((CoreException) e.getTargetException()).getStatus());
					System.out.println("Error in file creation");
				} else {
					System.out.println("Error creating file");
				}
				return false;
			}
		}
		return true;
	}

	@Override
	protected void createOptionsGroup(Composite parent) {

	}

	@Override
	protected void createSourceGroup(Composite parent) {
		createRootDirectoryGroup(parent);
		createFileSelectionGroup(parent);
		Composite buttonComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		layout.makeColumnsEqualWidth = true;
		buttonComposite.setLayout(layout);
		buttonComposite.setFont(parent.getFont());
		GridData buttonData = new GridData(SWT.FILL, SWT.FILL, true, false);
		buttonComposite.setLayoutData(buttonData);
	}

	@Override
	protected void enableButtonGroup(boolean enable) {

	}

}
