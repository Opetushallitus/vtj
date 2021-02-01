FROM adoptopenjdk/openjdk11:alpine-slim
COPY target/vtj-test-ws.jar /root/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/root/vtj-test-ws.jar"]
