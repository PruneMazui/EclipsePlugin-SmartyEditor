package com.github.PruneMazui.SmartyEditor.editors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Image;

import com.github.PruneMazui.SmartyEditor.Activator;
import com.github.PruneMazui.SmartyEditor.SmartyReferences;
import com.github.PruneMazui.SmartyEditor.Util;
import com.github.PruneMazui.SmartyEditor.preferences.PreferenceConstants;

public class ContentAssistProcessor implements IContentAssistProcessor {

	private Image icon = Activator.getImageDescriptor("icons/smarty.ico").createImage();

	private char delimiter;

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		IDocument document = viewer.getDocument();
		int pos = findDelimiter(document, offset - 1);
		if (pos >= 0) {
			if (delimiter == '$') {
				return variableProposals(document, offset, pos + 1);
			} else if (delimiter == '|') {
				return modifierProposals(document, offset, pos + 1);
			} else if (delimiter == '{') {
				return functionProposals(document, offset, pos + 1);
			}
		}
		return null;
	}

	public IContextInformation[] computeContextInformation(ITextViewer viewer, 	int offset) {
		return null;
	}

	public char[] getCompletionProposalAutoActivationCharacters() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		char[] left_delim = store.getString(PreferenceConstants.P_LEFT_DELIMITER).toCharArray();
		if (left_delim.length > 0) {
			return new char[] {left_delim[left_delim.length - 1]};
		}
		return null;
	}

	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

	public String getErrorMessage() {
		return null;
	}

	private int findDelimiter(IDocument document, int offset) {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String left_delimiter = store.getString(PreferenceConstants.P_LEFT_DELIMITER);
		int delim_length = left_delimiter.length();
		char delim = 0;
		if (delim_length > 0) {
			delim = left_delimiter.charAt(delim_length - 1);
		}
		int limit = 256;
		try {
			while (offset >= 0 && limit >= 0) {
				char ch = document.getChar(offset);
				if (ch == '$' || ch == '|') {
					delimiter = ch;
					return offset;
				}
				if (ch != 0 && ch == delim) {
					if (offset >= (delim_length - 1)) {
						String tmp = document.get(offset - delim_length + 1, delim_length);
						if (tmp.equals(left_delimiter)) {
							delimiter = '{';
							return offset;
						}
					}
				}
				if (!Character.isLetterOrDigit(ch)) {
					if ("_.".indexOf(ch) < 0) {
						return -1;
					}
				}
				offset--;
				limit--;
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return -1;
	}

	private ICompletionProposal[] variableProposals(IDocument document, int offset, int pos) {
		HashMap<String, String> variablesMap = Activator.getDefault().getSmartyReference().getVariablesMap();
		ArrayList<CompletionProposal> proposals = new ArrayList<CompletionProposal>();
		try {
			String temp = document.get(pos, offset - pos);
			Iterator<String> it = variablesMap.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				if (temp.length() == 0 || key.startsWith(temp)) {
					proposals.add(new CompletionProposal(
							key,
							pos, offset - pos, key.length(),
							icon, key, null,
							Util.createStringVariableDisplay(key)
						)
					);
				}
			}
			if (proposals.size() > 0) {
				ICompletionProposal[] result = new ICompletionProposal[proposals.size()];
				proposals.toArray(result);
				Arrays.sort(result, new XComparator());
				return result;
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}

	private ICompletionProposal[] modifierProposals(IDocument document, int offset, int pos) {
		HashMap<String, HashMap<String, String>> modifiersMap = Activator.getDefault().getSmartyReference().getModifiersMap();
		ArrayList<CompletionProposal> proposals = new ArrayList<CompletionProposal>();
		try {
			String temp = document.get(pos, offset - pos);
			Iterator<String> it = modifiersMap.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				if (temp.length() == 0 || key.startsWith(temp)) {
					HashMap<String, String> map = modifiersMap.get(key);
					String replaceStr = key;
					int cursorPos = key.length();

					if (map.containsKey(SmartyReferences.MAP_PARAM)) {
						replaceStr += map.get(SmartyReferences.MAP_PARAM);
						cursorPos += 1;
					}

					proposals.add(new CompletionProposal(
							replaceStr,
							pos, offset - pos, cursorPos,
							icon, key, null,
							Util.createStringModifierDisplay(key)
					));
				}
			}
			if (proposals.size() > 0) {
				ICompletionProposal[] result = new ICompletionProposal[proposals.size()];
				proposals.toArray(result);
				Arrays.sort(result, new XComparator());
				return result;
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}

	private ICompletionProposal[] functionProposals(IDocument document, int offset, int pos) {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String left_delimiter = store.getString(PreferenceConstants.P_LEFT_DELIMITER);
		String right_delimiter = store.getString(PreferenceConstants.P_RIGHT_DELIMITER);
		ArrayList<CompletionProposal> proposals = new ArrayList<CompletionProposal>();
		HashMap<String, HashMap<String, String>> functionsMap = Activator.getDefault().getSmartyReference().getFunctionsMap();

		try {
			String temp = document.get(pos, offset - pos);
			Iterator<String> it = functionsMap.keySet().iterator();

			while (it.hasNext()) {
				String key = it.next();
				if (temp.length() == 0 || key.startsWith(temp)) {
					HashMap<String, String> map = functionsMap.get(key);
					String replaceStr = key;
					int cursorPos = key.length();

					if (map.containsKey(SmartyReferences.MAP_PARAM)) {
						String param = map.get(SmartyReferences.MAP_PARAM);
						replaceStr += param;

						if (map.containsKey(SmartyReferences.MAP_SPACE)) {
							replaceStr += " ";
						}

						replaceStr += right_delimiter;
						cursorPos += param.indexOf("=") + 1; // 最初のイコールの場所にカーソルを合わせる
					} else {
						if (map.containsKey(SmartyReferences.MAP_SPACE)) {
							replaceStr += " ";
						}

						replaceStr += right_delimiter;
						cursorPos += right_delimiter.length();
					}

					if (map.containsKey(SmartyReferences.MAP_BLOCK)) {
						replaceStr += left_delimiter + "/" + key + right_delimiter;
					}

					proposals.add(new CompletionProposal(
							replaceStr,
							pos, offset - pos, cursorPos,
							icon, key, null,
							Util.createStringFunctionDisplay(key))
					);
				}
			}
			if (proposals.size() > 0) {
				ICompletionProposal[] result = new ICompletionProposal[proposals.size()];
				proposals.toArray(result);
				Arrays.sort(result, new XComparator());
				return result;
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}

	private class XComparator implements Comparator<Object> {
		public int compare(Object o1, Object o2) {
			if (o1 instanceof CompletionProposal && o2 instanceof CompletionProposal) {
				CompletionProposal p1 = (CompletionProposal) o1;
				CompletionProposal p2 = (CompletionProposal) o2;
				return p1.getDisplayString().compareTo(p2.getDisplayString());
			}
			return 0;
		}
	}
}
