def call() {
    def branch = env.BRANCH_NAME
    def image = env.IMAGE_NAME ?: "${env.JOB_NAME}".split('/')[0]
    def tag = currentBuild.id
    def deploymentFile = "k8sDeploymentFiles/${env.IMAGE_NAME}/deployment.yaml"
    def everdeenFolder = "EverdeenDeployments"
    def channel = '#builds'

    print "deployArgo(${env.JOB_NAME}, ${branch}/${image}:${tag})"
    def allowedBranches = ['develop', 'sandbox']
    if (branch in allowedBranches) {

        sh """
               if [ -d deployment ]
               then
               rm -r deployment
               mkdir deployment
               else
               mkdir deployment
               fi
           """
        if ( branch == 'sandbox' ) {
            withCredentials([usernamePassword(credentialsId: 'Self_use_readonly_token', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]){
                if ( image == 'everdeen' ) { 
                    // Updating 'Sandbox' Everdeen ArgoRepo           
                    sh """
                        cd deployment
                        git clone https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/rightrev/cd-sdx-yamls.git
                        cd cd-sdx-yamls/sdx-02
                        sed -i 's/${branch}\\/${image}:.*/${branch}\\/${image}:${tag}/g' ${everdeenFolder}/*/deployment.yaml
                        git add .
                        git commit -m 'CI - Modified image tag ${env.BUILD_ID}'
                        git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/rightrev/cd-sdx-yamls.git
                        sleep 2
                    """
                    CInotifySlack("${channel}", "${branch} || *${env.IMAGE_NAME}* || Build-${currentBuild.id} || #${env.STAGE_NAME}# || *${currentBuild.currentResult}*", "green", "${branch}")

                    // Updating 'Production' Everdeen ArgoRepo           
                    sh """
                        cd deployment
                        git clone https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/rightrev/cd-prd-yamls.git
                        cd cd-prd-yamls/
                        sed -i 's/${branch}\\/${image}:.*/${branch}\\/${image}:${tag}/g' ${everdeenFolder}/*/deployment.yaml
                        git add .
                        git commit -m 'CI - Modified image tag ${env.BUILD_ID}'
                        git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/rightrev/cd-prd-yamls.git
                        sleep 2
                    """
                    branch = 'prd'
                    CInotifySlack("${channel}", "${branch} || *${env.IMAGE_NAME}* || Build-${currentBuild.id} || #${env.STAGE_NAME}# || *${currentBuild.currentResult}*", "green", "${branch}")
                }
                else {
                    // Updating 'Sandbox' ArgoRepo
                    sh """
                        cd deployment
                        git clone https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/rightrev/cd-sdx-yamls.git
                        cd cd-sdx-yamls/sdx-02
                        sed -i 's/${branch}\\/${image}:.*/${branch}\\/${image}:${tag}/g' ${deploymentFile}
                        git add .
                        git commit -m 'CI - Modified image tag ${env.BUILD_ID}'
                        git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/rightrev/cd-sdx-yamls.git
                        sleep 2
                    """
                    CInotifySlack("${channel}", "${branch} || *${env.IMAGE_NAME}* || Build-${currentBuild.id} || #${env.STAGE_NAME}# || *${currentBuild.currentResult}*", "green", "${branch}")
                
                }
            }

        }

        else if ( branch == 'develop' ) {
            if ( image == 'everdeen' ) {
                withCredentials([usernamePassword(credentialsId: 'Self_use_readonly_token', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]){
                    // Updating 'Develop' Everdeen ArgoRepo
                    sh """
                        cd deployment
                        git clone https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/rightrev/cd-dev-yamls.git
                        cd cd-dev-yamls
                        sed -i 's/${branch}\\/${image}:.*/${branch}\\/${image}:${tag}/g' ${everdeenFolder}/*/deployment.yaml
                        git add .
                        git commit -m 'CI - Modified image tag ${env.BUILD_ID}'
                        git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/rightrev/cd-dev-yamls.git
                        sleep 2
                    """

                    // Updating 'QA' Everdeen ArgoRepo
                    branch = 'qa'
                    sh """
                        cd deployment
                        git clone https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/rightrev/cd-qa-yamls.git
                        cd cd-qa-yamls
                        sed -i 's/${branch}\\/${image}:.*/${branch}\\/${image}:${tag}/g' ${everdeenFolder}/*/deployment.yaml
                        git add .
                        git commit -m 'CI - Modified image tag ${env.BUILD_ID}'
                        git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/rightrev/cd-qa-yamls.git
                        sleep 2
                    """
                }
            }
            else {
                withCredentials([usernamePassword(credentialsId: 'Self_use_readonly_token', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]){
                    
                    // Updating 'Develop' ArgoRepo            
                        sh """
                            cd deployment
                            git clone https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/rightrev/cd-dev-yamls.git
                            cd cd-dev-yamls
                            sed -i 's/${branch}\\/${image}:.*/${branch}\\/${image}:${tag}/g' ${deploymentFile}
                            git add .
                            git commit -m 'CI - Modified image tag ${env.BUILD_ID}'
                            git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/rightrev/cd-dev-yamls.git
                            sleep 2
                            /usr/local/bin/argocd --grpc-web app sync dev-${image} --force || true
                        """
                    CInotifySlack("${channel}", "${branch} || *${env.IMAGE_NAME}* || Build-${currentBuild.id} || #${env.STAGE_NAME}# || *${currentBuild.currentResult}*", "green", "${branch}")
                                
                    // Updating 'QA' ArgoRepo
                        branch = 'qa'
                        sh """
                            cd deployment
                            git clone https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/rightrev/cd-qa-yamls.git
                            cd cd-qa-yamls
                            sed -i 's/${branch}\\/${image}:.*/${branch}\\/${image}:${tag}/g' ${deploymentFile}
                            git add .
                            git commit -m 'CI - Modified image tag ${env.BUILD_ID}'
                            git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/rightrev/cd-qa-yamls.git
                        """
                    CInotifySlack("${channel}", "${branch} || *${env.IMAGE_NAME}* || Build-${currentBuild.id} || #${env.STAGE_NAME}# || *${currentBuild.currentResult}*", "green", "${branch}")
                }
            }
        }

    }
    else {
        print "Skipping ArgoCD deployment."
    }
    true
}
