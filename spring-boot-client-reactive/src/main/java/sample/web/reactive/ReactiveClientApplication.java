package sample.web.reactive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.reactive.ClientWebRequestBuilder;
import org.springframework.web.client.reactive.WebClient;
import org.springframework.web.client.reactive.support.RxJava1ClientWebRequestBuilder;
import org.springframework.web.client.reactive.support.RxJava1ResponseExtractors;
import rx.Observable;
import sample.web.reactive.config.ReactorClientConfig;

public class ReactiveClientApplication {

	private static final Logger log = LoggerFactory.getLogger(ReactiveClientApplication.class);

	public static void main(String[] args) {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.setDisplayName("ReactiveWebClient");

		context.register(ReactorClientConfig.class);

		context.refresh();
		log.info("Starting up...");
		context.start();

		WebClient webClient = context.getBean(WebClient.class);
		Observable.range(0, 10).flatMap(id -> {
			ClientWebRequestBuilder webRequestBuilder = new RxJava1ClientWebRequestBuilder(HttpMethod.GET,
					"http://localhost:8080/many?id=" + id)
					.accept("text/event-stream");

			Observable<String> ids = webClient.perform(webRequestBuilder)
					.extract(RxJava1ResponseExtractors.bodyStream(String.class));
			return ids;
		}).toBlocking()
				.subscribe(returnedValue -> log.info("Received {}", returnedValue),
							err -> log.error("Error ", err)
				);

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


	}
}
