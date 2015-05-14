package com.github.PruneMazui.SmartyEditor;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.preference.IPreferenceStore;

import com.github.PruneMazui.SmartyEditor.preferences.PreferenceConstants;

public class SmartyReferences {

	private String smartyXml = null;

	private HashMap<String, HashMap<String, HashMap<String, String>>> functionsMap = null;
	private HashMap<String, HashMap<String, HashMap<String, String>>> modifiersMap = null;
	private HashMap<String, HashMap<String, String>> variablesMap = null;

	public final static String MAP_PARAM = "p";
	public final static String MAP_DESC = "d";
	public final static String MAP_SPACE = "s";
	public final static String MAP_BLOCK = "b";
	public final static String MAP_PDESC = "v";
	public final static String MAP_FLAG = "f";

	public SmartyReferences(String filename) {
		smartyXml = "";
		Plugin plugin = Activator.getDefault();
		try {
			StringBuffer buf = new StringBuffer();
			BufferedReader reader = null;

			if(Debug.debugFlg) {
				// デバッグの時は都度定義ファイルを読み込む
				reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filename), "UTF-8"));

				String line = "";
				while ((line = reader.readLine()) != null) {
					buf.append(line);
				}

				reader.close();
			}
			else {
				IPath path = plugin.getStateLocation().append("/" + filename);
				boolean isExist = path.toFile().exists();

				PrintStream writer = null;
				if (isExist) {
					reader = new BufferedReader(new InputStreamReader(new FileInputStream(path.toFile()), "UTF-8"));
				} else {
					reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filename), "UTF-8"));
					writer = new PrintStream(path.toFile(), "UTF-8");
				}

				String line = "";
				while ((line = reader.readLine()) != null) {
					buf.append(line);
					if (!isExist)
						writer.println(line);
				}
				reader.close();
				if (!isExist)
					writer.close();
			}
			smartyXml = buf.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HashMap<String, HashMap<String, String>> getFunctionsMap() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String version = store.getString(PreferenceConstants.P_SMARTY_VERSION);
		if (functionsMap == null) {
			functionsMap = new HashMap<String, HashMap<String, HashMap<String, String>>>();
		}

		if (Debug.debugFlg || !functionsMap.containsKey(version)) {
			HashMap<String, HashMap<String, String>> currentFunctionsMap = new HashMap<String, HashMap<String, String>>();
			try {
				StreamSource xml = new StreamSource(new StringReader(smartyXml));
				StreamSource xsl = new StreamSource(getClass().getResourceAsStream("smarty_functions.xsl"));
				StringWriter writer = new StringWriter();
				StreamResult result = new StreamResult(writer);
				TransformerFactory factory = TransformerFactory.newInstance();
				Transformer transformer = factory.newTransformer(xsl);
				transformer.setParameter("lang", Locale.getDefault().getLanguage());
				transformer.transform(xml, result);
				BufferedReader reader = new BufferedReader(new StringReader(writer.toString()));
				while (reader.ready()) {
					String name = reader.readLine();
					String param = reader.readLine();
					String space = reader.readLine();
					String block = reader.readLine();
					String desc = reader.readLine();
					String pdesc = reader.readLine();
					String flag = reader.readLine();
					if (name == null || param == null || block == null || space == null || desc == null || pdesc == null || flag == null) {
						break;
					}
					HashMap<String, String> map = new HashMap<String, String>();
					if (!param.equals(""))
						map.put(MAP_PARAM, param);
					if (!space.equals(""))
						map.put(MAP_SPACE, space);
					if (!block.equals(""))
						map.put(MAP_BLOCK, block);
					if (!desc.equals(""))
						map.put(MAP_DESC, desc);
					if (!pdesc.equals(""))
						map.put(MAP_PDESC, pdesc);
					if (!flag.equals(""))
						map.put(MAP_FLAG, flag);
					currentFunctionsMap.put(name, map);
				}
				reader.close();
				functionsMap.put(version, currentFunctionsMap);
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return functionsMap.get(version);
	}

	public HashMap<String, HashMap<String, String>> getModifiersMap() {

		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String version = store.getString(PreferenceConstants.P_SMARTY_VERSION);
		if (modifiersMap == null) {
			modifiersMap = new HashMap<String, HashMap<String, HashMap<String, String>>>();
		}

		if (Debug.debugFlg || !modifiersMap.containsKey(version)) {
			HashMap<String, HashMap<String, String>> currentModifiersMap = new HashMap<String, HashMap<String, String>>();
			try {
				StreamSource xml = new StreamSource(new StringReader(smartyXml));
				StreamSource xsl = new StreamSource(getClass().getResourceAsStream("smarty_modifiers.xsl"));
				StringWriter writer = new StringWriter();
				StreamResult result = new StreamResult(writer);
				TransformerFactory factory = TransformerFactory.newInstance();
				Transformer transformer = factory.newTransformer(xsl);
				transformer.setParameter("lang", Locale.getDefault().getLanguage());
				transformer.transform(xml, result);
				BufferedReader reader = new BufferedReader(new StringReader(writer.toString()));
				while (reader.ready()) {
					String name = reader.readLine();
					String param = reader.readLine();
					String desc = reader.readLine();
					String pdesc = reader.readLine();
					if (name == null || param == null || desc == null || pdesc == null) {
						break;
					}
					HashMap<String, String> map = new HashMap<String, String>();
					if (!param.equals(""))
						map.put(MAP_PARAM, param);
					if (!desc.equals(""))
						map.put(MAP_DESC, desc);
					if (!pdesc.equals(""))
						map.put(MAP_PDESC, pdesc);
					currentModifiersMap.put(name, map);
				}
				reader.close();
				modifiersMap.put(version, currentModifiersMap);
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return modifiersMap.get(version);
	}

	public HashMap<String, String> getVariablesMap() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String version = store.getString(PreferenceConstants.P_SMARTY_VERSION);
		if (variablesMap == null) {
			variablesMap = new HashMap<String, HashMap<String, String>>();
		}

		if (Debug.debugFlg || !variablesMap.containsKey(version)) {
			HashMap<String, String> currentVariablesMap = new HashMap<String, String>();
			try {
				StreamSource xml = new StreamSource(new StringReader(smartyXml));
				StreamSource xsl = new StreamSource(getClass().getResourceAsStream("smarty_variables.xsl"));
				StringWriter writer = new StringWriter();
				StreamResult result = new StreamResult(writer);
				TransformerFactory factory = TransformerFactory.newInstance();
				Transformer transformer = factory.newTransformer(xsl);
				transformer.setParameter("lang", Locale.getDefault().getLanguage());
				transformer.transform(xml, result);
				BufferedReader reader = new BufferedReader(new StringReader(writer.toString()));
				while (reader.ready()) {
					String name = reader.readLine();
					String desc = reader.readLine();
					if (name == null || desc == null) {
						break;
					}
					currentVariablesMap.put(name, desc);
				}
				reader.close();
				variablesMap.put(version, currentVariablesMap);
			} catch (TransformerConfigurationException e) {
				e.printStackTrace();
			} catch (TransformerException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return variablesMap.get(version);
	}

}
