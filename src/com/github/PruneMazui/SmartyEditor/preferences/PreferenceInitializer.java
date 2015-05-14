package com.github.PruneMazui.SmartyEditor.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

import com.github.PruneMazui.SmartyEditor.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		store.setDefault(PreferenceConstants.P_SMARTY_VERSION, PreferenceConstants.C_SMARTY_VERSION_3);
		PreferenceConverter.setDefault(store, PreferenceConstants.P_TAG_COLOR, new RGB(255, 0, 0));
		PreferenceConverter.setDefault(store, PreferenceConstants.P_COMMENT_COLOR, new RGB(0, 128, 0));
		store.setDefault(PreferenceConstants.P_LEFT_DELIMITER, "{");
		store.setDefault(PreferenceConstants.P_RIGHT_DELIMITER, "}");
	}
}
