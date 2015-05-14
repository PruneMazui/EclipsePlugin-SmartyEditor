package com.github.PruneMazui.SmartyEditor;

public class Debug {
	public static Boolean debugFlg = false;

	public static void consoleOut(String message) {
		if (debugFlg) {
			System.out.println(message);
		}
	}
}
