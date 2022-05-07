package co.com.mimeli.util;

public class MyRegex {

	public boolean validateIp(String ip) {
		String pattern = "((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))";

		if (ip.matches(pattern))
			return true;

		return false;
	}
}
