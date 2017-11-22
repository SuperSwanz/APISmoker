package com.apismoker.verticle;

import io.vertx.core.Future;
import io.vertx.rxjava.core.http.HttpServer;
import rx.Single;

public class MainVerticle extends BaseVerticle {

	@Override
	public void start(Future<Void> startFuture) {
		try {
			super.start();
			final int port = this.port;
			final String host = this.host;
			Single<HttpServer> server = createHttpServer(null, router, host, port);
			server.subscribe(result -> {
				startFuture.complete();
				System.out.println("Vertx initialized");
			});
		} catch (Exception ex) {
			System.out.println("Exception while starting BulldozerVerticle: " + ex.getMessage());
		}
	}
}