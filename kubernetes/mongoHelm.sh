helm install community-operator mongodb/community-operator --namespace nsmongo --create-namespace
kubectl config set-context --current --namespace=nsmongo