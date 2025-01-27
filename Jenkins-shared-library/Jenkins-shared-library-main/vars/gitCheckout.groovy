def call(project) {
  def branch = env.BRANCH_NAME
  def url = env.GIT_URL
  def creds = env.GITHUB_CREDENTIALS

    checkout([
        $class: 'GitSCM',
        branches: [[name: ${BRANCH_NAME} ]],
        userRemoteConfigs: [[ url: ${GIT_URL} , credentialsId: ${GITHUB_CREDENTIAL}]]
    ])
  }
  
