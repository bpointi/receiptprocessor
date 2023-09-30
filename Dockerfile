FROM eclipse-temurin:17
VOLUME /tmp
COPY build/libs/*.jar receiptprocessor-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/receiptprocessor-0.0.1-SNAPSHOT.jar"]
