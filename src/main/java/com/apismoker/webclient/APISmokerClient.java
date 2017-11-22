package com.apismoker.webclient;

import com.apismoker.config.ConfigReader;
import com.apismoker.config.IConfigReader;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.ext.dropwizard.DropwizardMetricsOptions;
import io.vertx.ext.dropwizard.Match;
import io.vertx.ext.dropwizard.MatchType;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

public class APISmokerClient {
	protected static IConfigReader configReader;

	protected static VertxOptions vertxOptions;

	protected static Vertx vertx;

	protected static HttpClientOptions httpClientOptions;

	protected static WebClient webClient;

	public APISmokerClient() {
		configReader = ConfigReader.INSTANCE;
		vertxOptions = configReader.getVertxOptions();
		httpClientOptions = configReader.getHttpClientOptions();

		vertxOptions.setMetricsOptions(new DropwizardMetricsOptions().setEnabled(true)
				.addMonitoredHttpClientEndpoint(new Match().setValue("some-host:80"))
				.addMonitoredHttpClientEndpoint(new Match().setValue("another-host:.*").setType(MatchType.REGEX)));

		vertx = Vertx.vertx(vertxOptions);
		final WebClientOptions options = new WebClientOptions(httpClientOptions);
		webClient = WebClient.create(vertx, options);
	}

	public HttpClient getHttpClient() {
		httpClientOptions.setSsl(true);
		httpClientOptions.setTrustAll(true);
		httpClientOptions.setVerifyHost(false);
		httpClientOptions.setTryUseCompression(true);
		return vertx.createHttpClient(httpClientOptions);
	}
}