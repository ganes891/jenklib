def call(project){
     withCredentials([usernamePassword(
             credentialsId: "AWS_ACCESS_KEY",
             usernameVariable: "USER",
             passwordVariable: "PASS"
     )]) {
            sh """
                   terraform init 
                   terraform plan 
               """
            }
     }
     //sh "aws eks --region ${AWS_DEFAULT_REGION} update-kubeconfig --name ${CLUSTER_NAME}"
 