package cc.easyandroid.easyutils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class ObjectUtil {
	public static boolean isEmpty(Object object) {
		if (object == null) {
			return true;
		}
		return false;
	}

	public static boolean isEmpty(Object... objects) {
		if (objects == null) {
			return true;
		} else {
			for (Object object : objects) {
				if (isEmpty(object)) {
					return true;
				}
			}
		}
		return false;
	}

	private static void close(OutputStream os) {
		try {
			if (os != null) {
				os.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static byte[] objectToByteArray(Object pojo) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(pojo);
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(baos);
			close(oos);
		}
		return null;
	}

	private static void close(InputStream os) {
		try {
			if (os != null) {
				os.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Object byteArray2Object(byte[] data) {
		Object obj = null;
		ObjectInputStream bis = null;
		ByteArrayInputStream bais = null;
		try {
			bais = new ByteArrayInputStream(data);
			bis = new ObjectInputStream(bais);
			obj = bis.readObject();
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(bis);
			close(bais);
		}
		return null;
	}
}
