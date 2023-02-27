FROM ubuntu:22.04

# Set environment variables
ENV JDBC_DATABASE=cars
ENV JDBC_DATABASE_URL=jdbc:mysql://localhost:3306/${JDBC_DATABASE}
ENV JDBC_USERNAME $(uuidgen | cut -c1-12)
ENV JDBC_PASSWORD $(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 12)

ENV APP_TOKEN_ISSUER $(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 12)
ENV APP_SECRET_KEY $(head /dev/urandom | tr -dc A-Za-z0-9 | head -c 12)

ENV MOTOR_API_URL ${MOTOR_API_URL}
ENV MOTOR_API_KEY ${MOTOR_API_KEY}

# Install OpenJDK 17 slim
RUN apt-get update && \
    apt-get install -y openjdk-17-jdk-headless && \
    rm -rf /var/lib/apt/lists/*

# Install MySQL
RUN apt-get update && \
    apt-get install -y mysql-server && \
    rm -rf /var/lib/apt/lists/*

# Create cars database
RUN service mysql start && \
    mysql -e "CREATE DATABASE ${JDBC_DATABASE};" && \
    mysql -e "CREATE USER '${JDBC_USERNAME}'@'%' IDENTIFIED BY '${JDBC_PASSWORD}';" && \
    mysql -e "GRANT ALL PRIVILEGES ON cars.* TO '${JDBC_USERNAME}'@'%';" && \
    mysql -e "FLUSH PRIVILEGES;"

WORKDIR /app

VOLUME /var/lib/mysql

COPY . .

RUN ./mvnw package

EXPOSE 8080

CMD ["sh", "-c", "service mysql start && java -jar target/car-0.0.1-SNAPSHOT.jar"]
