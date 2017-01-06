package com.wteam.mixin.utils;

/**
 * 验证字符串的编码格式.
 *
 * @version 1.0
 * @author benko
 * @time 2016-1-18 17:17:47
 *
 */
public class EncodeUtils {

	public enum Charset {
		GB2312("GB2312"),ISO_8859_1("ISO-8859-1"),UTF_8("UTF-8"),GBK("GBK");

		private String encode;

		private Charset(String encode) {
			this.encode = encode;
		}
		public String encode() {
			return encode;
		}
	};


	/**
	 * 验证字符串的编码格式.
	 * @param str
	 * @return
	 */
	public static Charset getEncoding(String str) {

		for (Charset type : Charset.values()) {
			String encode = type.encode();
			try {
				if (str.equals(new String(str.getBytes(encode), encode))) {
					return type;
				}
			} catch (Exception exception) {
			}
		}

		return null;
	}
}
