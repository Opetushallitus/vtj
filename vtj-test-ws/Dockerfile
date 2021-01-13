FROM adoptopenjdk/openjdk11:alpine-slim
RUN adduser --system --disabled-password vtj
USER vtj
COPY target/vtj-test-ws.jar /home/vtj/
EXPOSE 8080
ENTRYPOINT ["java", "-Djavax.net.debug=ssl", "-jar", "/home/vtj/vtj-test-ws.jar"]
