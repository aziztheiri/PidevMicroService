# Étape 1 : Utilisation de l'image de base JDK 17
FROM eclipse-temurin:17-jdk-alpine

# Étape 2 : Définition du répertoire de travail dans le conteneur
WORKDIR /app

# Étape 3 : Copier le fichier JAR dans le conteneur
COPY target/MicroSPaiement-0.0.1-SNAPSHOT.jar app.jar

# Étape 4 : Exposer le port sur lequel tourne Spring Boot
EXPOSE 8080

# Étape 5 : Définition de la commande de lancement
CMD ["java", "-jar", "app.jar"]
