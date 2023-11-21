FROM openjdk:11

COPY target/universityCRM-0.0.1-SNAPSHOT.jar /usr/local/app/universityCRM-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/usr/local/app/universityCRM-0.0.1-SNAPSHOT.jar"]