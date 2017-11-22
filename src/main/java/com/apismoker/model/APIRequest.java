package com.apismoker.model;

import java.util.Map;

import io.vertx.core.json.JsonObject;

public class APIRequest {
	private String url;
	private String httpMethod;
	private Map<String, String> headers;
	private JsonObject body;
	private String cookie;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public JsonObject getBody() {
		return body;
	}

	public void setBody(JsonObject body) {
		this.body = body;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
}