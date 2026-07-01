FROM eclipse-temurin:17-jdk

EXPOSE 9999

ADD target/portail-0.0.1-SNAPSHOT.jar application.jar

ENTRYPOINT ["java", "-jar", "/application.jar"]
