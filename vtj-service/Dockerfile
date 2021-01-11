FROM tomcat:8-jdk11-openjdk-slim
COPY DEV-security-context-backend.xml /root/oph-configuration/security-context-backend.xml
COPY DEV-vtj-service.properties /root/oph-configuration/vtj-service.properties
COPY target/vtj-service.war $CATALINA_HOME/webapps/
EXPOSE 8080
CMD ["catalina.sh", "run"]
