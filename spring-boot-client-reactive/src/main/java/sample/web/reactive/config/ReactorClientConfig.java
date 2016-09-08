package sample.web.reactive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.reactive.WebClient;

/**
 * @author sbalamaci
 */
@Configuration
public class ReactorClientConfig {

    @Bean
    public WebClient reactorClient() {
        ClientHttpConnector httpConnector = new ReactorClientHttpConnector();
        return new WebClient(httpConnector);
    }

}
