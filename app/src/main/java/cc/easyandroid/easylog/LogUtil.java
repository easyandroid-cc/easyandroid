package cc.easyandroid.easylog;

import android.util.Log;

public class LogUtil {
	private static LogMode m = LogMode.DEVELOP;

	private LogUtil() {
		// TODO Auto-generated constructor stub
	}

	public static void initMode(LogMode mode) {
		m = mode;
	}

	public static void verbose(String tag, String msg) {
		if (m == LogMode.DEVELOP) {
			Log.v(tag, msg);
		}
	}

	public static void verbose(String tag, String msg, Throwable tr) {
		if (m == LogMode.DEVELOP) {
			Log.v(tag, msg, tr);
		}
	}

	public static void debug(String tag, String msg) {
		if (m == LogMode.DEVELOP) {
			Log.d(tag, msg);
		}
	}

	public static void debug(String tag, String msg, Throwable tr) {
		if (m == LogMode.DEVELOP) {
			Log.d(tag, msg, tr);
		}
	}

	public static void info(String tag, String msg) {
		if (m == LogMode.DEVELOP) {
			Log.i(tag, msg);
		}
	}

	public static void info(String tag, String msg, Throwable tr) {
		if (m == LogMode.DEVELOP) {
			Log.i(tag, msg, tr);
		}
	}

	public static void warn(String tag, String msg) {
		if (m == LogMode.DEVELOP) {
			Log.w(tag, msg);
		}
	}

	public static void warn(String tag, String msg, Throwable tr) {
		if (m == LogMode.DEVELOP) {
			Log.w(tag, msg, tr);
		}
	}

	public static void error(String tag, String msg) {
		if (m == LogMode.DEVELOP) {
			Log.e(tag, msg);
		}
	}

	public static void error(String tag, String msg, Throwable tr) {
		if (m == LogMode.DEVELOP) {
			Log.e(tag, msg, tr);
		}
	}

	public static enum LogMode {
		RELEASE, DEVELOP
	}
}
