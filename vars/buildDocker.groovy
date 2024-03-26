
def call( ) {
    def current_build = currentBuild.id
    def branch = env.BRANCH_NAME.toLowerCase()
    def image = env.IMAGE_NAME ?: "${env.JOB_NAME}".split('/')[0]
    def allowedBranches = ['develop']
    def allowedImages = ['redplanet-pilot', 'rr-swagger', 'manualevents', 'dwh-reports', 'amortizer-router', 'bookings-release-trigger', 'cm-invoice-assigner', 'dm-invoice-assigner', 'holds', 'ragtag', 'revenue-accounting-daily', 'revenue-accounting-point-in-time', 'revenue-accounting-prorate', 'stamper', 'vixen', 'rr-assigner', 'friday', 'redplanet', 'billings-initial-entry', 'carves-accounting', 'billings-release-trigger', 'carves-calculator', 'contract-amendment', 'elrond-redux', 'events-order-assigner', 'generate-je', 'iam', 'invoice-order-assigner', 'oingest', 'policyset', 'prospective-ssp-adjuster', 'rc-metrics', 'rc-search', 'recognize-revenue-schedules', 'recorder-redux', 'sfdc-outbound-sync', 'shell', 'recorder', 'ssp-assigner', 'airsquid', 'governor']
    def repo = ""
    // check build number

    if (branch in allowedBranches) {
        if (image in allowedImages) {
 
        if ( branch == 'develop') 
        {
            repo = 'cd-dev-yamls'
        }
        
        else if ( branch == 'sandbox' )
        {
            repo = 'cd-sdx-yamls'
        }
        
        withCredentials([string(credentialsId: 'git_token', variable: 'git_token')]) 
        {
        sh """
            download_url=`curl -H "Accept: application/vnd.github.v3+json" -H "Authorization: token ${git_token}" https://api.github.com/repos/rightrev/${repo}/contents/k8sDeploymentFiles/${image}/deployment.yaml | grep -i download_url | awk '{print \$2}' | tr -d '"' | tr -d ','`;
            wget -O buildcheck.yaml \$download_url;
            """
        }

        image_num = sh (returnStdout: true, script: "cat buildcheck.yaml | grep -i registry.rr-infra.net/develop | head -n 1 | cut -d ':' -f 3")
        int last_build = "${image_num}".toInteger()
        int inc_num = 1
        int actual_build = last_build + inc_num
        int current_build_int = current_build.toInteger()

        if (actual_build !=  current_build_int) 
            {
                if (current_build_int == 1) 
        {

            channel = 'jenkins-build-num-alert'
            message = """
<!here>
*${branch}* || *${env.IMAGE_NAME}*
*Build Number Mismatch!*
Current Build Number - ${current_build} 
Last Build Number - ${last_build}
Build *${current_build}* aborted! Please set the correct build number.
"""
            CInotifySlack(channel, message, "red", branch)
        
            error("Build failed because of build number mismatch. Please contact Cloud-Ops.")
   
  
        }
            }
        else 
        {
            print "Build Number check passed successfully"
        }
        }

    }
    else
    {
        print "Skipping build number check"
    }

    // def branch = env.BRANCH_NAME

    def tag = currentBuild.id
    def channel = '#builds'
     sh " docker system prune -f"
     print "buildDocker(${branch}/${image}:${tag})"
     docker.build("${branch}/${image}:${tag}")
    true

    CInotifySlack("${channel}", "${branch} || *${env.IMAGE_NAME}* || Build-${currentBuild.id} || #${env.STAGE_NAME}# || *${currentBuild.currentResult}*", "green", "${branch}")


}
