def call(project){
    def branch = env.BRANCH
    def repo = env.APP_REPO
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
     //sh "aws eks --region ${AWS_DEFAULT_REGION} update-kubeconfig --name ${CLUSTER_NAME}"
 }

stage('Checkout') {
            steps {
                script {
                    // Define Git repository URL
                    def gitRepoUrl = https://github.com/${repo}

                    // Checkout the code using Personal Access Token
                    git credentialsId: '9db7a662-10fb-49ba-8b48-b9adcd66236d', url: gitRepoUrl
                }
            }
        }