# Experiment with http communication options on multiple k8s platforms

## Prerequisites

Install and start minikube.

```bash
minikube start --memory=8192 --cpus=2 --kubernetes-version=v1.18.0 --vm-driver=hyperkit --bootstrapper=kubeadm --extra-config=apiserver.enable-admission-plugins="LimitRanger,NamespaceExists,NamespaceLifecycle,ResourceQuota,ServiceAccount,DefaultStorageClass,MutatingAdmissionWebhook"
```

Enable ingress

```bash
minikube addons enable ingress
```

Install [Skaffold](https://skaffold.dev/docs/quickstart/).

## Deploy and Run using _skaffold_.

```bash
scaffold run
```

## Test

When running locally - test using [httpie](https://httpie.org/).

```bash
http :8113
```

Test deployed services:

```bash
kubectl get endpoints chachkie-server
kubectl run busybox --image=busybox --rm -it --restart=Never -- wget -qO- chachkie-server:8080/chachkies
kubectl run busybox --image=busybox --rm -it --restart=Never -- wget -qO- chachkie-client:8080
```

Test ingress:

```bash
http $(minikube ip)/client
http $(minikube ip)/server/chachkies
```

## Current version of spring-boot plugin does not push do dockerhub, so we use jib for now.

```bash
./mvnw jib:build
```