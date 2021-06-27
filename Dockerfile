FROM openjdk:8 AS BUILD_IMAGE
ENV APP_HOME=/opt/build
RUN mkdir -p $APP_HOME/src/main/java
WORKDIR $APP_HOME
COPY build.gradle gradlew gradlew.bat $APP_HOME/
COPY gradle $APP_HOME/gradle
# download dependencies
RUN ./gradlew build -x :bootRepackage -x test --continue
COPY . .
RUN ./gradlew build


FROM openjdk:8-jre
WORKDIR /root/
COPY --from=BUILD_IMAGE /opt/build/build/libs/velocityPreview-0.0.1.jar app.jar
EXPOSE 8080
CMD ["java","-jar","app.jar"]
