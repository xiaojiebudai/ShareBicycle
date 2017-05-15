package com.sharebicycle.utils;

import com.alibaba.fastjson.JSONObject;
import com.sharebicycle.MyApplication;


import org.xutils.http.RequestParams;

public class ParamsUtils {
	/**
	 * post传json
	 * 
	 * @param jsonObject
	 * @param url
	 * @return
	 */
	public static RequestParams getPostJsonParams(JSONObject jsonObject,
                                                  String url) {
		RequestParams params = new RequestParams(url);
		params.setAsJsonContent(true);
		params.setBodyContent(jsonObject.toString());
		return params;
	}
	/** 只带session参数的请求 */
	public static RequestParams getSessionParams(String api) {
		RequestParams params = new RequestParams(api);
		params.addQueryStringParameter(Consts.KEY_SESSIONID, MyApplication
				.getInstance().getSessionId());
		return params;
	}
}
