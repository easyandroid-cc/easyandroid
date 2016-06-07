package cc.easyandroid.easydb;

/**
 *
 */
public class EasyDbObject<T> {
    private String id;
    private T data;
    private long timestamp;

    public EasyDbObject() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
