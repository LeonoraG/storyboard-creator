package eu.scasefp7.eclipse.storyboards.diagram.part;

/**
 * @generated NOT
 */
public class ServiceCompositionStoryboardsExportWizard extends StoryboardsExportWizard {

	public void addPages() {
		diagramModelFilePage = new StoryboardsExportWizardPage(workbench, selection, "scd"); //$NON-NLS-1$ //$NON-NLS-2$
		diagramModelFilePage.setTitle(Messages.StoryboardsExportWizard_DiagramModelFilePageTitle);
		diagramModelFilePage.setDescription(Messages.StoryboardsExportWizard_DiagramModelFilePageDescription);
		addPage(diagramModelFilePage);
	}

}
