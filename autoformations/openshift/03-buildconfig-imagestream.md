# BuildConfig et ImageStream dans OpenShift

## 1. BuildConfig - C'est quoi ?
BuildConfig permet de construire automatiquement une image Docker à partir d’un repo Git ou d’un build local.

## 2. Exemple de création d’une BuildConfig
bash oc new-build --name=springboot-openshift \
--binary=true \
--strategy=docker
## 3. Lancer un build depuis le code source
oc start-build springboot-openshift --from-dir=. --follow
## 4. Vérifier l’image construite
oc get builds
oc get imagestreams
## 5. Déployer l’image construite
oc new-app springboot-openshift
oc expose svc/springboot-openshift
## 6. Tester l’application
oc get route
curl http://<route>/articles
Bonus : Visualiser dans la console OpenShift
* Aller dans Builds -> voir l’historique des builds
* Aller dans Images -> voir l’ImageStream associé