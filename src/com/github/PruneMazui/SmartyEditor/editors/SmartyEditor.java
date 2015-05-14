package com.github.PruneMazui.SmartyEditor.editors;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

import com.github.PruneMazui.SmartyEditor.Activator;
import com.github.PruneMazui.SmartyEditor.preferences.PreferenceConstants;

public class SmartyEditor extends StructuredTextEditor implements IPropertyChangeListener {

	public SmartyEditor() {
		super();
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(this);
	}

	public void dispose() {
		Activator.getDefault().getPreferenceStore().removePropertyChangeListener(this);
		super.dispose();
	}

	public void propertyChange(PropertyChangeEvent event) {
		String prop = event.getProperty();
		if (prop.equals(PreferenceConstants.P_TAG_COLOR)
				|| prop.equals(PreferenceConstants.P_COMMENT_COLOR)
				|| prop.equals(PreferenceConstants.P_LEFT_DELIMITER)
				|| prop.equals(PreferenceConstants.P_RIGHT_DELIMITER)) {
			getTextViewer().refresh();
		}
	}

}
