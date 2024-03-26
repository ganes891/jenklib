def call (String status) {

    def branch = env.BRANCH_NAME
    def image = env.IMAGE_NAME ?: "${env.JOB_NAME}".split('/')[0]
    def channel = '#builds'
    def result = 'success'
    sh """ 
    docker kill \$(docker ps -a -q) || true
    sleep 2
    docker system prune -f || true
    sleep 2
    docker network prune -f || true
    sleep 2
    """
    if ( status == result ) {
        CInotifySlack("${channel}", "${branch} || *${env.IMAGE_NAME}* || Build-${currentBuild.id} || #JobStatus -> *PASSED* - (<${env.BUILD_URL}|Open>)", "green", "${branch}" )
    }
    else {
    CInotifySlack("${channel}", "${branch} || *${env.IMAGE_NAME}* || Build-${currentBuild.id} || #JobStatus -> *FAILED* - (<${env.BUILD_URL}|Open>)", "red", "${branch}")
    }
}
