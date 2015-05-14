package com.github.PruneMazui.SmartyEditor;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.github.PruneMazui.SmartyEditor.preferences.PreferenceConstants;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.github.PruneMazui.SmartyEditor";

	// The shared instance
	private static Activator plugin;

	private SmartyReferences smarty2;

	private SmartyReferences smarty3;

	public SmartyReferences getSmartyReference() {

		IPreferenceStore store = getDefault().getPreferenceStore();
		String smarty_version = store.getString(PreferenceConstants.P_SMARTY_VERSION);

		if (smarty_version.equals(PreferenceConstants.C_SMARTY_VERSION_2)) {
			return smarty2;
		} else {
			return smarty3;
		}
	}

	/**
	 * The constructor
	 */
	public Activator() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		smarty2 = new SmartyReferences("smarty2.xml");
		smarty3 = new SmartyReferences("smarty3.xml");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 *
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
