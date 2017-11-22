package com.apismoker.config;

import static com.apismoker.constant.Constants.DEPLOYMENT_CONFIG;
import static com.apismoker.constant.Constants.ENVIRONMENT_DEV;
import static com.apismoker.constant.Constants.HTTP_HOST;
import static com.apismoker.constant.Constants.VERTICLE_INSTANCES;
import static com.apismoker.constant.Constants.VERTICLE_PORT;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.apismoker.constant.Constants;

import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.json.JsonObject;

public enum ConfigReader implements IConfigReader {
	INSTANCE;
	protected static JsonObject config;
	protected static final Path path = Paths.get("../APISmoker/src/main/resources/application.json");

	static {
		try {
			final String json = new String(Files.readAllBytes(path));
			final String env = System.getProperty(Constants.ENVIRONMENT, "DEV").toLowerCase();
			ConfigReader.config = new JsonObject(json).getJsonObject(env);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public JsonObject getConfig() {
		return ConfigReader.config;
	}

	@Override
	public VertxOptions getVertxOptions() {
		return new VertxOptions(loadConfig(ConfigReader.config, Constants.VERTX_OPTIONS_CONFIG));
	}

	@Override
	public JsonObject getDeploymentOptions() {
		return loadConfig(ConfigReader.config, DEPLOYMENT_CONFIG);
	}

	@Override
	public int getPort() {
		return ConfigReader.config.getInteger(VERTICLE_PORT, 8080);
	}

	@Override
	public String getHost() {
		return ConfigReader.config.getString(HTTP_HOST, "localhost");
	}

	@Override
	public int getVerticleInstances() {
		return getDeploymentOptions().getInteger(VERTICLE_INSTANCES, 2);
	}

	@Override
	public JsonObject getEnvironment(final String environment) {
		String env = !isBlank(environment) ? environment.trim() : ENVIRONMENT_DEV;
		return ConfigReader.config.getJsonObject(env.toUpperCase(), new JsonObject());
	}

	protected JsonObject loadConfig(final JsonObject obj, String key) {
		return obj.getJsonObject(key, new JsonObject());
	}

	@Override
	public HttpClientOptions getHttpClientOptions() {
		return new HttpClientOptions(loadConfig(ConfigReader.config, Constants.HTTP_OPTIONS_CONFIG));
	}
}