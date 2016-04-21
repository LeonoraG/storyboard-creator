package eu.scasefp7.eclipse.storyboards.diagram.part;

/**
 * @generated NOT
 */
public class ServiceCompositionStoryboardsImportWizard extends StoryboardsImportWizard {

	public void addPages() {
		diagramModelFilePage = new StoryboardsImportWizardPage(workbench, selection, "scd"); //$NON-NLS-1$ //$NON-NLS-2$
		diagramModelFilePage.setTitle(Messages.StoryboardsImportWizard_DiagramModelFilePageTitle);
		diagramModelFilePage.setDescription(Messages.StoryboardsImportWizard_DiagramModelFilePageDescription);
		addPage(diagramModelFilePage);
	}

}
