def call() {
            def branch = env.BRANCH_NAME
            def image = env.IMAGE_NAME ?: "${env.JOB_NAME}".split('/')[0]
            def tag = currentBuild.id
            def scannerHome = tool 'SonarQube'
            def channel = '#builds'

            print "SonarAnalysis(${env.JOB_NAME}, ${image}/src)"

            def NotallowedBranches = ['primary', 'sandbox']
            if (branch in NotallowedBranches) {
                print "Skipping SonarAnalysis for branch - ${branch}."
            }
            else {
                   withSonarQubeEnv('SonarQube'){
                   sh "${scannerHome}/bin/sonar-scanner \
                   -D sonar.projectName=${image} \
                   -D sonar.projectKey=${image} \
                   -D sonar.branch.name=${branch} \
                   -D sonar.sources=.  "
                    }

                    sleep 60
                    waitForQualityGate abortPipeline: true
                        
              }
            def sonar_url = "https://codequality.rr-infra.net/dashboard?id=${image}&branch=${branch}"
            def message = """
                            ${branch} || *${env.IMAGE_NAME}* || Build-${currentBuild.id} || #${env.STAGE_NAME}# || *${currentBuild.currentResult}*
                            *SonarAnalysis Passed*, - *Click to view the result* - (<${sonar_url}|Open>)
                          """
            CInotifySlack(channel, message, "green", branch)
  }
