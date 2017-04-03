package com.cylee.game.util;

import com.badlogic.gdx.utils.Logger;

public class Log {
	private static final Logger mLog = new Logger(Log.class.getSimpleName(), Logger.DEBUG);
	public static void d(String message) {
		mLog.debug(message);
	}
	public static void e(String message) {
		mLog.error(message);
	}
}
