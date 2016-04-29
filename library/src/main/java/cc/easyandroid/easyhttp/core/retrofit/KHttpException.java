package cc.easyandroid.easyhttp.core.retrofit;

/** Exception for an unexpected, non-2xx HTTP easyResponse. */
public final class KHttpException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5299080305687496721L;
	private final int code;
	private final String message;
	private final transient EasyResponse<?> easyResponse;

	public KHttpException(EasyResponse<?> easyResponse) {
		super("HTTP " + easyResponse.code() + " " + easyResponse.message());
		this.code = easyResponse.code();
		this.message = easyResponse.message();
		this.easyResponse = easyResponse;
	}

	/** HTTP status code. */
	public int code() {
		return code;
	}

	/** HTTP status message. */
	public String message() {
		return message;
	}

	/**
	 * The full HTTP easyResponse. This may be null if the exception was serialized.
	 */
	public EasyResponse<?> response() {
		return easyResponse;
	}
}
