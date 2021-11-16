package com.juh9870.moremountedstorages;

public final class Utils {
	public static String constructId(String modId, String entryId) {
		return "moremountedstorages:" + modId + "_" + entryId;
	}

	public static <T> T[] arrayOf(T... values) {
		return values;
	}
}
