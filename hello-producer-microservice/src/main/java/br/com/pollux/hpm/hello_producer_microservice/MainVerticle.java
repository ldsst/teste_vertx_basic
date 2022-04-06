package br.com.pollux.hpm.hello_producer_microservice;

import static io.vertx.core.Vertx.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		Router router = Router.router(vertx);
		router.get("/").handler(
				ctx -> ctx.response().putHeader("content-type", "text/plain; charset=UTF-8").end("Olá Mozaiko!"));

		vertx.createHttpServer().requestHandler(router).listen(8080, http -> {
			if (http.succeeded()) {
				startPromise.complete();

				System.out.println("Servidor HTTP iniciado na porta 8080");
			} else {
				startPromise.fail(http.cause());
			}
		});
	}

	public static void main(String[] args) {
		vertx().deployVerticle(new MainVerticle());
	}
}
