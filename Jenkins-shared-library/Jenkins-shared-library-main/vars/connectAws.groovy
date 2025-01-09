def call(project){
     withCredentials([usernamePassword(
             credentialsId: "AWS_ACCESS_KEY",
             usernameVariable: "USER",
             passwordVariable: "PASS"
     )]) {
        sh "aws configure set aws_access_key_id '$USER'"
        sh "aws configure set aws_secret_access_key '$PASS'"
        sh "aws configure set region '${AWS_DEFAULT_REGION}'"
        //sh aws eks --region ${AWS_REGION} update-kubeconfig --name ${CLUSTER_NAME}
     }
     sh "aws eks --region ${AWS_DEFAULT_REGION} update-kubeconfig --name ${CLUSTER_NAME}"
 }