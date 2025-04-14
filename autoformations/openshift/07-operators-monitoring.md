# Operators et Monitoring dans OpenShift

## 1. Qu’est-ce qu’un Operator ?
Un Operator permet de gérer des applications complexes sur OpenShift (bases de données, monitoring…).

## 2. Installer un Operator via la console
- Aller dans OperatorHub
- Rechercher par exemple : `Prometheus`
- Installer l’Operator dans ton projet

## 3. Exemple de création d’un Prometheus Instance
yaml
apiVersion: monitoring.coreos.com/v1
kind: Prometheus
metadata:
  name: springboot-prometheus
spec:
  serviceAccountName: prometheus-k8s
  serviceMonitorSelector:
    matchLabels:
      team: frontend
  resources:
    requests:
      memory: 400Mi
## 4. Ajouter un ServiceMonitor
apiVersion: monitoring.coreos.com/v1
kind: ServiceMonitor
metadata:
  name: springboot-monitor
  labels:
    team: frontend
spec:
  selector:
    matchLabels:
      app: springboot-openshift
  endpoints:
    - port: web
## 5. Visualiser les métriques
Aller dans la section Monitoring de la console OpenShift

Consulter les dashboards Prometheus ou Grafana si installé
## 6. Bonus : Grafana Operator
Installer l’Operator Grafana

Lier Grafana à Prometheus pour créer des dashboards personnalisés