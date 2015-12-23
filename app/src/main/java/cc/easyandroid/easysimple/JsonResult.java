package cc.easyandroid.easysimple;

import cc.easyandroid.easyhttp.pojo.EAResult;

/**
 * 用来解析登录的返回结果
 * 
 * @author Administrator
 * @param <T>
 *
 * @param <T>
 * 
 */
public class JsonResult<T> implements EAResult {

	private String code;
	private String desc;
	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public boolean isSuccess() {
		return "C0000".equals(code);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String getFailureDesc() {
		return desc;
	}

}
