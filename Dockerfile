FROM openjdk:17-oracle

WORKDIR /app

COPY ./one-api-backend-0.0.1-SNAPSHOT.jar /app/
COPY ./one-api-interface-0.0.1-SNAPSHOT.jar /app/
COPY ./one-api-gateway-0.0.1-SNAPSHOT.jar /app/
COPY ./nacos /app/nacos

EXPOSE 8101 8090 7089

ENV SPRING_PROFILES_ACTIVE prod

# 设置默认执行命令
CMD ["sh", "-c", \
    "cd /app/nacos/bin && sh startup.sh -m standalone && cd /app && java -jar one-api-backend-0.0.1-SNAPSHOT.jar & java -jar one-api-interface-0.0.1-SNAPSHOT.jar & java -jar one-api-gateway-0.0.1-SNAPSHOT.jar"]
