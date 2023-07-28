package top.charjin.oneapi.gateway;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;
import top.charjin.oneapi.common.model.BaseResponse;

@SpringBootApplication
@EnableDubbo
public class OneApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(OneApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route("oneapi_route",
                        r -> r.path("/api/**")
                                .filters(f -> f
                                        .modifyResponseBody(String.class, BaseResponse.class, MediaType.APPLICATION_JSON_VALUE, (exchange, response) -> {
                                            BaseResponse<Object> jsonObject = null;
                                            try {
                                                jsonObject = new Gson().fromJson(response, new TypeToken<BaseResponse<Object>>() {
                                                }.getType());
                                            } catch (JsonSyntaxException e) {
                                                // 如果后端接口的响应格式不匹配则同一成 500 错误码的 BaseResponse 对象
                                                return Mono.just(new BaseResponse<>(200, null, "服务内部响应错误"));
                                            }
                                            return Mono.just(jsonObject);
                                        })
                                )
                                .uri("http://localhost:7089"))
                .build();
    }

}
