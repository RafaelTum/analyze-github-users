FROM openjdk:8-jre

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} analize-github-users.jar
EXPOSE 8090
ENTRYPOINT ["java", "-Dspring.data.mongodb.uri=mongodb://mongo:27017/userdb","-jar","/app.jar"]
