package eu.scasefp7.eclipse.storyboards.diagram.part;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.gmf.runtime.diagram.core.preferences.PreferencesHint;
import org.eclipse.gmf.tooling.runtime.LogHelper;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import eu.scasefp7.eclipse.storyboards.diagram.edit.policies.StoryboardsBaseItemSemanticEditPolicy;
import eu.scasefp7.eclipse.storyboards.diagram.providers.ElementInitializers;
import eu.scasefp7.eclipse.storyboards.provider.StoryboardsItemProviderAdapterFactory;

/**
 * @generated
 */
public class StoryboardsDiagramEditorPlugin extends AbstractUIPlugin {

	/**
	 * A UTC ISO 8601 date formatter used to log the time of errors.
	 * 
	 * @generated NOT
	 */
	private static final DateFormat formatter;
	/**
	 * @generated NOT
	 */
	static {
		formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	/**
	 * The starting time of the current session for this plugin.
	 * 
	 * @generated NOT
	 */
	private static String STARTING_TIME;

	/**
	 * The current error ID for this session for this plugin.
	 * 
	 * @generated NOT
	 */
	private static int errorID;

	/**
	 * The plug-in ID.
	 * 
	 * @generated NOT
	 */
	public static final String PLUGIN_ID = "StoryboardsDiagram";

	/**
	 * @generated
	 */
	public static final String ID = "eu.scasefp7.eclipse.storyboards.diagram"; //$NON-NLS-1$

	/**
	 * @generated
	 */
	private LogHelper myLogHelper;

	/**
	 * @generated
	 */
	public static final PreferencesHint DIAGRAM_PREFERENCES_HINT = new PreferencesHint(ID);

	/**
	 * @generated
	 */
	private static StoryboardsDiagramEditorPlugin instance;

	/**
	 * @generated
	 */
	private ComposedAdapterFactory adapterFactory;

	/**
	 * @generated
	 */
	private StoryboardsDocumentProvider documentProvider;

	/**
	 * @generated
	 */
	private StoryboardsBaseItemSemanticEditPolicy.LinkConstraints linkConstraints;

	/**
	 * @generated
	 */
	private ElementInitializers initializers;

	/**
	 * @generated
	 */
	public StoryboardsDiagramEditorPlugin() {
	}

	/**
	 * @generated NOT
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		instance = this;
		myLogHelper = new LogHelper(this);
		PreferencesHint.registerPreferenceStore(DIAGRAM_PREFERENCES_HINT, getPreferenceStore());
		adapterFactory = createAdapterFactory();
		STARTING_TIME = formatter.format(new Date());
		errorID = 0;
	}

	/**
	 * @generated
	 */
	public void stop(BundleContext context) throws Exception {
		adapterFactory.dispose();
		adapterFactory = null;
		linkConstraints = null;
		initializers = null;
		instance = null;
		super.stop(context);
	}

	/**
	 * @generated
	 */
	public static StoryboardsDiagramEditorPlugin getInstance() {
		return instance;
	}

	/**
	 * @generated
	 */
	protected ComposedAdapterFactory createAdapterFactory() {
		ArrayList<AdapterFactory> factories = new ArrayList<AdapterFactory>();
		fillItemProviderFactories(factories);
		return new ComposedAdapterFactory(factories);
	}

	/**
	 * @generated
	 */
	protected void fillItemProviderFactories(List<AdapterFactory> factories) {
		factories.add(new StoryboardsItemProviderAdapterFactory());
		factories.add(new ResourceItemProviderAdapterFactory());
		factories.add(new ReflectiveItemProviderAdapterFactory());
	}

	/**
	 * @generated
	 */
	public AdapterFactory getItemProvidersAdapterFactory() {
		return adapterFactory;
	}

	/**
	 * @generated
	 */
	public ImageDescriptor getItemImageDescriptor(Object item) {
		IItemLabelProvider labelProvider = (IItemLabelProvider) adapterFactory.adapt(item, IItemLabelProvider.class);
		if (labelProvider != null) {
			return ExtendedImageRegistry.getInstance().getImageDescriptor(labelProvider.getImage(item));
		}
		return null;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 * 
	 * @generated
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getBundledImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(ID, path);
	}

	/**
	 * Respects images residing in any plug-in. If path is relative,
	 * then this bundle is looked up for the image, otherwise, for absolute
	 * path, first segment is taken as id of plug-in with image
	 * 
	 * @generated
	 * @param path the path to image, either absolute (with plug-in id as first segment), or relative for bundled images
	 * @return the image descriptor
	 */
	public static ImageDescriptor findImageDescriptor(String path) {
		final IPath p = new Path(path);
		if (p.isAbsolute() && p.segmentCount() > 1) {
			return AbstractUIPlugin.imageDescriptorFromPlugin(p.segment(0), p.removeFirstSegments(1).makeAbsolute()
					.toString());
		} else {
			return getBundledImageDescriptor(p.makeAbsolute().toString());
		}
	}

	/**
	 * Returns an image for the image file at the given plug-in relative path.
	 * Client do not need to dispose this image. Images will be disposed automatically.
	 * 
	 * @generated
	 * @param path the path
	 * @return image instance
	 */
	public Image getBundledImage(String path) {
		Image image = getImageRegistry().get(path);
		if (image == null) {
			getImageRegistry().put(path, getBundledImageDescriptor(path));
			image = getImageRegistry().get(path);
		}
		return image;
	}

	/**
	 * Returns string from plug-in's resource bundle
	 * 
	 * @generated
	 */
	public static String getString(String key) {
		return Platform.getResourceString(getInstance().getBundle(), "%" + key); //$NON-NLS-1$
	}

	/**
	 * @generated
	 */
	public StoryboardsDocumentProvider getDocumentProvider() {
		if (documentProvider == null) {
			documentProvider = new StoryboardsDocumentProvider();
		}
		return documentProvider;
	}

	/**
	 * @generated
	 */
	public StoryboardsBaseItemSemanticEditPolicy.LinkConstraints getLinkConstraints() {
		return linkConstraints;
	}

	/**
	 * @generated
	 */
	public void setLinkConstraints(StoryboardsBaseItemSemanticEditPolicy.LinkConstraints lc) {
		this.linkConstraints = lc;
	}

	/**
	 * @generated
	 */
	public ElementInitializers getElementInitializers() {
		return initializers;
	}

	/**
	 * @generated
	 */
	public void setElementInitializers(ElementInitializers i) {
		this.initializers = i;
	}

	/**
	 * @generated
	 */
	public void logError(String error) {
		getLogHelper().logError(error, null);
	}

	/**
	 * @generated
	 */
	public void logError(String error, Throwable throwable) {
		getLogHelper().logError(error, throwable);
	}

	/**
	 * @generated
	 */
	public void logInfo(String message) {
		getLogHelper().logInfo(message, null);
	}

	/**
	 * @generated
	 */
	public void logInfo(String message, Throwable throwable) {
		getLogHelper().logInfo(message, throwable);
	}

	/**
	 * @generated
	 */
	public LogHelper getLogHelper() {
		return myLogHelper;
	}

	/**
	 * Logs an exception to the Eclipse log file. This method detects the class and the method in which the exception
	 * was caught automatically using the current stack trace. If required, the user can override these values by
	 * calling {@link #log(String, String, String, Exception)} instead.
	 * 
	 * @param message a human-readable message about the exception.
	 * @param exception the exception that will be logged.
	 * 
	 * @generated NOT
	 */
	public static void log(String message, Exception exception) {
		StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];
		log(stackTraceElement.getClassName(), stackTraceElement.getMethodName(), message, exception);
	}

	/**
	 * Logs an exception to the Eclipse log file. Note that in most cases you can use the
	 * {@link #log(String, Exception)} method which automatically detects the class and the method in which the
	 * exception was caught, so it requires as parameters only a human-readable message and the exception.
	 * 
	 * @param className the name of the class in which the exception was caught.
	 * @param methodName the name of the method in which the exception was caught.
	 * @param message a human-readable message about the exception.
	 * @param exception the exception that will be logged.
	 * 
	 * @generated NOT
	 */
	public static void log(String className, String methodName, String message, Exception exception) {
		String msg = message;
		msg += "\n!ERROR_ID t" + errorID;
		msg += "\n!SERVICE_NAME Requirements Editor";
		msg += "\n!SERVICE_VERSION 1.0.0-SNAPSHOT";
		msg += "\n!STARTING_TIME " + STARTING_TIME;
		msg += "\n!CLASS_NAME " + className;
		msg += "\n!FUNCTION_NAME " + methodName;
		msg += "\n!FAILURE_TIMESTAMP " + formatter.format(new Date());
		errorID++;
		if (instance != null)
			instance.getLog().log(new Status(Status.ERROR, PLUGIN_ID, Status.OK, msg, exception));
		else
			exception.printStackTrace();
	}
}
