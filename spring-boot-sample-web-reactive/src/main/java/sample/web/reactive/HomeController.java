package sample.web.reactive;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

@RestController
public class HomeController {

	private static final Logger log = LoggerFactory.getLogger(HomeController.class);

	@RequestMapping(value = "/")
	public Mono<BootStarter> starter() {
		return Mono.just(new BootStarter("spring-boot-starter-web-reactive", "Spring Boot Web Reactive"));
	}

	@GetMapping(value = "/many")
	public Observable<String> many(@RequestParam String id) {
		return Observable.interval(1, TimeUnit.SECONDS)
				.observeOn(Schedulers.io())
				.doOnNext(val -> log.info("Emitting val {}", val))
				.map(val -> "Val " + val + " id=" + id)
				.take(20);
	}


	@GetMapping(value = "/sse")
	public Observable<ServerSentEvent<String>> sseStream(@RequestParam String id) {
		return Observable.interval(1, TimeUnit.SECONDS)
				.observeOn(Schedulers.io())
				.doOnNext(val -> log.info("Emitting val {}", val))
				.map(val -> ServerSentEvent.<String>builder()
						.id(id + "-" + val)
						.data("val=" + val)
						.build())
				.take(20);

	}
}