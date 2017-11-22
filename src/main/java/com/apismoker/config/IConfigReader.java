package com.apismoker.config;

import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.json.JsonObject;

public interface IConfigReader {

	public JsonObject getConfig();

	public VertxOptions getVertxOptions();
	
	public HttpClientOptions getHttpClientOptions();

	public JsonObject getDeploymentOptions();

	public int getPort();

	public String getHost();

	public int getVerticleInstances();

	public JsonObject getEnvironment(String environment);

}
