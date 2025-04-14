# Sécurité sur OpenShift : RBAC et SCC

## 1. Comprendre le RBAC (Role-Based Access Control)
OpenShift permet de donner des droits fins par utilisateur ou service account.

## 2. Exemple : Créer un Role
bash oc create role pod-reader --verb=get,list,watch --resource=pods
## 3. Créer un ServiceAccount
oc create serviceaccount springboot-sa
## 4. Binder le Role au ServiceAccount
oc create rolebinding pod-reader-binding \
--role=pod-reader \
--serviceaccount=springboot-demo:springboot-sa
## 5. SCC (Security Context Constraints)
Gère les permissions au niveau du container (user, volumes, réseau).
## 6. Voir les SCC disponibles
oc get scc
## 7. Donner un SCC particulier à un ServiceAccount
Exemple : donner le SCC anyuid
oc adm policy add-scc-to-user anyuid -z springboot-sa
8. Utiliser le ServiceAccount dans le Deployment
spec:
  serviceAccountName: springboot-sa



