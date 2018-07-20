FROM openjdk:8
ADD build/libs/service-1.0-SNAPSHOT.jar service.jar
EXPOSE 8080
ENV DB_HOST=, DB_PASSWORD=hbstudent
ENTRYPOINT ["java", "-jar", "service.jar"]