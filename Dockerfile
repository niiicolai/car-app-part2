FROM ubuntu:22.04

# Set environment variables
ENV JDBC_DATABASE ${{ secrets.JDBC_DATABASE }}
ENV JDBC_DATABASE_URL2 ${{ secrets.JDBC_DATABASE_URL2 }}
ENV JDBC_USERNAME ${{ secrets.JDBC_USERNAME }}
ENV JDBC_PASSWORD ${{ secrets.JDBC_PASSWORD }}

ENV APP_TOKEN_ISSUER ${{ secrets.APP_TOKEN_ISSUER }}
ENV APP_SECRET_KEY ${{ secrets.APP_SECRET_KEY }}

ENV MOTOR_API_URL ${{ secrets.MOTOR_API_URL }}
ENV MOTOR_API_KEY ${{ secrets.MOTOR_API_KEY }}

# Install OpenJDK 17 slim
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk-headless && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY . .

RUN ./mvnw package

EXPOSE 8081

CMD ["sh", "-c", "service mysql start && java -jar target/car-0.0.1-SNAPSHOT.jar"]
