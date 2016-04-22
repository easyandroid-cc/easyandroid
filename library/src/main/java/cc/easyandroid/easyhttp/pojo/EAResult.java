package cc.easyandroid.easyhttp.pojo;

public interface EAResult {
    /**
     * 解析成功的标识
     *
     * @return
     */
    boolean isSuccess();

    String getEADesc();

    String getEACode();
}
