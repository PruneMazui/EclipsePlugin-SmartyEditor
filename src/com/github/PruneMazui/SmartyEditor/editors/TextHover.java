package com.github.PruneMazui.SmartyEditor.editors;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextHoverExtension;
import org.eclipse.jface.text.ITextHoverExtension2;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Region;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.editors.text.EditorsUI;

import com.github.PruneMazui.SmartyEditor.Activator;
import com.github.PruneMazui.SmartyEditor.Util;
import com.github.PruneMazui.SmartyEditor.preferences.PreferenceConstants;

public class TextHover implements ITextHover, ITextHoverExtension, ITextHoverExtension2 {

	private ITextHover parent;
	private IRegion hoverRegion;

	public TextHover(ITextHover parent) {
		this.parent = parent;
	}

	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String left_delimiter = store.getString(PreferenceConstants.P_LEFT_DELIMITER);
		int delim_length = left_delimiter.length();
		char delim = 0;
		if (delim_length > 0) {
			delim = left_delimiter.charAt(delim_length - 1);
		}
		hoverRegion = null;
		IDocument document = textViewer.getDocument();
		try {
			int startOffset = -1;
			int ofs = offset;
			while (ofs >= 0) {
				char ch = document.getChar(ofs);
				if (ch == '|') {
					startOffset = ofs;
					break;
				}

				if (ch == '$') {
					startOffset = ofs;
					break;
				}

				if (ch != 0 && ch == delim) {
					if (ofs >= (delim_length - 1)) {
						String tmp = document.get(ofs - delim_length + 1, delim_length);
						if (tmp.equals(left_delimiter)) {
							startOffset = ofs;
							break;
						}
					}
				}
				if (Character.isLetterOrDigit(ch) || "_.".indexOf(ch) >= 0) {
				} else {
					break;
				}
				ofs--;
			}
			if (startOffset >= 0) {
				while (offset < document.getLength()) {
					char ch = document.getChar(offset);
					if (Character.isLetterOrDigit(ch) || "_.".indexOf(ch) >= 0) {
					} else {
						break;
					}
					offset++;
				}
				hoverRegion = new Region(startOffset, offset - startOffset);
				return hoverRegion;
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return parent.getHoverRegion(textViewer, offset);
	}

	/**
	 * @deprecated
	 */
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		return null;
	}

	@SuppressWarnings("deprecation")
	public Object getHoverInfo2(ITextViewer textViewer, IRegion hoverRegion) {
		if (this.hoverRegion != null
				&& this.hoverRegion.getOffset() == hoverRegion.getOffset()
				&& this.hoverRegion.getLength() == hoverRegion.getLength()) {
			try {
				IDocument document = textViewer.getDocument();
				String delim = document.get(hoverRegion.getOffset(), 1);
				String key = document.get(hoverRegion.getOffset() + 1, hoverRegion.getLength() - 1);

				if (delim.equals("|")) {
					return Util.createStringModifierDisplay(key);
				}

				if (delim.equals("$")) {
					return Util.createStringVariableDisplay(key);
				}

				return Util.createStringFunctionDisplay(key);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		if (parent instanceof ITextHoverExtension2) {
			return ((ITextHoverExtension2) parent).getHoverInfo2(textViewer, hoverRegion);
		} else {
			return parent.getHoverInfo(textViewer, hoverRegion);
		}
	}

	public IInformationControlCreator getHoverControlCreator() {
		return new IInformationControlCreator() {
			public IInformationControl createInformationControl(Shell parent) {
				return new DefaultInformationControl(parent, EditorsUI.getTooltipAffordanceString());
			}
		};
	}

}
