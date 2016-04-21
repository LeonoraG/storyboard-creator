package eu.scasefp7.eclipse.storyboards.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eu.scasefp7.eclipse.core.ontology.DynamicOntologyAPI;
import eu.scasefp7.eclipse.storyboards.Activator;

/**
 * A command handler for exporting a storyboard diagram to the dynamic ontology.
 * 
 * @author themis
 */
public class ExportToOntologyHandler extends ProjectAwareHandler {

	/**
	 * This function is called when the user selects the menu item. It reads the selected resource(s) and populates the
	 * dynamic ontology.
	 * 
	 * @param event the event containing the information about which file was selected.
	 * @return the result of the execution which must be {@code null}.
	 */
	@SuppressWarnings("unchecked")
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			List<Object> selectionList = structuredSelection.toList();
			String fileName = event.getParameter("fileName");
			DynamicOntologyAPI ontology;
			//When handler is called from builder
			if(fileName != null){
				Path path = new Path(fileName);
				IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
				//selectionList.add(file);
				ontology = new DynamicOntologyAPI(file.getProject());
			}
			else
				ontology = new DynamicOntologyAPI(getProjectOfSelectionList(selectionList), true);
			// Iterate over the selected files
			for (Object object : selectionList) {
				IFile file = (IFile) Platform.getAdapterManager().getAdapter(object, IFile.class);
				if (file == null) {
					if (object instanceof IAdaptable) {
						file = (IFile) ((IAdaptable) object).getAdapter(IFile.class);
					}
				}
				if (file != null) {
					instantiateOntology(file, ontology);
				}
			}
			ontology.close();
		}
		return null;
	}

	/**
	 * Instantiates the dynamic ontology given the file of a storyboard diagram.
	 * 
	 * @param file an {@link IFile} instance of a storyboard diagram.
	 * @param ontology the ontology to be instantiated.
	 */
	protected void instantiateOntology(IFile file, DynamicOntologyAPI ontology) {
		try {
			String filename = file.getName();
			String diagramName = filename.substring(0, filename.lastIndexOf('.'));
			diagramName = "SBD_" + diagramName.substring(diagramName.lastIndexOf('\\') + 1);
			instantiateOntology(diagramName, file.getContents(), ontology);
		} catch (CoreException e) {
			Activator.log("Error reading the contents of an sbd file", e);
		}
	}

	/**
	 * Instantiates the dynamic ontology given the file of a storyboard diagram.
	 * 
	 * @param file an {@link File} instance of a storyboard diagram.
	 * @param ontology the ontology to be instantiated.
	 */
	protected void instantiateOntology(File file, DynamicOntologyAPI ontology) {
		try {
			String filename = file.getName();
			String diagramName = filename.substring(0, filename.lastIndexOf('.'));
			diagramName = "SBD_" + diagramName.substring(diagramName.lastIndexOf('\\') + 1);
			InputStream is = new FileInputStream(file);
			instantiateOntology(diagramName, is, ontology);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Instantiates the ontology given a diagram as an inputstream string.
	 * 
	 * @param diagramName the name of the diagram.
	 * @param stream the {@link InputStream} that contains the diagram
	 * @param ontology the ontology to be instantiated.
	 */
	protected void instantiateOntology(String diagramName, InputStream stream, DynamicOntologyAPI ontology) {
		try {
			ontology.addActivityDiagram(diagramName);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse(stream);
			Element doc = dom.getDocumentElement();
			doc.normalize();
			Node root = doc.getElementsByTagName("auth.storyboards:StoryboardDiagram").item(0);
			sbdToOwl(diagramName, ontology, root);
			ontology.close();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			Activator.log("Error instantiating the dynamic ontology", e);
		}
	}

	/**
	 * Transfers an sbd file in the dynamic ontology.
	 * 
	 * @param diagramName the name of the diagram.
	 * @param ontology the ontology instance.
	 * @param root the root node of the sbd diagram model.
	 */
	private void sbdToOwl(String diagramName, DynamicOntologyAPI ontology, Node root) {

		HashMap<String, SBDNode> ids = new HashMap<String, SBDNode>();

		NodeList nodes = root.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			SBDNode sbdnode = new SBDNode(node);

			if (sbdnode.getType() != null) {
				// Iterate over all nodes
				if (sbdnode.getType().equals("action")) {
					// Add an action node as an activity
					ontology.addActivity(sbdnode.getName());
					ontology.connectActivityDiagramToElement(diagramName, sbdnode.getName());
					String[] actionAndObject = getActionAndObject(sbdnode.getName(), sbdnode.getAnnotations());
					String action = actionAndObject[0];
					String object1 = actionAndObject[1];
					ontology.addActionToActivity(sbdnode.getName(), action);
					ontology.addObjectToActivity(sbdnode.getName(), object1);
					// String actiontype = sbdnode.get("type") == null ? "create" : sbdnode.get("type");
					// ontology.addActivityTypeToActivity(sbdnode.getName(), actiontype);
				} else if (sbdnode.getType().equals("startnode")) {
					// Add the start node
					ontology.addInitialActivity("StartNode");
					ontology.connectActivityDiagramToElement(diagramName, "StartNode");
					if (sbdnode.getPrecondition() != null) {
						ontology.addPreconditionToDiagram(diagramName, sbdnode.getPrecondition());
					}
				} else if (sbdnode.getType().equals("endnode")) {
					// Add the end node
					ontology.addFinalActivity("EndNode");
					ontology.connectActivityDiagramToElement(diagramName, "EndNode");
				} else if (sbdnode.getType().equals("storyboard")) {
					// Add a storyboard node as an activity
					ontology.addActivity(sbdnode.getName());
					ontology.connectActivityDiagramToElement(diagramName, sbdnode.getName());
				}
				ids.put(sbdnode.getId(), sbdnode);
			}
		}

		for (SBDNode sbdnode : ids.values()) {
			if (sbdnode.getType().equals("action")) {
				// Add the properties of the action
				ArrayList<String> propertyIds = sbdnode.getProperties();
				for (String propertyId : propertyIds) {
					SBDNode property = ids.get(propertyId);
					ontology.addPropertyToActivity(sbdnode.getName(), property.getName());
				}
			}
			if (sbdnode.getType().equals("action") || sbdnode.getType().equals("storyboard")
					|| sbdnode.getType().equals("startnode")) {
				String nextNodeId = sbdnode.getNextNode();
				String from = sbdnode.getName();
				SBDNode nextNode = ids.get(nextNodeId);
				addTransitions(diagramName, ontology, ids, from, nextNode, new ArrayList<String>());
			}
		}
	}

	/**
	 * Adds the transitions to an action node of a storyboard. If there are conditions, this function recursively calls
	 * itself to find the next action node and connects all conditions to the transition.
	 * 
	 * @param diagramName the name of the diagram.
	 * @param ontology the ontology instance.
	 * @param ids hashmap used to find the ids of the nodes.
	 * @param from the name of the initial node.
	 * @param nextNode the current name of the next node.
	 * @param conditionNames list used to hold the conditions to be added to the final transition.
	 */
	private void addTransitions(String diagramName, DynamicOntologyAPI ontology, HashMap<String, SBDNode> ids,
			String from, SBDNode nextNode, ArrayList<String> conditionNames) {
		if (nextNode.getType().equals("condition")) {
			// Add the transitions in the case of conditions
			ArrayList<SBDNode> conditionPaths = nextNode.getChildren();

			SBDNode actionOfConditionPath0 = ids.get(conditionPaths.get(0).getNextNode());
			ArrayList<String> newConditionNames0 = new ArrayList<String>(conditionNames);
			newConditionNames0.add(conditionPaths.get(0).getName());
			if (actionOfConditionPath0.getType().equals("condition")) {
				addTransitions(diagramName, ontology, ids, from, actionOfConditionPath0, newConditionNames0);
			} else {
				ontology.addTransition(from, actionOfConditionPath0.getName());
				ontology.connectActivityDiagramToTransition(diagramName, from, actionOfConditionPath0.getName());
				for (String conditionName : newConditionNames0) {
					ontology.addConditionToTransition(conditionName, from, actionOfConditionPath0.getName());
				}
			}

			SBDNode actionOfConditionPath1 = ids.get(conditionPaths.get(1).getNextNode());
			ArrayList<String> newConditionNames1 = new ArrayList<String>(conditionNames);
			newConditionNames1.add(conditionPaths.get(1).getName());
			if (actionOfConditionPath1.getType().equals("condition")) {
				addTransitions(diagramName, ontology, ids, from, actionOfConditionPath1, newConditionNames1);
			} else {
				ontology.addTransition(from, actionOfConditionPath1.getName());
				ontology.connectActivityDiagramToTransition(diagramName, from, actionOfConditionPath1.getName());
				for (String conditionName : newConditionNames1) {
					ontology.addConditionToTransition(conditionName, from, actionOfConditionPath1.getName());
				}
			}
		} else {
			// Add the transitions
			ontology.addTransition(from, nextNode.getName());
			ontology.connectActivityDiagramToTransition(diagramName, from, nextNode.getName());
		}
	}

	/**
	 * Extracts the action and the object of an activity.
	 * 
	 * @param activity the activity to be split.
	 * @param annotations the annotations of this activity.
	 * @return a string array including the action in the first position and the object in the second.
	 */
	private static String[] getActionAndObject(String activity, String annotations) {
		String[] actobj = new String[2];
		actobj[0] = "";
		actobj[1] = "";
		if (annotations != null) {
			String[] annotationList = annotations.split("\\\\n");
			for (int i = 0; i < annotationList.length; i++) {
				String annotation = annotationList[i];
				String annType = annotation.split("\\\\t")[0].split(":")[1].substring(0, 1);
				if (annType.equals("T")) {
					String annotationType = annotation.split("\\\\t")[1].split("\\s+")[0];
					if (annotationType.equals("Action"))
						actobj[0] = annotation.split("\\\\t")[2];
					else if (annotationType.equals("Object"))
						actobj[1] = annotation.split("\\\\t")[2];
				}
			}
		}
		String[] tempactobj = activity.split("\\s+");
		if (actobj[0].equals(""))
			actobj[0] = tempactobj[0];
		if (actobj[1].equals(""))
			actobj[1] = tempactobj[tempactobj.length - 1];
		return actobj;
	}

	/**
	 * Tests this export handler.
	 * 
	 * @param args the filename of the file to be added to the dynamic ontology.
	 */
	public static void main(String[] args) {
		File file = new File(args[0]);
		DynamicOntologyAPI ontology = new DynamicOntologyAPI("Project", true);
		new ExportToOntologyHandler().instantiateOntology(file, ontology);
	}
}