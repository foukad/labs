# Intégration de PostgreSQL sur OpenShift

## 1. Déploiement de la base PostgreSQL
### a) Création d’un projet si ce n’est pas déjà fait
bash oc new-project springboot-demo
### b) Déploiement de PostgreSQL
oc new-app --name=postgresql \
-e POSTGRESQL_USER=postgres \
-e POSTGRESQL_PASSWORD=postgres \
-e POSTGRESQL_DATABASE=articles \
registry.redhat.io/rhel8/postgresql-13
## 2. Vérifier le service et le pod PostgreSQL
oc get pods
oc get svc
## 3. Créer un Secret pour la connexion BDD (optionnel)
oc create secret generic db-secret \
--from-literal=username=postgres \
--from-literal=password=postgres
## 4. Lier la BDD à l'application Spring Boot
Dans le application.properties :
spring.datasource.url=jdbc:postgresql://postgresql:5432/articles
spring.datasource.username=postgres
spring.datasource.password=postgres
## 5. Vérifier la connexion et la persistance
curl -X POST http://<route>/articles \
-H "Content-Type: application/json" \
-d '{"title":"Premier Article","content":"Contenu"}'

curl http://<route>/articles
