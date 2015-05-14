package com.github.PruneMazui.SmartyEditor.editors;

import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.wst.html.core.text.IHTMLPartitions;
import org.eclipse.wst.html.ui.StructuredTextViewerConfigurationHTML;
import org.eclipse.wst.sse.core.text.IStructuredPartitions;
import org.eclipse.wst.sse.ui.internal.provisional.style.LineStyleProvider;

@SuppressWarnings("restriction")
public class StructuredTextViewerConfigurationSmarty extends StructuredTextViewerConfigurationHTML {

	protected IContentAssistProcessor[] getContentAssistProcessors(ISourceViewer sourceViewer, String partitionType) {
		IContentAssistProcessor[] processors = super.getContentAssistProcessors(sourceViewer, partitionType);
		if (processors != null) {
			if (partitionType.equals(IHTMLPartitions.HTML_DEFAULT)
					|| partitionType.equals(IHTMLPartitions.HTML_DECLARATION)
					|| partitionType.equals(IHTMLPartitions.HTML_COMMENT)
					|| partitionType.equals(IStructuredPartitions.UNKNOWN_PARTITION)) {
				IContentAssistProcessor[] newpro = new IContentAssistProcessor[processors.length + 1];
				System.arraycopy(processors, 0, newpro, 1, processors.length);
				newpro[0] = new ContentAssistProcessor();
				return newpro;
			}
		}
		return processors;
	}

	public LineStyleProvider[] getLineStyleProviders(ISourceViewer sourceViewer, String partitionType) {
		LineStyleProvider[] providers = super.getLineStyleProviders(sourceViewer, partitionType);
		if (providers != null) {
			if (partitionType.equals(IHTMLPartitions.HTML_DEFAULT)
					|| partitionType.equals(IHTMLPartitions.HTML_DECLARATION)
					|| partitionType.equals(IHTMLPartitions.HTML_COMMENT)) {
				LineStyleProvider[] newpro = new LineStyleProvider[1];
				newpro[0] = new LineStyleProviderForSmarty();
				return newpro;
			}
		}
		return providers;
	}

	public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType, int stateMask) {
		ITextHover hover = super.getTextHover(sourceViewer, contentType, stateMask);
		if (contentType.equals(IHTMLPartitions.HTML_DEFAULT)
				|| contentType.equals(IHTMLPartitions.HTML_DECLARATION)
				|| contentType.equals(IHTMLPartitions.HTML_COMMENT)) {
			ITextHover newhover = new TextHover(hover);
			return newhover;
		}
		return hover;
	}

}
