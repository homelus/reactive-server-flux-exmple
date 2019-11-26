package study.reactive;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import study.reactive.client.UserClient;
import study.reactive.model.User;

import static org.springframework.web.reactive.function.BodyExtractors.toMono;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
@RequiredArgsConstructor
public class FrontApiApplication {

    private final UserClient userClient;

    public static void main(String[] args) {
        SpringApplication.run(FrontApiApplication.class, args);
    }

    @Bean
    RouterFunction<ServerResponse> routes() {
        return route(POST("/user"), req ->
                req.bodyToMono(User.class)
                .doOnNext(userClient::insert)
                .flatMap(result -> ok().body(Mono.just(result), User.class)));
    }

}
