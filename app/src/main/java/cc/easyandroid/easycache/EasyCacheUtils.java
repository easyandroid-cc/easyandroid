package cc.easyandroid.easycache;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import cc.easyandroid.easycache.dislrucache.DiskLruCache;

/**
 * 缓存辅助类
 * 
 * @author cgpllx1@qq.com
 *
 */
public class EasyCacheUtils {
	private static final String DIR_NAME = "diskCache";
	private static final int MAX_SIZE = 50 * 1024 * 1024;// 50m
	private static final int DEFAULT_CACHE_VERSION = 1;// 他升级将会删除缓存
	private static final int DEFAULT_VALUECOUNT = 1;

	public static final String TAG = EasyCacheUtils.class.getSimpleName();

	protected final DiskLruCache mDiskLruCache;

	public EasyCacheUtils(Context context, int cacheVersion) throws IOException {
		this(context, DIR_NAME, cacheVersion);
	}

	public EasyCacheUtils(Context context) throws IOException {
		this(context, DEFAULT_CACHE_VERSION);
	}

	public EasyCacheUtils(Context context, String dirName, int cacheVersion) throws IOException {
		mDiskLruCache = generateCache(context, dirName, cacheVersion, DEFAULT_VALUECOUNT, MAX_SIZE);
	}

	private DiskLruCache generateCache(Context context, String dirName, int cacheVersion, int valueCount, long maxSize) throws IOException {
		DiskLruCache diskLruCache = DiskLruCache.open(CacheUtils.getDiskCacheDir(context, dirName), //
				cacheVersion, valueCount, maxSize);
		return diskLruCache;
	}

	// =======================================
	// ============== String 数据 读写 =============
	// =======================================

