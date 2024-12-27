
def call(project){
     withCredentials([usernamePassword(
             credentialsId: "hubUser",
             usernameVariable: "USER",
             passwordVariable: "PASS"
     )]) {
         sh """aws ecr get-login-password --region ${AWS_DEFAULT_REGION} | sudo docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com"""
     }
     sh """sudo docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}:${IMAGE_TAG}"""
 }
