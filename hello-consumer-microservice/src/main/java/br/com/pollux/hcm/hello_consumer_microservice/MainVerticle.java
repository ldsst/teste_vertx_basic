package br.com.pollux.hcm.hello_consumer_microservice;

import static io.vertx.core.Vertx.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class MainVerticle extends AbstractVerticle {

	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		Router router = Router.router(vertx);
		router.get("/").handler(ctx -> {
			WebClient client = WebClient.create(vertx);
			
			client.get(8080, "localhost", "/").send(ar -> {
				if (ar.succeeded()) {
					HttpResponse<Buffer> response = ar.result();
					
					System.out.println("Response recebido com o status " + response.statusCode());
					ctx.response().putHeader("content-type", "text/plain; charset=UTF-8").end(response.bodyAsString());
				} else {
					System.out.println("Algo deu errado " + ar.cause().getMessage());
				}
			});
		});
		
		vertx.createHttpServer().requestHandler(router).listen(8081, http -> {
			if (http.succeeded()) {
				startPromise.complete();

				System.out.println("Servidor HTTP iniciado na porta 8081");
			} else {
				startPromise.fail(http.cause());
			}
		});
	}

	public static void main(String[] args) {
		vertx().deployVerticle(new MainVerticle());
	}

}