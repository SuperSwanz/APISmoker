package com.apismoker.handler;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.web.RoutingContext;

public abstract class BaseHandler implements Handler<RoutingContext> {

	@Override
	public void handle(RoutingContext ctx) {
		ctx.response().end(new JsonObject().put("status", "Ok").toString());
	}
}
