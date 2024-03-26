

def call( String reportsFolder="reports" ) {
    def branch = env.BRANCH_NAME
    def image = env.IMAGE_NAME ?: "${env.JOB_NAME}".split('/')[0]
    def tag = currentBuild.id

    print "testDocker(${reportsFolder}, ${branch}/${image}:${tag})"
    docker.image("${branch}/${image}:${tag}").inside{
        sh "pytest --junitxml=${reportsFolder}/pytest-report.xml"
    }
    true
}
