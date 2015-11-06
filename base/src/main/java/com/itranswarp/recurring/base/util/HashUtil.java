package com.itranswarp.recurring.base.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

	public static String sha1(String s) {
		MessageDigest md = getMessageDigest("sha-1");
		return bytes2HexString(md.digest(s.getBytes(StandardCharsets.UTF_8)));
	}

	public static String sha256(String s) {
		MessageDigest md = getMessageDigest("sha-256");
		return bytes2HexString(md.digest(s.getBytes(StandardCharsets.UTF_8)));
	}

	static MessageDigest getMessageDigest(String name) {
		try {
			return MessageDigest.getInstance(name);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

	static String bytes2HexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length << 2);
		for (byte x : b) {
			int hi = (x & 0xf0) >> 4;
			int lo = x & 0x0f;
			sb.append(HEX_CHARS[hi]);
			sb.append(HEX_CHARS[lo]);
		}
		return sb.toString();
	}

}
