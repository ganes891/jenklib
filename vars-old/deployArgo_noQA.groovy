def call() {
    def branch = env.BRANCH_NAME
    def image = env.IMAGE_NAME ?: "${env.JOB_NAME}".split('/')[0]
    def tag = currentBuild.id
    def deploymentFile = "k8sDeploymentFiles/${image}/deployment.yaml"
    def channel = '#builds'

    print "deployArgo(${env.JOB_NAME}, ${branch}/${image}:${tag})"
    def allowedBranches = ['develop', 'sandbox', 'primary','demo']
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
            sh """
                cd deployment
                git clone https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/rightrev/cd-sdx-yamls.git
                cd cd-sdx-yamls
                sed -i 's/${branch}\\/${image}:.*/${branch}\\/${image}:${tag}/g' ${deploymentFile}
                git add .
                git commit -m 'CI - Modified image tag ${env.BUILD_ID}'
                git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/rightrev/cd-sdx-yamls.git
                sleep 2
            """
            }
              CInotifySlack("${channel}", "${branch} || *${env.IMAGE_NAME}* || Build-${currentBuild.id} || #${env.STAGE_NAME}# || *${currentBuild.currentResult}*", "green", "${branch}")

        }

        else if ( branch == 'primary' ) {

           withCredentials([usernamePassword(credentialsId: 'Self_use_readonly_token', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]){
            sh """
                cd deployment
                git clone https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/rightrev/cd-prod-yamls.git
                cd cd-prod-yamls
                sed -i 's/${branch}\\/${image}:.*/${branch}\\/${image}:${tag}/g' ${deploymentFile}
                git add .
                git commit -m 'CI - Modified image tag ${env.BUILD_ID}'
                git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/rightrev/cd-prod-yamls.git
                sleep 2
            """
            }
              CInotifySlack("${channel}", "${branch} || *${env.IMAGE_NAME}* || Build-${currentBuild.id} || #${env.STAGE_NAME}# || *${currentBuild.currentResult}*", "green", "${branch}")

        }

        else if ( branch == 'demo' ) {

           withCredentials([usernamePassword(credentialsId: 'Self_use_readonly_token', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]){
            sh """
                cd deployment
                git clone https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/rightrev/cd-demo-yamls.git
                cd cd-demo-yamls
                sed -i 's/${branch}\\/${image}:.*/${branch}\\/${image}:${tag}/g' ${deploymentFile}
                git add .
                git commit -m 'CI - Modified image tag ${env.BUILD_ID}'
                git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/rightrev/cd-demo-yamls.git
                sleep 2
                /usr/local/bin/argocd --grpc-web app sync demo-${image} --force || true
            """
            }
              CInotifySlack("${channel}", "${branch} || *${env.IMAGE_NAME}* || Build-${currentBuild.id} || #${env.STAGE_NAME}# || *${currentBuild.currentResult}*", "green", "${branch}")

        }
        else if ( branch == 'develop' ) {

           // Updating Develop ArgoRepo
           withCredentials([usernamePassword(credentialsId: 'Self_use_readonly_token', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]){
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
           }

           CInotifySlack("${channel}", "${branch} || *${env.IMAGE_NAME}* || Build-${currentBuild.id} || #${env.STAGE_NAME}# || *${currentBuild.currentResult}*", "green", "${branch}")

        }

    }
    else {
        print "Skipping ArgoCD deployment."
    }
    true
}
