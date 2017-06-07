package cc.easyandroid.easyutils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

 
public final class RegexUtil {
	
	private RegexUtil() {
	}

	public static final String EMAIL_REGEX = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
	public static final String MOBILE_NUM_REGEX = "^(\\+86)?((13[0-9])|(15[^4,\\D])|(18[^4,\\D])|(170))\\d{8}$";
	public static final String QQ_REGEX = "^[1-9][0-9]{4,9}$";
	public static final String PASSWORD_REGEX = "^[1-9a-zA-X]{6,20}$";

	/**
	 * @Title: isPasswordValid
	 * @Description: verify a valid password.
	 * @return boolean
	 * @throws null
	 */
	public static boolean isPasswordValid(String password) {
		return password.matches(PASSWORD_REGEX);
	}
	
	/**
	 * @Title: isEmailValid
	 * @Description: verify a valid email address
	 * @param email
	 * @return boolean
	 * @throws null
	 */
	public static boolean isEmailValid(String email) {
		boolean flag = false;
		flag = email.matches(EMAIL_REGEX);
		return flag;
	}

	/**
	 * @Title: isMobileNumValid
	 * @Description: verify a valid mobile number
	 * @param mobileNum
	 * @return boolean
	 * @throws null
	 */
	public static boolean isMobileNumValid(String mobileNum) {
		boolean flag = false;
		flag = mobileNum.matches(MOBILE_NUM_REGEX);
		return flag;
	}

	/**
	 * @Title: isQQNumValid
	 * @Description: TODO
	 * @param qq
	 * @return boolean
	 * @throws
	 */
	public static boolean isQQNumValid(String qq) {
		boolean flag = false;
		flag = qq.matches(QQ_REGEX);
		return flag;
	}

	/**
	 * @Title: compile
	 * @Description: TODO
	 * @param @param source
	 * @param @param regex
	 * @param @param newValue
	 * @param @return
	 * @return String
	 */
	public static String compile(String source, String regex, String newValue) {
		Pattern patterns = Pattern.compile(regex);
		Matcher matcher = patterns.matcher(source);
		if (matcher.find()) {
			for (int i = 0; i < matcher.groupCount(); i++) {
				String group = matcher.group(i);
				source.replace(group, newValue);
			}
		}
		return source;
	}
}
