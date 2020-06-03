# Experiment with http communication options on multiple k8s platforms

## Prerequisites

Install and start minikube.

[Configure knative](https://gist.github.com/poprygun/c7cc07232a7ec0f86eb7888d48b470cd)

## Current version of spring-boot plugin does not push do dockerhub, so we use jib for now

```bash
./mvnw jib:build
```

```bash
minikube start --memory=16384 --cpus=4 --kubernetes-version=v1.18.0 --vm-driver=hyperkit --bootstrapper=kubeadm --addons=ingress --extra-config=apiserver.enable-admission-plugins="LimitRanger,NamespaceExists,NamespaceLifecycle,ResourceQuota,ServiceAccount,DefaultStorageClass,MutatingAdmissionWebhook"
```

Install [Skaffold](https://skaffold.dev/docs/quickstart/).

## Test

When running locally - test using [httpie](https://httpie.org/).

```bash
http :8113
```

## Deploy and Run using _skaffold_

```bash
scaffold run
```

## Review knative objects

```bash
kubectl get route -l \
"serving.knative.dev/service=chachkie-client" --output yaml
```
```bash
kubectl get service -l \
"serving.knative.dev/service=chachkie-client" --output yaml
```
```bash
kubectl get configuration -l \
"serving.knative.dev/service=chachkie-client" --output yaml
```
```bash
kubectl get revision -l \
"serving.knative.dev/service=chachkie-client" --output yaml
```
```bash
kubectl get deployment -l \
"serving.knative.dev/service=chachkie-client" --output yaml
```

## Watch for errors during deployment

```bash
kubectl logs deployment/controller -n knative-serving | grep "error" | less
```

## Test deployed services

Obtain service uri by noting URL column:

```bash
kubectl get ksvc
http chachkie-server.default.10.109.98.199.xip.io/chachkies
http chachkie-client.default.10.109.98.199.xip.io
```