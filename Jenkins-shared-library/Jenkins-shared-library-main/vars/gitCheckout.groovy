def call(project) {
  def branch = env.BRANCH
  def url = env.GIT_URL
  def creds = env.GITHUB_CREDENTIALS

    checkout([
        $class: 'GitSCM',
        branches: [[name: "${BRANCH}" ]],
        userRemoteConfigs: [[ url: "${GIT_URL}" , credentialsId: "${GITHUB_CREDENTIAL}"]]
    ])
  }
  
