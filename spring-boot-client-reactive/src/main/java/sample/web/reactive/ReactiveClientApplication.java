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

		WebClient webClient = context.getBean(WebClient.class);
		ClientWebRequestBuilder webRequestBuilder = new RxJava1ClientWebRequestBuilder(HttpMethod.GET,
				"http://locahost:8080/many")
				.accept("text/event-stream");

		Observable<String> ids = webClient.perform(webRequestBuilder)
				.extract(RxJava1ResponseExtractors.bodyStream(String.class));

        ids.subscribe(id -> log.info("Received {}", id));

		log.info("Starting up...");
		context.start();


	}
}
