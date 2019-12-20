# Freelance4j Inc. Assignment Lab / Red Hat OpenShift Application Runtimes
## Design choise
Design choise based on the technical requirements to Assignment Lab.
The following technologies should be used for the services.
### Project Service
 - Vert.x
 - MongoDB database
### Freelancer Service
 - Spring Boot runtime
 - PostgeSQL database
### Gateway API
 - Thorntail runtime

In the project 'freelance4j' is used technologies such as ConfigMap, because it allows to easily set up the environment.
For example, if it need to change the endpoint for the service or it need t change the connection configuration to the database.
The database h2 in memory is used for testing. It is very easy to use, but at the same time gives the opportunity to fully check services.
For the MongoDB testing is used MongoClinet from vert.x.
## URL on github.com or bitbucket.org
 - https://bitbucket.org/diogolopes16/freelance4j
## curl request
### Project Service
 - curl -X GET "http://project-service-dilopes-freelance4j-project.apps.na311.openshift.opentlc.com/projects"
 - curl -X GET "http://project-service-dilopes-freelance4j-project.apps.na311.openshift.opentlc.com/projects/329299"
 - curl -X GET "http://project-service-dilopes-freelance4j-project.apps.na311.openshift.opentlc.com/projects/status/cancelled"
### Freelancer Service
 - curl -X GET "http://freelancer-service-dilopes-freelance4j-freelancer.apps.na311.openshift.opentlc.com/freelancers"
 - curl -X GET "http://freelancer-service-dilopes-freelance4j-freelancer.apps.na311.openshift.opentlc.com/freelancers/1000001"
### Gateway API
 - curl -X GET "http://gateway-service-dilopes-freelance4j-gateway.apps.na311.openshift.opentlc.com/gateway/projects"
 - curl -X GET "http://gateway-service-dilopes-freelance4j-gateway.apps.na311.openshift.opentlc.com/gateway/projects/status/open"
 - curl -X GET "http://gateway-service-dilopes-freelance4j-gateway.apps.na311.openshift.opentlc.com/gateway/projects/329299"
 - curl -X GET "http://gateway-service-dilopes-freelance4j-gateway.apps.na311.openshift.opentlc.com/gateway/freelancers"
 - curl -X GET "http://gateway-service-dilopes-freelance4j-gateway.apps.na311.openshift.opentlc.com/gateway/freelancers/1000002"
# Environment setup
## Environment variables

```sh
$ export PROJECT_PRJ=dilopes-freelance4j-project
$ export FREELANCER_PRJ=dilopes-freelance4j-freelancer
$ export GATEWAY_PRJ=dilopes-freelance4j-gateway
```
## Login on Sharde Master Cluster

```sh
$ oc login --insecure-skip-tls-verify -u dilopes-redhat.com --server=https://master.na311.openshift.opentlc.com:443
```

# Project Service
The Project service provides information about the projects. The Implementation is based on Vert.x + MongoDB. Create project service
```sh
$ oc new-project $PROJECT_PRJ
```

## MONGO DB DEPLOY
Deploy an instance of MongoDB on OpenShift Container Platform using the freelance4j-project-mongodb-persistent.yaml template in the ocp/project-service directory in the lab home directory:
```sh
$ oc process -f ocp/project-service/freelance4j-project-mongodb-persistent.yaml \
-p PROJECT_DB_USERNAME=mongo \
-p PROJECT_DB_PASSWORD=mongo | oc create -f - -n $PROJECT_PRJ
```
## Add Role
Add the view role to the default service acount
```sh
$ oc adm policy add-role-to-group view system:serviceaccounts:$PROJECT_PRJ -n $PROJECT_PRJ
```
## Create configmap
Create the ConfigMap with the configuration for the freelance4j project microservice
```sh
$ oc create configmap app-config --from-file=etc/app-config.yaml -n $PROJECT_PRJ
```
Verify that the configmap is deployed
```sh
$ oc get configmap app-config -o yaml
```
## Deploy on openShift
Deploy the application with fabric8 maven
```sh
$ mvn clean fabric8:deploy -Popenshift -Dfabric8.namespace=$PROJECT_PRJ
```
## Test freelance4j project service
```sh
$ export PROJECT_URL=http://$(oc get route project-service -n $PROJECT_PRJ -o template --template='{{.spec.host}}')
```
Validate project url
```sh
$ echo $PROJECT_URL
```
> http://project-service-dilopes-freelance4j-project.apps.na311.openshift.opentlc.com

