apiVersion: apps/v1
kind: Deployment
metadata:
  name: springboot-with-nginx
spec:
  replicas: 1
  selector:
    matchLabels:
      app: myapp
  template:
    metadata:
      labels:
        app: myapp
    spec:
      containers:
      - name: nginx
        image: nginx:latest
        ports:
        - containerPort: 443
        volumeMounts:
        - name: nginx-config-volume
          mountPath: /etc/nginx/nginx.conf
          subPath: nginx.conf
        - name: certs
          mountPath: /etc/nginx/certs
      - name: springboot-app
        image: myapp:latest
        ports:
        - containerPort: 8080
      volumes:
      - name: nginx-config-volume
        configMap:
          name: nginx-config
      - name: certs
        secret:
          secretName: my-tls-secret
