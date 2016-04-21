package eu.scasefp7.eclipse.storyboards.diagram.part;

import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * @generated NOT
 */
public class StoryboardsExportWizard extends Wizard implements IExportWizard {

	protected IWorkbench workbench;

	protected IStructuredSelection selection;

	protected StoryboardsExportWizardPage diagramModelFilePage;

	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		this.workbench = workbench;
		this.selection = currentSelection;
		setWindowTitle(Messages.StoryboardsExportWizardTitle);
		setDefaultPageImageDescriptor(StoryboardsDiagramEditorPlugin
				.getBundledImageDescriptor("icons/wizban/NewStoryboardsWizard.gif")); //$NON-NLS-1$
		setNeedsProgressMonitor(true);
	}

	public void addPages() {
		diagramModelFilePage = new StoryboardsExportWizardPage(workbench, selection, "sbd"); //$NON-NLS-1$ //$NON-NLS-2$
		diagramModelFilePage.setTitle(Messages.StoryboardsExportWizard_DiagramModelFilePageTitle);
		diagramModelFilePage.setDescription(Messages.StoryboardsExportWizard_DiagramModelFilePageDescription);
		addPage(diagramModelFilePage);
	}

	public boolean performFinish() {
		return diagramModelFilePage.finish();
	}

}
