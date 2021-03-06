package eu.scasefp7.eclipse.storyboards.diagram.providers;

import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.View;

import eu.scasefp7.eclipse.storyboards.diagram.edit.parts.StoryboardDiagramEditPart;
import eu.scasefp7.eclipse.storyboards.diagram.part.StoryboardsDiagramEditorPlugin;
import eu.scasefp7.eclipse.storyboards.diagram.part.StoryboardsVisualIDRegistry;

/**
 * @generated
 */
public class StoryboardsValidationProvider {

	/**
	 * @generated
	 */
	private static boolean constraintsActive = false;

	/**
	 * @generated
	 */
	public static boolean shouldConstraintsBePrivate() {
		return false;
	}

	/**
	 * @generated
	 */
	public static void runWithConstraints(TransactionalEditingDomain editingDomain, Runnable operation) {
		final Runnable op = operation;
		Runnable task = new Runnable() {
			public void run() {
				try {
					constraintsActive = true;
					op.run();
				} finally {
					constraintsActive = false;
				}
			}
		};
		if (editingDomain != null) {
			try {
				editingDomain.runExclusive(task);
			} catch (Exception e) {
				StoryboardsDiagramEditorPlugin.getInstance().logError("Validation failed", e); //$NON-NLS-1$
			}
		} else {
			task.run();
		}
	}

	/**
	 * @generated
	 */
	static boolean isInDefaultEditorContext(Object object) {
		if (shouldConstraintsBePrivate() && !constraintsActive) {
			return false;
		}
		if (object instanceof View) {
			return constraintsActive
					&& StoryboardDiagramEditPart.MODEL_ID.equals(StoryboardsVisualIDRegistry.getModelID((View) object));
		}
		return true;
	}

}
