# Déploiement d'une API REST Spring Boot sur OpenShift

## 1. Description de l'application
Une API REST de gestion des articles avec endpoints :
- GET /articles
- POST /articles
- GET /articles/{id}

La base de données utilisée : PostgreSQL.

## 2. Préparation de l'application Spring Boot
- Créer le projet avec :
bash spring init --dependencies=web,data-jpa,postgresql springboot-openshift

## 3. Dockerfile pour builder l'app
FROM openjdk:17-jdk-slim
COPY target/springboot-openshift.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
## 4. Commande pour builder et créer l'image
./mvnw clean package -DskipTests
docker build -t springboot-openshift:1.0 .
## 5. Déploiement sur OpenShift

### a) Pusher l’image dans le registry OpenShift
oc new-build --binary --name=springboot-openshift -l app=springboot-openshift
oc start-build springboot-openshift --from-dir=. --follow
### b) Créer le Deployment
oc new-app springboot-openshift
oc expose svc/springboot-openshift
### c) Tester
Récupérer l’URL :
oc get route
Test de l'API :
curl http://<route>/articles
