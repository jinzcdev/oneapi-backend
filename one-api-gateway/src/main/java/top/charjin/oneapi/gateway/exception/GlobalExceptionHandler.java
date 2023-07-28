package top.charjin.oneapi.gateway.exception;

import com.google.gson.Gson;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
@Order(-2)
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        int code = HttpStatus.INTERNAL_SERVER_ERROR.value();

        if (ex instanceof BusinessException) {
            code = ((BusinessException) ex).getCode();
        }

        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> errorAttributes = new HashMap<>();
        errorAttributes.put("code", code);
        errorAttributes.put("data", null);
        errorAttributes.put("message", ex.getMessage());

        String responseBody = new Gson().toJson(errorAttributes);

        return response.writeWith(Mono.just(response.bufferFactory().wrap(responseBody.getBytes())));
    }
}