	public void put(String key, String value) {
		DiskLruCache.Editor edit = null;
		BufferedWriter bw = null;
		try {
			edit = editor(key);
			if (edit == null)
				return;
			OutputStream os = edit.newOutputStream(0);
			bw = new BufferedWriter(new OutputStreamWriter(os));
			bw.write(value);
			edit.commit();// write CLEAN
		} catch (IOException e) {
			e.printStackTrace();
			try {
				// s
				edit.abort();// write REMOVE
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				if (bw != null)
					bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getAsString(String key) {
		InputStream inputStream = null;
		try {
			// write READ
			inputStream = get(key);
			if (inputStream == null)
				return null;
			StringBuilder sb = new StringBuilder();
			int len = 0;
			byte[] buf = new byte[128];
			while ((len = inputStream.read(buf)) != -1) {
				sb.append(new String(buf, 0, len));
			}
			return sb.toString();

		} catch (IOException e) {
			e.printStackTrace();
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		}
		return null;
	}

	public void put(String key, JSONObject jsonObject) {
		put(key, jsonObject.toString());
	}

	public JSONObject getAsJson(String key) {
		String val = getAsString(key);
		try {
			if (val != null)
				return new JSONObject(val);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	// =======================================
	// ============ JSONArray 数据 读写 =============
	// =======================================

	public void put(String key, JSONArray jsonArray) {
		put(key, jsonArray.toString());
	}

	public JSONArray getAsJSONArray(String key) {
		String JSONString = getAsString(key);
		try {
			JSONArray obj = new JSONArray(JSONString);
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// =======================================
	// ============== byte 数据 读写 =============
	// =======================================

	/**
	 * 保存 byte数据 到 缓存中
	 *
	 * @param key
	 *            保存的key
	 * @param value
	 *            保存的数据
	 */
	public void put(String key, byte[] value) {
		OutputStream out = null;
		DiskLruCache.Editor editor = null;
		try {
			editor = editor(key);
			if (editor == null) {
				return;
			}
			out = editor.newOutputStream(0);
			out.write(value);
			out.flush();
			editor.commit();// write CLEAN
		} catch (Exception e) {
			e.printStackTrace();
			try {
				editor.abort();// write REMOVE
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// =======================================
	// ============== InputStream 数据 读写 =============
	// =======================================
	/**
	 * 保存 InputStream数据流 到 缓存中
	 *
	 * @param key
	 *            保存的key
	 * @param value
	 *            保存的数据
	 */
	public int put(String key, InputStream value) {
		int count = -1;
		OutputStream out = null;
		DiskLruCache.Editor editor = null;
		try {
			editor = editor(key);
			if (editor == null) {
				return count;
			}
			out = editor.newOutputStream(0);
			count = copy(value, out);
			out.flush();
			editor.commit();// write CLEAN
		} catch (Exception e) {
			e.printStackTrace();
			try {
				editor.abort();// write REMOVE
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return count;
	}

	public int copy(InputStream input, OutputStream output) throws IOException {
		long count = copyLarge(input, output);
		if (count > 2147483647L) {// 不能大于2G
			return -1;
		}
		return (int) count;
	}

	public long copyLarge(InputStream input, OutputStream output) throws IOException {
		return copyLarge(input, output, new byte[256]);
	}

	public long copyLarge(InputStream input, OutputStream output, byte[] buffer) throws IOException {
		long count = 0L;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
			System.out.println("copyLarge="+count);
		}
		return count;
	}

	public byte[] getAsBytes(String key) {
		byte[] res = null;
		InputStream is = get(key);
		if (is == null)
			return null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			byte[] buf = new byte[256];
			int len = 0;
			while ((len = is.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			res = baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	// =======================================
	// ============== 序列化 数据 读写 =============
	// =======================================
	public void put(String key, Serializable value) {
		DiskLruCache.Editor editor = editor(key);
		ObjectOutputStream oos = null;
		if (editor == null)
			return;
		try {
			OutputStream os = editor.newOutputStream(0);
			oos = new ObjectOutputStream(os);
			oos.writeObject(value);
			oos.flush();
			editor.commit();
		} catch (IOException e) {
			e.printStackTrace();
			try {
				editor.abort();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				if (oos != null)
					oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T getAsSerializable(String key) {
		T t = null;
		InputStream is = get(key);
		ObjectInputStream ois = null;
		if (is == null)
			return null;
		try {
			ois = new ObjectInputStream(is);
			t = (T) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (ois != null)
					ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return t;
	}

	// =======================================
	// ============== bitmap 数据 读写 =============
	// =======================================
	public void put(String key, Bitmap bitmap) {
		put(key, CacheUtils.bitmap2Bytes(bitmap));
	}

	public Bitmap getAsBitmap(String key) {
		byte[] bytes = getAsBytes(key);
		if (bytes == null)
			return null;
		return CacheUtils.bytes2Bitmap(bytes);
	}

	// =======================================
	// ============= drawable 数据 读写 =============
	// =======================================
	public void put(String key, Drawable value) {
		put(key, CacheUtils.drawable2Bitmap(value));
	}

	public Drawable getAsDrawable(String key) {
		byte[] bytes = getAsBytes(key);
		if (bytes == null) {
			return null;
		}
		return CacheUtils.bitmap2Drawable(CacheUtils.bytes2Bitmap(bytes));
	}

	// =======================================
	// ============= other methods =============
	// =======================================
	public boolean remove(String key) {
		try {
			key = CacheUtils.hashKeyForDisk(key);
			return mDiskLruCache.remove(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void close() throws IOException {
		mDiskLruCache.close();
	}

	public void delete() throws IOException {
		mDiskLruCache.delete();
	}

	public void flush() throws IOException {
		mDiskLruCache.flush();
	}

	public boolean isClosed() {
		return mDiskLruCache.isClosed();
	}

	public long size() {
		return mDiskLruCache.size();
	}

	public void setMaxSize(long maxSize) {
		mDiskLruCache.setMaxSize(maxSize);
	}

	public File getDirectory() {
		return mDiskLruCache.getDirectory();
	}

	public long getMaxSize() {
		return mDiskLruCache.getMaxSize();
	}

	// =======================================
	// ===遇到文件比较大的，可以直接通过流读写 =====
	// =======================================
	// basic editor
	public DiskLruCache.Editor editor(String key) {
		try {
			key = CacheUtils.hashKeyForDisk(key);
			// wirte DIRTY
			DiskLruCache.Editor edit = mDiskLruCache.edit(key);
			// edit maybe null :the entry is editing
			if (edit == null) {
				Log.w(TAG, "the entry spcified key:" + key + " is editing by other . ");
			}
			return edit;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	// basic get
	public InputStream get(String key) {
		try {
			DiskLruCache.Snapshot snapshot = mDiskLruCache.get(CacheUtils.hashKeyForDisk(key));
			if (snapshot == null) // not find entry , or entry.readable = false
			{
				Log.e(TAG, "not find entry , or entry.readable = false");
				return null;
			}
			// write READ
			return snapshot.getInputStream(0);

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

}
