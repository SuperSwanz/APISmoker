package com.apismoker.verticle;

import java.util.HashSet;
import java.util.Set;

import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.rxjava.core.http.HttpServer;
import io.vertx.rxjava.ext.web.Router;
import io.vertx.rxjava.ext.web.handler.CorsHandler;
import rx.Single;

public class BaseVerticle extends io.vertx.rxjava.core.AbstractVerticle {
	protected Router router;
	protected final int port = 8080;
	protected final String host = "localhost";

	@Override
	public void start() throws Exception {
		
	}

	@Override
	public void init(io.vertx.core.Vertx vertx, Context context) {
		super.init(vertx, context);
		buildRouter();
	}

	@Override
	public void stop(Future<Void> stopFuture) throws Exception {
		super.stop(stopFuture);
	}

	protected void buildRouter() {
		this.router = Router.router(this.vertx).exceptionHandler((error -> {
			System.out.println("Routers not injected: " + error.getMessage());
		}));
	}

	protected Single<HttpServer> createHttpServer(HttpServerOptions httpOptions, Router router, String host, int port) {
		Single<HttpServer> server = vertx.createHttpServer()
										.requestHandler(router::accept)
										.rxListen(this.port, this.host);
		return server;
	}

	protected void enableCorsSupport(Router router) {
		Set<String> allowHeaders = new HashSet<>();
		allowHeaders.add("x-requested-with");
		allowHeaders.add("Access-Control-Allow-Origin");
		allowHeaders.add("origin");
		allowHeaders.add("Content-Type");
		allowHeaders.add("accept");
		router.route()
				.handler(CorsHandler.create("*").allowedHeaders(allowHeaders).allowedMethod(HttpMethod.GET)
						.allowedMethod(HttpMethod.POST).allowedMethod(HttpMethod.PUT).allowedMethod(HttpMethod.DELETE)
						.allowedMethod(HttpMethod.PATCH).allowedMethod(HttpMethod.OPTIONS));
	}

}
