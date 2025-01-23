stage('Checkout') {
            steps {
                script {
                    // Define Git repository URL
                    def gitRepoUrl = 'https://github.com/yourusername/your-repo.git'

                    // Checkout the code using Personal Access Token
                    git credentialsId: 'github-credentials', url: gitRepoUrl
                }
            }
        }