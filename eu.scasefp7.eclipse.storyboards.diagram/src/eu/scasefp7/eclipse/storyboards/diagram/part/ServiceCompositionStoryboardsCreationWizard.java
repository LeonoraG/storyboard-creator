package eu.scasefp7.eclipse.storyboards.diagram.part;

/**
 * @generated NOT
 */
public class ServiceCompositionStoryboardsCreationWizard extends StoryboardsCreationWizard {

	public void addPages() {
		diagramModelFilePage = new StoryboardsCreationWizardPage("DiagramModelFile", getSelection(), "scd"); //$NON-NLS-1$ //$NON-NLS-2$
		diagramModelFilePage.setTitle(Messages.StoryboardsCreationWizard_DiagramModelFilePageTitle);
		diagramModelFilePage.setDescription(Messages.StoryboardsCreationWizard_DiagramModelFilePageDescription);
		addPage(diagramModelFilePage);
	}

}
