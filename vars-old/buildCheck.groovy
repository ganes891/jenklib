
def call(){
    def current_build = currentBuild.id
    def branch = env.BRANCH_NAME
    def image = env.IMAGE_NAME ?: "${env.JOB_NAME}".split('/')[0]
    def repo = ""
    def allowedImages = ['alerts']

    // check build number

    if (image in allowedImages) {

        
        if ( branch == 'develop') 
        {
            repo = 'cd-dev-yamls'
        }
        
        else if ( branch == 'sandbox' )
        {
            repo = 'cd-sdx-yamls'
        }
        
        withCredentials([string(credentialsId: 'git_token', variable: 'git_token')]) {
        sh """
            download_url=`curl -H "Accept: application/vnd.github.v3+json" -H "Authorization: token ${git_token}" https://api.github.com/repos/rightrev/${repo}/contents/k8sDeploymentFiles/${image}/deployment.yaml | grep -i download_url | awk '{print \$2}' | tr -d '"' | tr -d ',' `;
            wget -O buildcheck.yaml \$download_url;
            """
        }
        image_num = sh (returnStdout: true, script: "cat buildcheck.yaml | grep -i registry.rr-infra.net/develop | head -n 1 | cut -d ':' -f 3")
        int last_build = "${image_num}".toInteger()
        print last_build.getClass()
        int num = 1
        int actual_build = last_build + num
        if (actual_build !=  current_build) {
            print "${actual_build} not eqaul to ${current_build}"
            print "Aborting build due to build number mismatch. Please contact Cloud-Ops"
            currentBuild.result = 'FAILURE'  
        }
        else {
            print "-----------------"
            print "${current_build}"
            print "${last_build}"
            print "${actual_build}"
            print "-----------------"  
        }

    }
    else
    {
        print "Skipping build number check"
    }