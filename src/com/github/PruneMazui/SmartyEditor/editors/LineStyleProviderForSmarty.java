package com.github.PruneMazui.SmartyEditor.editors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wst.html.ui.internal.style.LineStyleProviderForHTML;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.ui.internal.provisional.style.LineStyleProvider;
import org.eclipse.wst.sse.ui.internal.provisional.style.ReconcilerHighlighter;
import org.eclipse.wst.xml.core.internal.regions.DOMRegionContext;

import com.github.PruneMazui.SmartyEditor.Activator;
import com.github.PruneMazui.SmartyEditor.preferences.PreferenceConstants;

@SuppressWarnings("restriction")
public class LineStyleProviderForSmarty extends LineStyleProviderForHTML implements LineStyleProvider {

	private IStructuredDocument document;

	public LineStyleProviderForSmarty() {
		super();
	}

	public void init(IStructuredDocument structuredDocument, ReconcilerHighlighter highlighter) {
		document = structuredDocument;
		super.init(structuredDocument, highlighter);
	}

	public void release() {
		document = null;
		super.release();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean prepareRegions(ITypedRegion typedRegion, int lineRequestStart, int lineRequestLength, Collection holdResults) {
		List<Object> results = new ArrayList<Object>();
		boolean result = super.prepareRegions(typedRegion, lineRequestStart, lineRequestLength, results);
		if (document == null) {
			return result;
		}
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		Color tagColor = new Color(null, PreferenceConverter.getColor(store, PreferenceConstants.P_TAG_COLOR));
		Color commentColor = new Color(null, PreferenceConverter.getColor(store, PreferenceConstants.P_COMMENT_COLOR));
		String left_delimiter = store.getString(PreferenceConstants.P_LEFT_DELIMITER);
		String right_delimiter = store.getString(PreferenceConstants.P_RIGHT_DELIMITER);
		String leftDelim = escapeDelimiter(left_delimiter);
		String rightDelim = escapeDelimiter(right_delimiter);
		Iterator<Object> it = results.iterator();
		while (it.hasNext()) {
			StyleRange styleRange = (StyleRange) it.next();
			IStructuredDocumentRegion region;
			region = document.getRegionAtCharacterOffset(styleRange.start);
			String text = region.getText();
			int mStart = styleRange.start - region.getStartOffset();
			int mEnd = mStart + styleRange.length;
			IStructuredDocumentRegion chkRegion;
			chkRegion = region.getPrevious();
			while (chkRegion != null) {
				String type = chkRegion.getType();
				if (type.equals(DOMRegionContext.XML_CONTENT) || type.equals(DOMRegionContext.UNDEFINED) || type.equals(DOMRegionContext.XML_TAG_OPEN)) {
					text = chkRegion.getText() + text;
					mStart += chkRegion.getLength();
					mEnd += chkRegion.getLength();
					chkRegion = chkRegion.getPrevious();
				} else {
					break;
				}
			}
			chkRegion = region.getNext();
			while (chkRegion != null) {
				String type = chkRegion.getType();
				if (type.equals(DOMRegionContext.XML_CONTENT) || type.equals(DOMRegionContext.UNDEFINED) || type.equals(DOMRegionContext.XML_TAG_CLOSE)) {
					text = text + chkRegion.getText();
					chkRegion = chkRegion.getNext();
				} else {
					break;
				}
			}
			Pattern p = Pattern.compile(leftDelim + "(.*?[^\\\\]|)" + rightDelim, Pattern.DOTALL);
			Matcher m = p.matcher(text);
			int pos = 0;
			while (m.find()) {
				Color fgColor;
				if (m.group().startsWith(left_delimiter + "*") && m.group().endsWith("*" + right_delimiter)) {
					fgColor = commentColor;
				} else {
					fgColor = tagColor;
				}
				if (m.start() < mStart) {
					// pattern starts in previous range
					if (m.end() < mStart) {
						// pattern ends in previous range ...ignore
					} else if (m.end() < mEnd) {
						// pattern ends in current range
						StyleRange curr = (StyleRange) styleRange.clone();
						curr.start = styleRange.start;
						curr.length = m.end() - mStart;
						curr.foreground = fgColor;
						holdResults.add(curr);
						pos = m.end() - mStart;
					} else {
						// pattern ends in next range
						StyleRange curr = (StyleRange) styleRange.clone();
						curr.foreground = fgColor;
						holdResults.add(curr);
						pos = m.end();
						break;
					}
					continue;
				} else if (m.start() < mEnd) {
					// pattern starts in current range
					if (m.start() > mStart && pos == 0) {
						// leading sub range
						StyleRange prev = (StyleRange) styleRange.clone();
						prev.start = styleRange.start;
						prev.length = m.start() - mStart;
						holdResults.add(prev);
						pos = m.end() - mStart;
					}
					if (m.end() < mEnd) {
						// pattern ends in current range
						StyleRange curr = (StyleRange) styleRange.clone();
						curr.start = styleRange.start + m.start() - mStart;
						curr.length = m.end() - m.start();
						curr.foreground = fgColor;
						holdResults.add(curr);
						pos = m.end() - mStart;
					} else {
						// pattern ends in next range
						StyleRange curr = (StyleRange) styleRange.clone();
						curr.start = styleRange.start + m.start() - mStart;
						curr.length = styleRange.length - m.start() + mStart;
						curr.foreground = fgColor;
						holdResults.add(curr);
						pos = m.end() - mStart;
					}
				} else {
					// pattern starts in next range ...ignore
					break;
				}
			}
			if (pos < styleRange.length) {
				// trailing sub range
				StyleRange post = (StyleRange) styleRange.clone();
				post.start = styleRange.start + pos;
				post.length = styleRange.length - pos;
				holdResults.add(post);
			}
		}
		return result;
	}

	static String escapeDelimiter(String delimiter) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < delimiter.length(); i++) {
			switch (delimiter.charAt(i)) {
				case '{':
				case '}':
				case '(':
				case ')':
				case '[':
				case ']':
				case '.':
				case '?':
				case '*':
				case '^':
				case '$':
				case '\\':
					buf.append('\\');
				default:
					buf.append(delimiter.charAt(i));
			}
		}
		return buf.toString();
	}

}
