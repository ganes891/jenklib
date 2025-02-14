//def call(String project, String imageTag, String hubUser){
def call(project){
    sh """
    sudo docker image build -t ${PROJECT} .
    sudo docker image tag ${PROJECT} ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}:${IMAGE_TAG}
    sudo docker image tag ${PROJECT} quay.io/ganesan_kandasamy/${PROJECT}:${IMAGE_TAG}
    """
}


