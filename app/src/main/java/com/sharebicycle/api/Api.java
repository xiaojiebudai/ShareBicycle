package com.sharebicycle.api;

public class Api {
	private static final boolean isDebug =true;

	/** 主机地址 */
	public static final String WW_ONLINE = isDebug ? "http://api.51wanj.com"
			: "http://api.szhysy.cn";
	/** Image */
	public static final String WW_ONLINE_IMAGE = isDebug ? "http://img.51wanj.com/"
			: "http://img.szhysy.cn/";
}
