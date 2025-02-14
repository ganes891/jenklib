//def call(String project, String imageTag, String hubUser){
def call(project){
    sh """
    sudo podman image build -t ${PROJECT} .
    sudo podman image tag ${PROJECT} ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${AWS_IMAGE_REPO_NAME}:${IMAGE_TAG}
    sudo podman image tag ${PROJECT} quay.io/ganesan_kandasamy/${PROJECT}:${IMAGE_TAG}
    """
}


