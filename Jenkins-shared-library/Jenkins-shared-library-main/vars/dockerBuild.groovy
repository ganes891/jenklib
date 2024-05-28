//def call(String project, String imageTag, String hubUser){
def call(project){
    sh """
    sudo docker image build -t ${hubUser}/${project} .
    sudo docker image tag ${hubUser}/${project} ${hubUser}/${project}:${imageTag}
    sudo docker image tag ${hubUser}/${project} ${hubUser}/${project}:latest
    """
}


