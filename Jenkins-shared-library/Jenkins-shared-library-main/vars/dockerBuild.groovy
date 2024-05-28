def dockerBuild(String project, String imageTag, String hubUser){
    sh """
    docker image build -t ${hubUser}/${project} .
    docker image tag ${hubUser}/${project} ${hubUser}/${project}:${imageTag}
    docker image tag ${hubUser}/${project} ${hubUser}/${project}:latest
    """
}


