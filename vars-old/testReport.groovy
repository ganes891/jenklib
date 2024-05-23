
def call( String reportsFolder="reports" ) {
    def branch = env.BRANCH_NAME
    def branchInLowCase = env.BRANCH_NAME.toLowerCase()
    def image = env.IMAGE_NAME ?: "${env.JOB_NAME}".split('/')[0]
    def tag = currentBuild.id
    def channel = '#builds'
    def message = "null"
    def color = "null"
    def summary = "null"
    def notallowedBranches = ['demo','primary', 'sandbox']
    
    if (branch in notallowedBranches) {
        print "Skipping testreport for primary and sandbox branch"
    }


    else {
    
        print "Container Vulnerbility scan on (${branchInLowCase}/${image}:${tag})"
            print "${branchInLowCase}/${image}:${tag} image scanning................"
            print "============================= Vulnerbility scan report  ==========================================="
            sh """
                trivy --severity HIGH,CRITICAL --no-progress --ignore-unfixed ${branchInLowCase}/${image}:${tag} | tee './${image}:${tag}.report'
            """
            def vuln_scan_report = sh (returnStdout: true, script: "cat './${image}:${tag}.report' | grep Total")
            sh "rm -f './${image}:${tag}.report'" 
            print "${vuln_scan_report}" 
        
        
        

        print "testReport(${reportsFolder}, ${branch}/${image}:${tag})"
            dir(reportsFolder) {
                // cobertura coberturaReportFile: './coverage.xml', enableNewApi: true, lineCoverageTargets: '0, 0, 0'
                summary = junit testResults: '**/*.xml'
                print "testResult(*Test Summary* - ${summary.totalCount}, Failures: ${summary.failCount}, Skipped: ${summary.skipCount}, Passed: ${summary.passCount})"
                def failOrErrorCount = summary.totalCount - (summary.skipCount + summary.passCount)
                color = (failOrErrorCount ? 'red' : (summary.skipCount ? 'yellow' : 'green'))
                assert failOrErrorCount == 0
            }

        print "SonarAnalysis(${env.JOB_NAME}, ${image}/src)"
            def scannerHome = tool 'SonarQube'
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
                    -D sonar.sources=.  \
                    -D sonar.python.coverage.reportPaths=./reports/coverage.xml \
                    -D sonar.python.xunit.reportPaths=./reports/pytest-report.xml "
                        }

                    sleep 50
                    timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                        }
                            
            }
            def sonar_url = "https://codequality.rr-infra.net/dashboard?id=${image}&branch=${branch}"
            message = """
                    ${branch} || *${env.IMAGE_NAME}* || Build-${currentBuild.id} || #${env.STAGE_NAME}# || *${currentBuild.currentResult}*
                    *Test Summary* - ${summary.totalCount}, Failures: ${summary.failCount}, Skipped: ${summary.skipCount}, Passed: ${summary.passCount}
                    *SonarAnalysis*: *Passed* - (<${sonar_url}|Open>)
                    *Vulnerbility scan report* - ${vuln_scan_report}
                    """
            CInotifySlack(channel, message, color, branch)

    }
    true
}
