# Fase de construção
FROM ubuntu:latest AS build

# Atualizar pacotes e instalar Java e Maven
RUN apt-get update && \
    apt-get install -y openjdk-21-jdk maven && \
    apt-get clean

# Definir o diretório de trabalho
WORKDIR /app

# Copiar todos os arquivos do projeto para o diretório de trabalho
COPY . .

# Construir a aplicação
RUN mvn clean install

# Fase de execução
FROM openjdk:21-jdk-slim

# Definir o diretório de trabalho
WORKDIR /app

# Expor a porta que a aplicação irá rodar
EXPOSE 8082

# Copiar o arquivo JAR da fase de construção
COPY --from=build /app/target/product-0.0.1-SNAPSHOT.jar app.jar

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]