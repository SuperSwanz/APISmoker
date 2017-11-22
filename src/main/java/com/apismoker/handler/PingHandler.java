package com.apismoker.handler;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.ext.web.RoutingContext;

public class PingHandler extends BaseHandler {

	@Override
	public void handle(RoutingContext ctx) {
		ctx.response().end(new JsonObject().put("status", "Ok").toString());
	}
}