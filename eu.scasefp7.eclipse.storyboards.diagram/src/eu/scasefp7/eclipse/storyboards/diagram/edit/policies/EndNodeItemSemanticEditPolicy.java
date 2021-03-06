package eu.scasefp7.eclipse.storyboards.diagram.edit.policies;

import java.util.Iterator;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyReferenceCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.CreateRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyReferenceRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientReferenceRelationshipRequest;
import org.eclipse.gmf.runtime.emf.type.core.requests.ReorientRelationshipRequest;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.View;

import eu.scasefp7.eclipse.storyboards.diagram.edit.commands.ActionNodeNextNodeCreateCommand;
import eu.scasefp7.eclipse.storyboards.diagram.edit.commands.ActionNodeNextNodeReorientCommand;
import eu.scasefp7.eclipse.storyboards.diagram.edit.commands.ConditionPathCreateCommand;
import eu.scasefp7.eclipse.storyboards.diagram.edit.commands.ConditionPathReorientCommand;
import eu.scasefp7.eclipse.storyboards.diagram.edit.commands.StartNodeFirstNodeCreateCommand;
import eu.scasefp7.eclipse.storyboards.diagram.edit.commands.StartNodeFirstNodeReorientCommand;
import eu.scasefp7.eclipse.storyboards.diagram.edit.parts.ActionNodeNextNodeEditPart;
import eu.scasefp7.eclipse.storyboards.diagram.edit.parts.ConditionPathEditPart;
import eu.scasefp7.eclipse.storyboards.diagram.edit.parts.StartNodeFirstNodeEditPart;
import eu.scasefp7.eclipse.storyboards.diagram.part.StoryboardsVisualIDRegistry;
import eu.scasefp7.eclipse.storyboards.diagram.providers.StoryboardsElementTypes;

/**
 * @generated
 */
public class EndNodeItemSemanticEditPolicy extends StoryboardsBaseItemSemanticEditPolicy {

	/**
	 * @generated
	 */
	public EndNodeItemSemanticEditPolicy() {
		super(StoryboardsElementTypes.EndNode_2009);
	}

	/**
	 * @generated
	 */
	protected Command getDestroyElementCommand(DestroyElementRequest req) {
		View view = (View) getHost().getModel();
		CompositeTransactionalCommand cmd = new CompositeTransactionalCommand(getEditingDomain(), null);
		cmd.setTransactionNestingEnabled(false);
		for (Iterator<?> it = view.getTargetEdges().iterator(); it.hasNext();) {
			Edge incomingLink = (Edge) it.next();
			if (StoryboardsVisualIDRegistry.getVisualID(incomingLink) == ConditionPathEditPart.VISUAL_ID) {
				DestroyElementRequest r = new DestroyElementRequest(incomingLink.getElement(), false);
				cmd.add(new DestroyElementCommand(r));
				cmd.add(new DeleteCommand(getEditingDomain(), incomingLink));
				continue;
			}
			if (StoryboardsVisualIDRegistry.getVisualID(incomingLink) == StartNodeFirstNodeEditPart.VISUAL_ID) {
				DestroyReferenceRequest r = new DestroyReferenceRequest(incomingLink.getSource().getElement(), null,
						incomingLink.getTarget().getElement(), false);
				cmd.add(new DestroyReferenceCommand(r));
				cmd.add(new DeleteCommand(getEditingDomain(), incomingLink));
				continue;
			}
			if (StoryboardsVisualIDRegistry.getVisualID(incomingLink) == ActionNodeNextNodeEditPart.VISUAL_ID) {
				DestroyReferenceRequest r = new DestroyReferenceRequest(incomingLink.getSource().getElement(), null,
						incomingLink.getTarget().getElement(), false);
				cmd.add(new DestroyReferenceCommand(r));
				cmd.add(new DeleteCommand(getEditingDomain(), incomingLink));
				continue;
			}
		}
		EAnnotation annotation = view.getEAnnotation("Shortcut"); //$NON-NLS-1$
		if (annotation == null) {
			// there are indirectly referenced children, need extra commands: false
			addDestroyShortcutsCommand(cmd, view);
			// delete host element
			cmd.add(new DestroyElementCommand(req));
		} else {
			cmd.add(new DeleteCommand(getEditingDomain(), view));
		}
		return getGEFWrapper(cmd.reduce());
	}

	/**
	 * @generated
	 */
	protected Command getCreateRelationshipCommand(CreateRelationshipRequest req) {
		Command command = req.getTarget() == null ? getStartCreateRelationshipCommand(req)
				: getCompleteCreateRelationshipCommand(req);
		return command != null ? command : super.getCreateRelationshipCommand(req);
	}

	/**
	 * @generated
	 */
	protected Command getStartCreateRelationshipCommand(CreateRelationshipRequest req) {
		if (StoryboardsElementTypes.ConditionPath_4001 == req.getElementType()) {
			return null;
		}
		if (StoryboardsElementTypes.StartNodeFirstNode_4004 == req.getElementType()) {
			return null;
		}
		if (StoryboardsElementTypes.ActionNodeNextNode_4006 == req.getElementType()) {
			return null;
		}
		return null;
	}

	/**
	 * @generated
	 */
	protected Command getCompleteCreateRelationshipCommand(CreateRelationshipRequest req) {
		if (StoryboardsElementTypes.ConditionPath_4001 == req.getElementType()) {
			return getGEFWrapper(new ConditionPathCreateCommand(req, req.getSource(), req.getTarget()));
		}
		if (StoryboardsElementTypes.StartNodeFirstNode_4004 == req.getElementType()) {
			return getGEFWrapper(new StartNodeFirstNodeCreateCommand(req, req.getSource(), req.getTarget()));
		}
		if (StoryboardsElementTypes.ActionNodeNextNode_4006 == req.getElementType()) {
			return getGEFWrapper(new ActionNodeNextNodeCreateCommand(req, req.getSource(), req.getTarget()));
		}
		return null;
	}

	/**
	 * Returns command to reorient EClass based link. New link target or source
	 * should be the domain model element associated with this node.
	 * 
	 * @generated
	 */
	protected Command getReorientRelationshipCommand(ReorientRelationshipRequest req) {
		switch (getVisualID(req)) {
		case ConditionPathEditPart.VISUAL_ID:
			return getGEFWrapper(new ConditionPathReorientCommand(req));
		}
		return super.getReorientRelationshipCommand(req);
	}

	/**
	 * Returns command to reorient EReference based link. New link target or source
	 * should be the domain model element associated with this node.
	 * 
	 * @generated
	 */
	protected Command getReorientReferenceRelationshipCommand(ReorientReferenceRelationshipRequest req) {
		switch (getVisualID(req)) {
		case StartNodeFirstNodeEditPart.VISUAL_ID:
			return getGEFWrapper(new StartNodeFirstNodeReorientCommand(req));
		case ActionNodeNextNodeEditPart.VISUAL_ID:
			return getGEFWrapper(new ActionNodeNextNodeReorientCommand(req));
		}
		return super.getReorientReferenceRelationshipCommand(req);
	}

}
