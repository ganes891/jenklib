# lib-jenkins
Common routines for Jenkins which can be used by jobs.
To use these subroutines, the shared library has to be imported in respective Jenkinsfile.
`@Library('rightrev-lib') _`

## buildDocker()
Performs a docker build.
On successful build, tag the image with current build number.
Uses BRANCH_NAME, IMAAGE_NAME, BUILD_NUMBER, JOB_NAME

## testDocker( reportsFolder = 'reports' )
Runs the python unit test within the docker image built for current build.
On successful run, a test report file `pytest-report.xml` is created in `reporstFolder` folder.
Uses BRANCH_NAME, IMAAGE_NAME, BUILD_NUMBER, JOB_NAME

## testReport( reportsFolder = 'reports' )
Builds test summary report from already generated test report file(s) persent in `reportsFolder` folder.
On successful run, a slack message is sent to `#builds` channel with the test summary.
Uses BRANCH_NAME, IMAAGE_NAME, BUILD_NUMBER, JOB_NAME

## pushToECR()
For `develop` and `primary` branches, the docker image created for current build is pushed to AWS elastic container registry (ECR).
Uses BRANCH_NAME, IMAAGE_NAME, BUILD_NUMBER, JOB_NAME

## deployInK8S( deploymentFile = 'deployment.yaml', channel = '#central-dev-k8s' )
For `develop` and `primary` branches, the current build docker image from AWS ECR gets deployed into kubernetes env using the `deployment.yaml` file.
On successful deployment, the message is posted in slack channel.
Uses BRANCH_NAME, IMAAGE_NAME, BUILD_NUMBER, JOB_NAME

## notifySlack( channel, message, color )
Use this to send slack message.
Param `channel` has the slack channel name to post, eg. `#builds`
Param `message` has the message to be posted, eg. `validation for file content success`
Param `color` to be the status color used to post the message, eg. `red`. Supported colors are `red`, `yellow` and `green`.

## deployArgo()
For `develop` and `primary` branches, the current build image tag for k8s deployment is updated in the common repository maintained for ArgoCD.
On successful deployment, the message is posted in slack channel.
Uses BRANCH_NAME, IMAGE_NAME, BUILD_NUMBER, JOB_NAME

## postSlack( status )
Use this to send post build slack message.
Param `status` has the outcome status of the job, eg. `success`
Based on the outcome, corresponding status will be posted to `#builds` slack channel via `notifySlack` subroutine.
