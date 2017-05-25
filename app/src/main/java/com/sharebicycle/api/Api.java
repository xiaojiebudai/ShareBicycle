package com.sharebicycle.api;

public class Api {
	private static final boolean isDebug =false;

	/** 主机地址 */
	public static final String WW_ONLINE = isDebug ? "http://api.51wanj.com"
			: "http://api.jsh88.net";
	/** Image */
	public static final String WW_ONLINE_IMAGE = isDebug ? "http://img.51wanj.com/"
			: "http://img.jsh88.net/";
}