Retrieve a list of projects
```sh
$ curl -X GET "$PROJECT_URL/projects"
```
Retrieve project by the id '329299'
```sh
curl -X GET "$PROJECT_URL/projects/329299"
```
Retrieve a list of projects with status 'open'
```sh
curl -X GET "$PROJECT_URL/projects/status/open"
```
# Freelancer service
The Freelancer service provides information about freelancers. The Implementation is based on Spring Boot + PosggreSQL. Create project service
```sh
$ oc new-project $FREELANCER_PRJ
```
## Deploy PostgreSQL on OpenShift
Deploy an instance of PostgreSQL on OpenShift Container Platform using the freelance4j-freelancer-postgresql-persistent.yaml template in the ocp/freelancer-service directory in the lab home directory:
```sh
$ oc process -f ocp/freelancer-service/freelance4j-freelancer-postgresql-persistent.yaml \
-p FREELANCER_DB_USERNAME=jboss \
-p FREELANCER_DB_PASSWORD=jboss | oc create -f - -n $FREELANCER_PRJ
```
Validate database
```sh
$ oc get pods --show-all=false
```
> NAME                            READY     STATUS    RESTARTS   AGE
> freelancer-postgresql-1-vnwv6   1/1       Running   0          5h
```sh
$ oc rsh freelancer-postgresql-1-vnwv6
```
## Add Role
Add the view role to the default service acount
```sh
$ oc adm policy add-role-to-group view system:serviceaccounts:$FREELANCER_PRJ -n $FREELANCER_PRJ
```
## Deploy on openShift
Deploy the application with fabric8 maven
```sh
$ mvn clean fabric8:deploy -Popenshift -Dfabric8.namespace=$FREELANCER_PRJ -DskipTests
```
## Test freelance4j project service
```sh
$ export FREELANCER_URL=http://$(oc get route freelancer-service -n $FREELANCER_PRJ -o template --template='{{.spec.host}}')
```
Validate project url
```sh
$ echo $FREELANCER_URL
```
> http://freelancer-service-dilopes-freelance4j-freelancer.apps.na311.openshift.opentlc.com

Test project service with curl
Invoke the health check endpoint
```sh
$ curl -X GET "$FREELANCER_URL/health"
```
Retrieve a list of freelancers
```sh
$ curl -X GET "$FREELANCER_URL/freelancers"
```
Retrieve freelancer by the id '100001'
```sh
$ curl -X GET "$FREELANCER_URL/freelancers/1000001"
```
# API Gateway
The API gateway is entry point for front-end clients into freelance4j application. The Implementation is based on Thorntail runtime. Create gateway service
```sh
$ oc new-project $GATEWAY_PRJ
```
## Create configmap
Create the ConfigMap with the configuration for the freelance4j gateway microservice
```sh
$ oc create configmap app-config --from-file=etc/project-defaults.yml -n $GATEWAY_PRJ
```
Verify that the configmap is deployed
```sh
$ oc get configmap app-config -o yaml
```
## Add Role
Add the view role to the default service acount
```sh
$ oc adm policy add-role-to-group view system:serviceaccounts:$GATEWAY_PRJ -n $GATEWAY_PRJ
```
## Deploy on openShift
Deploy the application with fabric8 maven
```sh
$ mvn clean fabric8:deploy -Popenshift -Dfabric8.namespace=$GATEWAY_PRJ -DskipTests
```
## Test freelance4j gateway service
```sh
$ export GATEWAY_URL=http://$(oc get route gateway-service -n $GATEWAY_PRJ -o template --template='{{.spec.host}}')
```
Validate project url
```sh
$ echo $GATEWAY_URL
```
> http://gateway-service-dilopes-freelance4j-gateway.apps.na311.openshift.opentlc.com

Test project service with curl
Invoke the health check endpoint
```sh
$ curl -X GET "$GATEWAY_URL/health"
```
Retrieve a list of freelancers
```sh
$ curl -X GET "$GATEWAY_URL/gateway/freelancers"
```
Retrieve freelancer by the id '100001'
```sh
$ curl -X GET "$GATEWAY_URL/gateway/freelancers/1000001"
```
Retrieve a list of projects
```sh
$ curl -X GET "$GATEWAY_URL/gateway/projects"
```
Retrieve project by the id '329299'
```sh
curl -X GET "$GATEWAY_URL/gateway/projects/329299"
```
Retrieve a list of projects with status 'open'
```sh
curl -X GET "$GATEWAY_URL/gateway/projects/status/open"
```
