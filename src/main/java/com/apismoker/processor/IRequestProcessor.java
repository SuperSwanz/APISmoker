package com.apismoker.processor;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface IRequestProcessor {

	public Future<JsonObject> doRequest();
}
