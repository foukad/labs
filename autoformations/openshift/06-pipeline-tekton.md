# CI/CD avec Tekton et OpenShift Pipelines

## 1. Qu’est-ce que Tekton ?
Tekton est un framework Kubernetes natif pour construire des pipelines CI/CD dans OpenShift.

## 2. Installer l’Operator Tekton (si nécessaire)
Via l’interface OpenShift -> OperatorHub -> Tekton Pipelines

## 3. Exemple de Pipeline YAML
`yaml
apiVersion: tekton.dev/v1beta1
kind: Pipeline
metadata:
  name: springboot-pipeline
spec:
  tasks:
    - name: build
      taskRef:
        name: maven
      params:
        - name: GOALS
          value: ["clean", "package"]
## 4. Exemple de Task Maven
apiVersion: tekton.dev/v1beta1
kind: Task
metadata:
  name: maven
spec:
  params:
    - name: GOALS
      type: array
  steps:
    - name: mvn
      image: maven:3.8.4-openjdk-17
      script: |
        mvn $(params.GOALS)
## 5. Lancer la pipeline
tkn pipeline start springboot-pipeline
## 6. Suivre la pipeline
tkn pipeline logs springboot-pipeline -f
## 7. Bonus
Visualiser dans la Console OpenShift -> Pipelines -> Runs
