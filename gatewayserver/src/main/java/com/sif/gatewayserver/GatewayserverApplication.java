package com.sif.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	@Bean
	public RouteLocator gatewayRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
				.route(p -> p
						.path("/bank/accounts/**")
						.filters( f -> f.rewritePath("/bank/accounts/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
								.circuitBreaker(c -> c.setName("ACCOUNTS-CB")
										.setFallbackUri("forward:/fallback/contact-info"))
								.retry(retryConfig -> retryConfig.setRetries(2).setMethods(HttpMethod.GET)
										.setBackoff(Duration.ofMillis(200), Duration.ofSeconds(1), 3, true))
						)
						.uri("lb://ACCOUNTS"))
				.route(p -> p
						.path("/bank/loans/**")
						.filters( f -> f.rewritePath("/bank/loans/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
						.uri("lb://LOANS"))
				.route(p -> p
						.path("/bank/cards/**")
						.filters( f -> f.rewritePath("/bank/cards/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
						.uri("lb://CARDS")).build();
	}

}
