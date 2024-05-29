@Library('Jenkins-shared-library') _
pipeline {
    agent any
    
    parameters {
        string(name: 'imageName', description: "name of the docker build", defaultValue: 'javaapp')
        string(name: 'imageTag', description: "tag of the docker build", defaultValue: 'v2')
        string(name: 'hubUser', description: "name of the Application", defaultValue: 'ganesh891')
        string(name: 'project', description: "name of the Application", defaultValue: 'myapp01')
    }
    environment {
        DOCKER_IMAGE = 'myapp01:latest'
        PROJECT = 'myapp02'
        HUBUSER = 'ganesh891'
        IMAGETAG = '${imageTag}'
        BRANCH = 'main'
    }

    stages {

        stage('maven build') {
            steps {
                    mvnBuild(PROJECT)                
            }
        }
        stage('Build dockerImage') {
            steps {
                    dockerBuild(PROJECT)                
            }
        }
        stage('Build and Push Docker Image') {
            steps {
                   dockerImagePush(PROJECT)
            }
        }

       /* stage('Deploy to Kubernetes') {
            steps {
                script {
                    // Deploy to Kubernetes using the shared library function
                    deployToKubernetes(deploymentName: 'myapp', image: DOCKER_IMAGE)
                }
            }
        }*/
    }
}



