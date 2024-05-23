
def call( String projectName='template-python' ) {
    def project = Jenkins.instance.getItem( projectName )
    project.items.each { job -> deleteAllBuilds( job ) }
    true
}

def deleteAllBuilds(Object job) {
    job.builds().each { build -> build.delete() }
    job.nextBuildNumber = 1
    job.save()
    true
}
