
def call(project){
     withCredentials([usernamePassword(
             credentialsId: "hubUser",
             usernameVariable: "USER",
             passwordVariable: "PASS"
     )]) {
         sh """aws ecr get-login-password --region ${AWS_DEFAULT_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com"""
     }
     sh """docker push ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}/${PROJECT}:${IMAGE_TAG}"""
 }
