@Library('Jenkins-shared-library') _

pipeline {

    agent any

    parameters
    {   
        choice(name: 'platform', choices: 'AWS\nAZURE\nVM', description: 'choose the cloud platform')
        choice(name: 'action', choices: 'BuildAndDeploy\ndelete\nDeployOnly', description: 'choose BuildAndDeploy/Destroy')
        string(name: 'aws_account_id', description: " AWS Account ID", defaultValue: '599646583608')
        string(name: 'Region', description: "Region of ECR", defaultValue: 'ap-southeast-1')
        string(name: 'ImageName', description: "name of the docker build", defaultValue: 'myapp01')
        string(name: 'ImageTag', description: "tag of the docker build", defaultValue: 'v2.1')
        string(name: 'cluster', description: "name of the EKS Cluster", defaultValue: 'SAP-dev-eksdemo')
    }
 
    environment{
        DOCKER_IMAGE = 'myapp01'
        PROJECT = '${ImageName}'
        IMAGE_TAG = '${ImageTag}'
        BRANCH = 'main'
        AWS_ACCOUNT_ID= '599646583608'
        AWS_DEFAULT_REGION= 'ap-southeast-1'
        AWS_IMAGE_REPO_NAME= 'dev-project/app01'
        CLUSTER_NAME = 'xyz'
        EKS_TF_DIR = 'infra/eks-admin-tf/01-ekscluster-terraform-manifests'
        GITHUB_CREDENTIAL = '9db7a662-10fb-49ba-8b48-b9adcd66236d'
        //APP_REPO = 'ganes891/jenklib.git'
        GIT_URL = 'https://github.com/ganes891/jenklib.git'
        HUB_URL = 'https://quay.io/repository/ganesan_kandasamy/'
        HUB_REPO = 'ada01'
    }
   
    stages{
         
      stage('Git Checkout'){
                when{expression{params.action == "BuildAndDeploy"}}    
            steps{
              script{
                //gitCheckout(project)
                sh 'pwd'
              }
            }
        }
      
      /* stage('Static Code Analysis: Sonarqube'){
               when{expression{params.action == "BuildAndDeploy"}}      
            steps{
               script{
                   def SonarQubecredentialsId = 'SonarQubeapi'
                   staticCodeAnalysis(SonarQubecredentialsId)
               }
            }
        }
       
       stage('Quality Gate status check: Sonarqube'){
               when{expression{params.action == "BuildAndDeploy"}}      
            steps{
               script{
                   def SonarQubecredentialsId = 'SonarQubeapi'
                   staticCodeAnalysis(SonarQubecredentialsId)
               }
            }
        }*/
        
      stage('Maven build: maven'){
              when{expression{params.action == "BuildAndDeploy"}}       
            steps{
               script{
                   
                    //mvnBuild()
                    sh "pwd"
               }
            }
        }
        
      stage('Docker Image Build'){
              when{expression{params.action == "BuildAndDeploy"}}       
            steps{
               script{
                   
                    //dockerBuild("${params.ImageName}","${params.ImageTag}","${params.DockerHubUser}")
                    podmanBuild(PROJECT)
                    sh "date"
               }
            }
        }
      stage('Docker Quay Registry Image Push'){
              when{expression{params.platform == "VM"}}       
            steps{
               script{
                   
                    //dockerImagePush("${params.ImageName}","${params.ImageTag}","${params.DockerHubUser}")
                    dockerImagePush(PROJECT)
               }
            }
        }
        
      stage('AWS ECR Image Push'){
              when{expression{params.platform == "AWS"}}       
            steps{
               script{
                   
                    dockerImagePushEcr("${params.ImageName}","${params.ImageTag}","${params.DockerHubUser}")
                    
               }
            }
        }

      stage('Connect to EKS cluster: Terraform'){
              when{expression{params.action == "AWS"}}       
            steps{
               script{
                   
                    //connectAws(PROJECT)
                    sh 'pwd'
               }
            }
        }


      stage('BuildAndDeploy EKS cluster using IAAC: Terraform'){
            when{expression{params.action == "AWS"}}       
            steps{
               script{
                   dir("${EKS_TF_DIR}")
                   { 
                    BuildAndDeployInfraAws(PROJECT)
               }   
            }
        }
      }
      stage('cleanup workspace'){
        steps{
       // cleanWs()
       sh 'pwd'
        }
      }
       /* stage('Connect to EKS cluster: Terraform'){
              when{expression{params.action == "BuildAndDeploy"}}       
            steps{
               script{
                  sh """
                  aws configure set aws_access_key_id "$ACCESS_KEY"
                  aws configure set aws_secret_access_key "$SECRET_KEY"
                  aws configure set region "${params.Region}"
                  aws eks --region ${params.Region} update-kubeconfig --name ${params.cluster}
                  """
               }   
            }
        }*/
        stage('Deployment of EKS cluster: Terraform'){
              when{expression{params.action == "DeployOnly" || params.action == "BuildAndDeploy" }}       
            steps{
               script{
                    
                  def apply = false

                  try{
                    input message: 'please confirm to deploy on eks', ok: 'Ready to apply the config ?'
                    apply = true
                  }catch(err){
                    apply= false
                    currentBuild.result  = 'UNSTABLE'
                  }
                  if(apply){

                    sh """
                      kubectl apply -f .
                    """
                  }
               }   
            }
        }
    }
}


