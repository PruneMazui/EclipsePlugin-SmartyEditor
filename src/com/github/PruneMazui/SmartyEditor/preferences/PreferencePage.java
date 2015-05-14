package com.github.PruneMazui.SmartyEditor.preferences;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.github.PruneMazui.SmartyEditor.Activator;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription(Messages.PreferenceDescription);
	}

	public void createFieldEditors() {

		addField(new ComboFieldEditor(PreferenceConstants.P_SMARTY_VERSION, Messages.PreferenceSmartyVersion, PreferenceConstants.C_SMARTY_VERSION_SELECT, getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.P_TAG_COLOR, Messages.PreferenceTagColor, getFieldEditorParent()));
		addField(new ColorFieldEditor(PreferenceConstants.P_COMMENT_COLOR, Messages.PreferenceCommentColor, getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_LEFT_DELIMITER, Messages.PreferenceLeftDelimiter, getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_RIGHT_DELIMITER, Messages.PreferenceRightDelimiter, getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
	}

}
