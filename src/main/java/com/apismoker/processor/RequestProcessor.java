package com.apismoker.processor;

import java.net.MalformedURLException;

import com.apismoker.model.APIRequest;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;

public class RequestProcessor extends AbstractRequestProcessor {

	public RequestProcessor(APIRequest apiRequest) throws MalformedURLException {
		super(apiRequest);
	}

	@Override
	public Future<JsonObject> doRequest() {
		Future<JsonObject> future = Future.future();
		final boolean hasSSL = hasSSL();
		int port = getPort();
		port = !hasSSL ? port : 443;
		HttpClientRequest request = httpClient.requestAbs(getHttpMethod(), url.toString(), resp -> {
			resp.bodyHandler(buffer -> {
				future.complete(buildResponse(resp, buffer));
			});
		});
		if (null != getPayload()) {
			String payload = getPayload().toString();
			Buffer buffer = Buffer.buffer(payload.getBytes());
			request.headers().add(HttpHeaders.CONTENT_LENGTH, buffer.length() + "");
			request.write(buffer);
		}
		if (null != getHeaders()) {
			request.headers().addAll(getHeaders());
		}
		if (null != getCookie()) {
			request.putHeader("Cookie", getCookie());
		}
		request.exceptionHandler(throwable -> {
			future.fail(throwable.getMessage());
		});
		request.end();
		return future;
	}
}
