def call(project) {
    
    checkout([
        $class: 'GitSCM',
        branches: [[name: "${BRANCH}" ]],
        userRemoteConfigs: [[ url: "${GIT_URL}" , credentialsId: "${GITHUB_CREDENTIAL}"]]
    ])
  }
  
