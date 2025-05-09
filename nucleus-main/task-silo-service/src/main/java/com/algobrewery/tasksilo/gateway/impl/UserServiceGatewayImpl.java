package com.algobrewery.tasksilo.gateway.impl;

import com.algobrewery.tasksilo.exceptions.GatewayTimeoutException;
import com.algobrewery.tasksilo.exceptions.InvalidRequestError;
import com.algobrewery.tasksilo.exceptions.NotFoundException;
import com.algobrewery.tasksilo.exceptions.ServiceException;
import com.algobrewery.tasksilo.gateway.UserServiceGateway;
import com.algobrewery.tasksilo.model.gateway.userservice.GetUserResponse;
import com.algobrewery.tasksilo.model.internal.InternalRequestContext;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import static com.algobrewery.constants.HeaderConstants.APP_CLIENT_USER_SESSION_UUID;
import static com.algobrewery.constants.HeaderConstants.APP_ORG_UUID;
import static com.algobrewery.constants.HeaderConstants.APP_REGION_ID;
import static com.algobrewery.constants.HeaderConstants.APP_TRACE_ID;
import static com.algobrewery.constants.HeaderConstants.APP_USER_UUID;

@Slf4j
@Component("UserServiceGatewayImpl")
public class UserServiceGatewayImpl implements UserServiceGateway {

    @Getter
    public enum Operations {
        GET_BY_USER_UUID("/user/{userId}");

        private final String resourcePath;

        Operations(String resourcePath) {
            this.resourcePath = resourcePath;
        }
    }

    private final WebClient webClient;
    private final Duration timeout;

    public UserServiceGatewayImpl(
            WebClient.Builder webClientBuilder,
            @Value("${outbounds.user-service.url}") String userServiceUrl,
            @Value("${outbounds.user-service.timeout:5000}") Long timeoutMillis) {

        this.webClient = webClientBuilder
                .baseUrl(userServiceUrl)
                .build();
        this.timeout = Duration.ofMillis(timeoutMillis);
    }

    @Override
    public CompletableFuture<String> getUser(InternalRequestContext requestContext, String userUuid) {
        if (Objects.isNull(requestContext)) {
            return CompletableFuture.failedFuture(new InvalidRequestError("RequestContext cannot be null"));
        }

        if (Objects.isNull(userUuid) || userUuid.isBlank()) {
            return CompletableFuture.failedFuture(new InvalidRequestError("User UUID cannot be null or empty"));
        }

        log.debug("Invoking user-service for requestContext:{} and userUuid: {}", requestContext, userUuid);

        final long startTime = System.currentTimeMillis();

        // TODO - remove once user-management-service is stable
        // return CompletableFuture.completedFuture(userUuid);

        return webClient.get()
                .uri(Operations.GET_BY_USER_UUID.getResourcePath(), userUuid)
                .header(APP_USER_UUID, requestContext.getAppUserUuid())
                .header(APP_ORG_UUID, requestContext.getAppOrgUuid())
                .header(APP_CLIENT_USER_SESSION_UUID, requestContext.getAppClientUserSessionUuid())
                .header(APP_TRACE_ID, requestContext.getTraceId())
                .header(APP_REGION_ID, requestContext.getRegionId())
                .retrieve()
                .bodyToMono(GetUserResponse.class)
                .map(response -> {
                    log.debug("Received successful response from user-service: {}", response);
                    return response.getUserId();
                })
                .timeout(timeout)
                .doOnSuccess(userId -> {
                    long duration = System.currentTimeMillis() - startTime;
                    log.info("Successfully retrieved user in {}ms. UserId: {}", duration, userId);
                })
                .onErrorResume(WebClientResponseException.class, ex -> {
                    long duration = System.currentTimeMillis() - startTime;
                    log.error("User service returned {} in {}ms. Response: {}",
                            ex.getStatusCode(), duration, ex.getResponseBodyAsString(), ex);
                    if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new NotFoundException("User not found: " + userUuid));
                    }
                    return Mono.error(ex);
                })
                .onErrorResume(TimeoutException.class, ex -> {
                    long duration = System.currentTimeMillis() - startTime;
                    log.error("User service timeout after {}ms", duration, ex);
                    return Mono.error(new GatewayTimeoutException("User service timeout: " + ex.getMessage()));
                })
                .onErrorResume(Exception.class, ex -> {
                    long duration = System.currentTimeMillis() - startTime;
                    log.error("Unexpected error after {}ms", duration, ex);
                    return Mono.error(new ServiceException("User service error: " + ex.getMessage()));
                })
                .doFinally(signal -> {
                    long duration = System.currentTimeMillis() - startTime;
                    log.debug("Completed user service call with signal {} in {}ms", signal, duration);
                })
                .toFuture();
    }

}
