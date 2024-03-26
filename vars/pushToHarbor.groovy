// Function to gather SCM changes for the build - added as per OPS-150 ## Do NOT remove @NonCPS 
@NonCPS
def getChanges() {
                MAX_MSG_LEN = 300
                def changeString = ""
                def msgChangeString = ""
                def prevChangeString = ""                
                echo "Gathering SCM changes"
                def changeLogSets = currentBuild.changeSets
                for (int i = 0; i < changeLogSets.size(); i++) {
                    def entries = changeLogSets[i].items
                    for (int j = 0; j < entries.length; j++) {
                        def entry = entries[j]
                        def count = j + 1 
                        truncated_msg = entry.msg.take(MAX_MSG_LEN)
                        def commitId = entry.commitId
                        def commit = commitId.substring(0,7)
                        def baseURL = entry.parent.browser.url
                        changeString += "- [<${baseURL}commit/${commitId}|${commit}>] ${truncated_msg} [${entry.author}]\n"
                    }
                }
                if (!changeString) {
                    msgChangeString = "No new changes, build not triggered by SCM. Hence, printing changes from previous SCM Build #"
                    def build = currentBuild.previousBuild
                    while (build != null) {
                        changeLogSets = build.changeSets
                        for (int i = 0; i < changeLogSets.size(); i++) {
                            def entries = changeLogSets[i].items
                            for (int j = 0; j < entries.length; j++) {
                                def entry = entries[j]
                                def count = j + 1 
                                truncated_msg = entry.msg.take(MAX_MSG_LEN)
                                def commitId = entry.commitId
                                def commit = commitId.substring(0,7)
                                def baseURL = entry.parent.browser.url
                                prevChangeString += "- [<${baseURL}commit/${commitId}|${commit}>] ${truncated_msg} [${entry.author}]\n"
                            }
                        }
                        if (!prevChangeString) {
                            print("Previous Build has no SCMChanges, Loop should continue backwards further..")
                        }
                        else {
                            buildID= build.id as Integer
                            changeString = "_${msgChangeString}${buildID}_ \n\n${prevChangeString}"
                            break
                        }                                                                             
                        build = build.previousBuild
                    }
                }   
                return changeString 
}

// Function to Push Image to the Repository ECR/Harbor
def call( ) {
    def branch = env.BRANCH_NAME
    def image = env.IMAGE_NAME ?: "${env.JOB_NAME}".split('/')[0]
    def tag = currentBuild.id
    def channel = '#builds'
    def lttag = 'latest'
    def changeBranch = env.BRANCH_NAME
    def changeChannel = '#builds-scm-changes'
    def harborHost = 'registry.rr-infra.net'

    print "pushToECR(${env.JOB_NAME}, ${branch}/${image}:${tag})"
    def allowedBranches = ['develop', 'sandbox']

    //Publish Recent changes to Slack Channel - Begin processing the message

    def changeStr =  getChanges()               
    message = """
                ${changeBranch} || *${env.IMAGE_NAME}* || Build-${currentBuild.id} || SCMChanges || (<${env.JOB_URL}/changes|Open>) \n\n\n*Changes:* \n${changeStr} \n\n
              """
    //End of processing message to publish

    if (branch in allowedBranches) {

      sh "docker images"

        if ( branch == 'develop' ) {
            // Push to Develop Harbor and this will be used by PT env as well
            docker.withRegistry("https://${harborHost}", "harbor_creds") {
                 sh "docker tag ${branch}/${image}:${tag} ${harborHost}/${branch}/${image}"
                 def dockerImage = docker.image("${harborHost}/${branch}/${image}")
                 dockerImage.push("${tag}")
            }
                // Push Develop 'latest' tagged image
                docker.withRegistry("https://${harborHost}", "harbor_creds") {
                    sh "docker tag ${branch}/${image}:${tag} ${harborHost}/${branch}/${image}"
                    def dockerImage = docker.image("${harborHost}/${branch}/${image}")
                    dockerImage.push("${lttag}")
                 }
             
             CInotifySlack("${channel}", "${branch} || *${env.IMAGE_NAME}* || Build-${currentBuild.id} || #${env.STAGE_NAME}# || *${currentBuild.currentResult}*", "green", "${branch}")

            // Push to QA ECR
            def target_branch='qa'
            docker.withRegistry("https://${harborHost}", "harbor_creds") {
                sh "docker tag ${branch}/${image}:${tag} ${harborHost}/${target_branch}/${image}"
                def dockerImage = docker.image("${harborHost}/${target_branch}/${image}")
                dockerImage.push("${tag}")
            }

                // Push QA 'latest' tagged image
                 docker.withRegistry("https://${harborHost}", "harbor_creds") {
                    sh "docker tag ${branch}/${image}:${tag} ${harborHost}/${target_branch}/${image}"
                    def dockerImage = docker.image("${harborHost}/${target_branch}/${image}")
                    dockerImage.push("${lttag}")
                }
            
            branch='qa'
            CInotifySlack("${channel}", "${branch} || *${env.IMAGE_NAME}* || Build-${currentBuild.id} || #${env.STAGE_NAME}# || *${currentBuild.currentResult}*", "green", "${branch}")
        }

        else if ( branch == 'sandbox') {
            docker.withRegistry("https://${harborHost}", "harbor_creds") {
                 sh "docker tag ${branch}/${image}:${tag} ${harborHost}/${branch}/${image}"
                 def dockerImage = docker.image("${harborHost}/${branch}/${image}")
                 dockerImage.push("${tag}")
            }

                // Push 'latest' tagged image
                docker.withRegistry("https://${harborHost}", "harbor_creds") {
                    sh "docker tag ${branch}/${image}:${tag} ${harborHost}/${branch}/${image}"
                    def dockerImage = docker.image("${harborHost}/${branch}/${image}")
                    dockerImage.push("${lttag}")
                }
            
            CInotifySlack("${channel}", "${branch} || *${env.IMAGE_NAME}* || Build-${currentBuild.id} || #${env.STAGE_NAME}# || *${currentBuild.currentResult}*", "green", "${branch}")
        }
    // Calling SlackNotify to publish SCMchanges to #builds-scm-changes channel
    CInotifySlack(changeChannel, message, "green", changeBranch)
    }

    else {
        print "Skipping image push to ECR for branch ${branch}."
    }
    true
}
