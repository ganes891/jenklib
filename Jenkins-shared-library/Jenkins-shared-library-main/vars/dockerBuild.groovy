//def call(String project, String imageTag, String hubUser){
def call(project){
    sh """
    sudo docker image build -t ${APP} .
    sudo docker image tag ${APP} ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}:${IMAGE_TAG}
    """
}


