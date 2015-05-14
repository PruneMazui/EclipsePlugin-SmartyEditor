package com.github.PruneMazui.SmartyEditor;

import java.util.HashMap;

import com.github.PruneMazui.SmartyEditor.preferences.Messages;

public class Util {

	public static String createStringVariableDisplay(String key) {
		StringBuffer displayString = new StringBuffer();
		HashMap<String, String> variablesMap = Activator.getDefault().getSmartyReference().getVariablesMap();

		if (!variablesMap.containsKey(key)) {
			// $smarty.hoge.fugaになっている可能性があるのを補完
			if (!key.startsWith("smarty") || !key.contains(".")) {
				return "";
			}

			String searchWord = "";
			Boolean isMatch = false;
			for (String value : key.split("\\.")) {

				if (searchWord.length() != 0) {
					searchWord += ".";
				}
				searchWord += value;

				if (variablesMap.containsKey(searchWord)) {
					key = searchWord;
					isMatch = true;
				}
			}

			if(!isMatch) {
				return "";
			}
		}

		String desc = variablesMap.get(key);

		if (desc == null) {
			desc = "";
		}

		displayString.append("<b>" + key + "</b><br>" + convertDescMessage(desc));

		return displayString.toString();
	}

	/**
	 * modifier の表示メッセージを作成
	 *
	 * @param key
	 * @return
	 */
	public static String createStringModifierDisplay(String key) {
		StringBuffer displayString = new StringBuffer();
		HashMap<String, HashMap<String, String>> modifiersMap = Activator.getDefault().getSmartyReference().getModifiersMap();

		if (!modifiersMap.containsKey(key)) {
			return "";
		}

		HashMap<String, String> map = modifiersMap.get(key);

		displayString.append("<b>" + key + "</b>");

		// 説明文の追記
		if (map.containsKey(SmartyReferences.MAP_DESC)) {
			displayString.append("<br>" + convertDescMessage(map.get(SmartyReferences.MAP_DESC)));
		}

		if (map.containsKey(SmartyReferences.MAP_PDESC)) {
			displayString.append("<p><b>" + Messages.ContentAssistParameters + "</b><br>" + map.get(SmartyReferences.MAP_PDESC));
		}

		return displayString.toString();
	}

	/**
	 * function の表示メッセージを作成
	 *
	 * @param key
	 * @return 表示メッセージ
	 */
	public static String createStringFunctionDisplay(String key) {
		StringBuffer displayString = new StringBuffer();
		HashMap<String, HashMap<String, String>> functionsMap = Activator.getDefault().getSmartyReference().getFunctionsMap();

		if (!functionsMap.containsKey(key)) {
			return "";
		}

		HashMap<String, String> map = functionsMap.get(key);

		displayString.append("<b>" + key + "</b>");

		// 説明文の追記
		if (map.containsKey(SmartyReferences.MAP_DESC)) {
			displayString.append("<br>" + convertDescMessage(map.get(SmartyReferences.MAP_DESC)) + "<br>");
		}

		if (map.containsKey(SmartyReferences.MAP_PDESC)) {
			displayString.append("<b>" + Messages.ContentAssistArguments + "</b><br>" + map.get(SmartyReferences.MAP_PDESC));
		}

		if (map.containsKey(SmartyReferences.MAP_FLAG)) {
			displayString.append("<b>" + Messages.ContentOptionFlg + "</b><br>" + map.get(SmartyReferences.MAP_FLAG));
		}

		return displayString.toString();
	}

	private static String convertDescMessage(String message) {
		String result = "";

		for (String line : message.split("\\\\n")) {
			if (result.length() != 0) {
				result += "<br>";
			}

			result += "&nbsp;&nbsp;" + line;
		}

		return result;
	}
}
