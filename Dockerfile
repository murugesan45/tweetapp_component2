FROM openjdk:8-jdk-alpine
EXPOSE 8080
ADD target/*.jar tweetapp.jar
ENTRYPOINT ["sh","-c","java -jar /tweetapp.jar"]
