@Library('Jenkins-shared-library') _
//import com.example.kubernetes.*
pipeline {
    agent any
    
    parameters {
        string(name: 'ImageName', description: "name of the docker build", defaultValue: 'javaapp')
        string(name: 'ImageTag', description: "tag of the docker build", defaultValue: 'v1')
        string(name: 'HubUser', description: "name of the Application", defaultValue: 'ganash891')
        string(name: 'project', description: "name of the Application", defaultValue: 'myapp01')
    }
    environment {
        DOCKER_IMAGE = 'myapp01:latest'
        PROJECT = 'springboot'
    }

    stages {
        stage('Build dockerImage') {
            steps {
                script {
                    dockerBuild()
                }
            }
        }
        stage('Build and Push Docker Image') {
            steps {
                script {
                    dockerImagePush()
                }
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