package com.apismoker.runner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.apismoker.model.APIRequest;
import com.apismoker.processor.AbstractRequestProcessor;
import com.apismoker.processor.RequestProcessor;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class Runner {

	public static void main(String[] args) {
		final Path path = Paths.get("request.json");
		final String key = "body";
		try {
			final String json = new String(Files.readAllBytes(path));
			JsonArray array = new JsonArray(json);
			array.stream().parallel().forEach(r -> {
				JsonObject request = (JsonObject) r;
				final JsonObject body = request.getJsonObject(key, null);
				request.remove(key);
				APIRequest apiRequest = Json.mapper.convertValue(request, APIRequest.class);
				if (null != body)
					apiRequest.setBody(body);
				AbstractRequestProcessor requestProcessor = null;
				try {
					System.out.println("Processing: " + apiRequest.getHttpMethod());
					requestProcessor = new RequestProcessor(apiRequest);
					// long start = System.currentTimeMillis();
					requestProcessor.doRequest().setHandler(resp -> {
						// long end = System.currentTimeMillis() - start;
						if (resp.succeeded()) {
							System.out.println(apiRequest.getUrl() + "::::" + resp.result());
						} else {
							resp.cause().printStackTrace();
						}
					});
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}