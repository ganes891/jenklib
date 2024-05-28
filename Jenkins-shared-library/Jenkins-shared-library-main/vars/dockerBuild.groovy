def call(String Project, String ImageTag, String hubUser){
    sh """
    docker image build -t ${hubUser}/${Project} .
    docker image tag ${hubUser}/${Project} ${hubUser}/${Project}:${ImageTag}
    docker image tag ${hubUser}/${Project} ${hubUser}/${Project}:latest
    """
}