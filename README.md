![Pipeline (1)](https://user-images.githubusercontent.com/39452579/233925555-ea8327e3-66d1-42e8-9d67-323555073773.jpg)

# kubernetes-configmap-reload

Pre-requisites:
--------
    - Install Git
    - Install Maven
    - Install Docker
    - EKS Cluster
    
Clone code from github:
-------
    git clone https://github.com/ganasai88/CI-CD-Project.git
    cd CI-CD-Project
    
Build Maven Artifact:
-------
    mvn clean install
 
Build Docker image for Springboot Application
--------------
    docker build -t dockerhubuserid/imagename
  
Docker login
-------------
    docker login
    
Push docker image to dockerhub
-----------
    docker push dockerhubuserid/imagename
    
Deploy Spring Application:
--------
    kubectl apply -f .
    
Check Deployments, Pods and Services:
-------

    kubectl get deploy
    kubectl get pods
    kubectl get svc
    
Now Goto Loadbalancer and check whether service comes Inservice or not, If it comes Inservice copy DNS Name of Loadbalancer and Give in WebUI

 
Now we can cleanup by using below commands:
--------
    kubectl delete deploy kubernetes-configmap-reload
    kubectl delete svc kubernetes-configmap-reload

 sudo docker image tag ${hubUser}/${project} ${hubUser}/${project}:${imageTag}
 
 https://medium.com/@andy001018/jenkins-cicd-deploy-laravel-using-aws-ecr-and-ecs-fe0f7103d13d
 

 =====

code:

// vars/customLibrary.groovy
def buildDockerImage(String imageName, String tag) {
    def image = docker.build("${imageName}:${tag}")
        return image
        }
def pushDockerImage(String imageName, String tag, String registry, String credentialsId) {
    docker.withRegistry("${registry}", "${credentialsId}") {
            docker.image("${imageName}:${tag}").push()
                }
                }
https://chatgpt.com/c/7c8ee984-cc03-4de1-9fa3-33c193572d2c

                    /*sh """
                          terraform init 
                          #terraform plan -var 'access_key=$ACCESS_KEY' -var 'secret_key=$SECRET_KEY' -var 'region=${params.Region}' --var-file=./config/terraform.tfvars
                          terraform plan -var 'access_key=$ACCESS_KEY' -var 'secret_key=$SECRET_KEY' -var 'region=${params.Region}' 
                          #terraform apply -var 'access_key=$ACCESS_KEY' -var 'secret_key=$SECRET_KEY' -var 'region=${params.Region}' --var-file=./config/terraform.tfvars --auto-approve
                          terraform apply -var 'access_key=$ACCESS_KEY' -var 'secret_key=$SECRET_KEY' -var 'region=${params.Region}' --auto-approve
                      """
                   }*/



-var 'access_key=$USER' -var 'secret_key=$PASS' -var 'region=${AWS_DEFAULT_REGION}' 
                   #terraform apply -var 'access_key=$USER' -var 'secret_key=$PASS' -var 'region=${AWS_DEFAULT_REGION}' --auto-approve
                   
