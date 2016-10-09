package cc.easyandroid.easycache;

import android.content.Context;

import java.io.UnsupportedEncodingException;

import cc.easyandroid.easycache.volleycache.Cache;
import cc.easyandroid.easycache.volleycache.DiskBasedCache;
import cc.easyandroid.easycore.EAResult;
import cc.easyandroid.easylog.EALog;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * 缓存http的响应体
 */
public class EasyHttpCache {
    private static EasyHttpCache easyHttpCache;

    private EasyHttpCache() {
    }

    void setCache(Cache valleyCache) {
        this.cache = valleyCache;
    }

    private Cache cache;

    public static EasyHttpCache getInstance() {
        if (easyHttpCache == null) {
            synchronized (EasyHttpCache.class) {
                if (easyHttpCache == null) {
                    easyHttpCache = new EasyHttpCache();
                }
            }
        }
        return easyHttpCache;
    }

    /**
     * 使用前先进行初始化
     *
     * @param context context
     */
    public void initialize(Context context) {
        Cache valleyCache = new DiskBasedCache(CacheUtils.getDiskCacheDir(context.getApplicationContext(), "volleycache"));
        valleyCache.initialize();
        setCache(valleyCache);
    }

    public void put(Request request, Object object, byte[] data) throws UnsupportedEncodingException {
        parseCache(request, object, data, "application/json; charset=UTF-8");
    }

    public ResponseBody get(Request request) {
        checkNull(cache);
        Cache.Entry entry = cache.get(request.url().toString());// 充缓存中获取entry
        if (entry == null) {
            return null;
        }
        if (entry.isExpired()) {// 缓存过期了
            return null;
        }
        if (entry.data != null) {// 如果有数据就使用缓存
            MediaType contentType = MediaType.parse(entry.mimeType);
            byte[] bytes = entry.data;
            try {
                return ResponseBody.create(contentType, bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //将结果保存到cache中
    private void parseCache(Request request, Object object, byte[] data, String mimeType) throws UnsupportedEncodingException {
        okhttp3.CacheControl cacheControl = request.cacheControl();
        if (cacheControl != null) {
            if (!cacheControl.noCache() && !cacheControl.noStore()) {
                if (chechCanSave(object)) {
                    long now = System.currentTimeMillis();
                    long maxAge = cacheControl.maxAgeSeconds();
                    long softExpire = now + maxAge * 1000;
                    EALog.d("easycache When long: %1$s秒", (softExpire - now) / 1000 + "");
                    Cache.Entry entry = new Cache.Entry();
                    entry.softTtl = softExpire;
                    entry.ttl = entry.softTtl;
                    entry.mimeType = mimeType;
                    entry.data = data;
                    checkNull(cache);
                    cache.put(request.url().toString(), entry);
                }
            }
        }
    }

    boolean chechCanSave(Object object) {
        if (object != null && object instanceof EAResult) {
            EAResult kResult = (EAResult) object;
            if (kResult.isSuccess()) {
                return true;
            }
        }
        if (object != null && object instanceof String) {
            return true;
        }
        return false;
    }

    private void checkNull(Cache cache) {
        if (cache == null) {
            throw new IllegalArgumentException("Please call first initialize method");
        }

    }
}
