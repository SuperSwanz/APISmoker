package com.apismoker.processor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.apismoker.model.APIRequest;
import com.apismoker.webclient.APISmokerClient;

import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

public abstract class AbstractRequestProcessor implements IRequestProcessor {
	protected final APIRequest apiRequest;
	protected static final HttpClient httpClient = new APISmokerClient().getHttpClient();
	protected final URL url;

	public AbstractRequestProcessor(final APIRequest apiRequest) throws MalformedURLException {
		this.apiRequest = apiRequest;
		this.url = getURL();
	}

	protected HttpMethod getHttpMethod() throws IllegalArgumentException {
		return HttpMethod.valueOf(apiRequest.getHttpMethod());
	}

	protected URL getURL() throws MalformedURLException {
		return new URL(apiRequest.getUrl());
	}

	protected String getCookie() {
		return apiRequest.getCookie();
	}

	protected MultiMap getHeaders() {
		final Map<String, String> requestHeaders = apiRequest.getHeaders();
		MultiMap headers = null;
		if (null != requestHeaders && !requestHeaders.isEmpty()) {
			headers = MultiMap.caseInsensitiveMultiMap();
			for (Entry<String, String> entry : requestHeaders.entrySet()) {
				headers.add(entry.getKey(), entry.getValue());
			}
		}
		return headers;
	}

	protected JsonObject getPayload() {
		final JsonObject body = apiRequest.getBody();
		if (null != body) {
			return body;
		}
		return null;
	}

	protected boolean hasSSL() {
		return "HTTPS".equalsIgnoreCase(this.url.getProtocol());
	}

	protected int getPort() {
		return url.getPort();
	}

	public void doClose() {
		httpClient.close();
	}

	protected JsonObject buildResponse(HttpClientResponse response, Buffer buffer) {
		JsonObject jsonResponse = new JsonObject();
		jsonResponse.put("status", response.statusCode());
		jsonResponse.put("message", response.statusMessage());
		MultiMap multiMap = response.headers();
		Map<String, String> headers = null;
		if (!multiMap.isEmpty()) {
			headers = new HashMap<>();
			for (Entry<String, String> entry : multiMap) {
				headers.put(entry.getKey(), entry.getValue());
			}
		}
		jsonResponse.put("headers", headers);
		jsonResponse.put("cookies", response.cookies());
		jsonResponse.put("httpVersion", response.version());
		jsonResponse.put("content-length", buffer != null ? buffer.length() : 0);
		try {
			jsonResponse.put("response", Json.decodeValue(buffer, Object.class));
		} catch (DecodeException dx) {
			jsonResponse.put("response", buffer != null ? buffer.toString() : "");
		}
		return jsonResponse;

	}
}
