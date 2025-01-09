def call(project){
     withCredentials([usernamePassword(
             credentialsId: "AWS_ACCESS_KEY_ID",
             usernameVariable: "AWS_ACCESS_KEY_ID",
             credentialsId: "AWS_SECRET_KEY_ID",
             passwordVariable: "AWS_SECRET_KEY_ID",
     )]) {
        sh "aws configure set aws_access_key_id "$AWS_ACCESS_KEY_ID""
        sh "aws configure set aws_secret_access_key "$AWS_SECRET_KEY_ID""
        sh "aws configure set region "${params.Region}""
        //sh aws eks --region ${AWS_REGION} update-kubeconfig --name ${CLUSTER_NAME}
     }
     sh "aws eks --region ${AWS_DEFAULT_REGION} update-kubeconfig --name ${CLUSTER_NAME}"
 }