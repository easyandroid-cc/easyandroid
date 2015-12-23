package cc.easyandroid.easyhttp.core.retrofit;

/** Exception for an unexpected, non-2xx HTTP response. */
public final class KHttpException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5299080305687496721L;
	private final int code;
	private final String message;
	private final transient Response<?> response;

	public KHttpException(Response<?> response) {
		super("HTTP " + response.code() + " " + response.message());
		this.code = response.code();
		this.message = response.message();
		this.response = response;
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
	 * The full HTTP response. This may be null if the exception was serialized.
	 */
	public Response<?> response() {
		return response;
	}
}
