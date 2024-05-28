@Library('Jenkins-shared-library') // Load the Kubernetes deployment library
import com.example.kubernetes.*

pipeline {
    agent any
    
    parameters {
        string(name: 'ImageName', description: "name of the docker build", defaultValue: 'javaapp')
        string(name: 'ImageTag', description: "tag of the docker build", defaultValue: 'v1')
        string(name: 'HubUser', description: "name of the Application", defaultValue: 'ganash891')
    }
    environment {
        DOCKER_IMAGE = 'myapp01:latest'
        PROJECT = 'springboot'
    }

    stages {
        stage('Build dockerImage') {
            steps {
                script {
                    dockerBuild(image: DOCKER_IMAGE, project: PROJECT)
                }
            }
        }
        stage('Build and Push Docker Image') {
            steps {
                script {
                    dockerImagePush(image: DOCKER_IMAGE, project: PROJECT)
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