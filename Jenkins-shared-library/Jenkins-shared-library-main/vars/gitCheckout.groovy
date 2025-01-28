def call(project) {
    
    withCredentials([usernamePassword(
    credentialsId: "9db7a662-10fb-49ba-8b48-b9adcd66236d",
    usernameVariable: "USER",
    passwordVariable: "PASS"
     )])
    checkout([
        $class: 'GitSCM',
        branches: [[name: "${BRANCH}" ]],
        userRemoteConfigs: [[ url: "${GIT_URL}" , credentialsId: "${GITHUB_CREDENTIAL}"]]
    ])
  }
  
