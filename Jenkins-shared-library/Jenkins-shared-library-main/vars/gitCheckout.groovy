def call(Map stageParams) {
  def branch = env.BRANCH_NAME
  def url = env.GIT_URL
    checkout([
        $class: 'GitSCM',
        branches: [[name:  stageParams.branch ]],
        userRemoteConfigs: [[ url: stageParams.url ]]
    ])
  }
  