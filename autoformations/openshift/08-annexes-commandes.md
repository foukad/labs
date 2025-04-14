
# Commandes OpenShift (oc) essentielles

## 1. Gestion des projets
bash
oc new-project <project-name>
oc get projects
oc project <project-name>
## 2. Gestion des builds et images
oc new-build --binary --name=myapp
oc start-build myapp --from-dir=. --follow
oc get builds
## 3. Déploiement et scaling
oc new-app myapp
oc scale deployment myapp --replicas=3
oc rollout status deployment/myapp
## 4. Exposition des services
oc expose svc/myapp
oc get route
## 5. Logs et debug
oc logs deployment/myapp
oc describe pod <pod-name>
## 6. Supprimer des ressources
oc delete all -l app=myapp
