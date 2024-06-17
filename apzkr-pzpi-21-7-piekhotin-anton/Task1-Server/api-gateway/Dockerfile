# Використовуємо за базу офіційний образ Maven для збирання додатків Spring Boot
FROM maven:3.8.1-jdk-17 AS build

# Копіюємо вихідний код додатку у робочу директорію контейнера
COPY . /usr/src/app

# Переміщаємо до робочої директорії контейнера
WORKDIR /usr/src/app

# Виконуємо збірку додатку Maven
RUN mvn clean package -DskipTests

# Використовуємо за базу офіційний образ Java для виконання додатків Spring Boot
FROM openjdk:17-jre-slim

# Переміщаємо до каталогу контейнера скомпільований додаток Spring Boot
COPY --from=build /api-gateway/target/*.jar /app/my-spring-boot-app.jar

# Вказуємо робочу директорію контейнера
WORKDIR /app

# Команда для запуску додатку при старті контейнера
CMD ["java", "-jar", "my-spring-boot-app.jar"]
