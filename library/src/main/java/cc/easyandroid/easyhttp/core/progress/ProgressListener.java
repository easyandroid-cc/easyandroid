package cc.easyandroid.easyhttp.core.progress;

/**
 * 下载监听接口
 */
public interface ProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}