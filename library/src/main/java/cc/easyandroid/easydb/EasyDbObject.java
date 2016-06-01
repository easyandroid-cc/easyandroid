package cc.easyandroid.easydb;

/**
 * Created by Administrator on 2016/6/1.
 */
public class EasyDbObject<T> {
    private long id;
    private T data;
    private long timestamp;

    public EasyDbObject(T data) {
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
