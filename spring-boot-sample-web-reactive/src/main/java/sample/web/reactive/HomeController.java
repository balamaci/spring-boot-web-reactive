package sample.web.reactive;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rx.Observable;

import java.time.Duration;
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
				.doOnNext(val -> log.info("Emitting val {}", val))
				.map(val -> "Val " + val + " id=" + id)
				.take(20);

/*
		return Flux
				.interval(Duration.ofSeconds(1))
				.doOnNext(val -> System.out.println("Emitting" + val))
				.map(val -> "Val" + val + "");
*/
	}

	@GetMapping("/sse-many")
	Flux<ServerSentEvent<String>> sse() {
		return Flux
				.interval(Duration.ofSeconds(1))
				.map(l -> ServerSentEvent
						.builder("foo\nbar")
						.comment("bar\nbaz")
						.id(Long.toString(l))
						.build());
	}

}