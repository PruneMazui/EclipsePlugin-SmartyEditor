package com.github.PruneMazui.SmartyEditor.preferences;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

	private static final String BUNDLE_NAME = "com.github.PruneMazui.SmartyEditor.preferences.messages"; //$NON-NLS-1$

	public static String PreferenceDescription;
	public static String PreferenceSmartyVersion;
	public static String PreferenceTagColor;
	public static String PreferenceCommentColor;
	public static String PreferenceLeftDelimiter;
	public static String PreferenceRightDelimiter;

	public static String ContentAssistDescription;
	public static String ContentAssistParameters;
	public static String ContentAssistArguments;
	public static String ContentOptionFlg;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
