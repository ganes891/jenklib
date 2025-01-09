def call(project){
     withCredentials([usernamePassword(
             credentialsId: "AWS_ACCESS_KEY",
             usernameVariable: "USER",
             passwordVariable: "PASS"
     )]) {
            sh """
                   terraform init 
                   terraform plan -var 'access_key=$USER' -var 'secret_key=$PASS' -var 'region=${AWS_DEFAULT_REGION}' 
                   #terraform apply -var 'access_key=$USER' -var 'secret_key=$PASS' -var 'region=${AWS_DEFAULT_REGION}' --auto-approve
               """
            }
     }
     //sh "aws eks --region ${AWS_DEFAULT_REGION} update-kubeconfig --name ${CLUSTER_NAME}"
 }