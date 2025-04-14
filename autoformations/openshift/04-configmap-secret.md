# Utilisation des ConfigMaps et Secrets dans OpenShift

## 1. Créer une ConfigMap
Permet de stocker une config externe et l’injecter dans l’application :
bash oc create configmap app-config \
--from-literal=APP_NAME=SpringBootOpenShiftDemo
## 2. Vérifier la ConfigMap
oc get configmap app-config -o yaml
## 3. Monter la ConfigMap dans le Pod
env:
  - name: APP_NAME
    valueFrom:
      configMapKeyRef:
        name: app-config
        key: APP_NAME
## 4. Créer un Secret
Pour stocker des credentials ou tokens :

bash
Copier
Modifier
oc create secret generic db-secret \
--from-literal=username=postgres \
--from-literal=password=postgres
## 5. Monter le Secret dans le Pod
env:
  - name: DB_USERNAME
    valueFrom:
      secretKeyRef:
        name: db-secret
        key: username
  - name: DB_PASSWORD
    valueFrom:
      secretKeyRef:
        name: db-secret
        key: password
## 6. Vérifier dans le code Spring Boot
@Value("${APP_NAME}")
private String appName;
