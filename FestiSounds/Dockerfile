FROM eclipse-temurin:17-jdk-jammy as base
WORKDIR /FestiSounds
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve
COPY src ./src

FROM base as development
CMD ["./mvnw", "spring-boot:run" ,"-Dspring-boot.run.jvmArguments='-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000'"]

FROM base as build
RUN ./mvnw package

FROM eclipse-temurin:17-jre-jammy as production
EXPOSE 8080
COPY --from=build /FestiSounds/target/*.jar /*.jar
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/*.jar"]