# 使用 JDK 17 作为基础镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制 Maven 构建的 jar 文件到容器中
COPY target/parking-system-1.0-SNAPSHOT.jar app.jar

# 复制配置文件
COPY src/main/resources/application.yml application.yml

# 暴露应用端口
EXPOSE 8080

# 设置容器启动命令
ENTRYPOINT ["java", "-jar", "app.jar"] 