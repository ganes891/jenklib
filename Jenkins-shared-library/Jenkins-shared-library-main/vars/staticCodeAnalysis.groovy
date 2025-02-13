def call(credentialsId){
    withSonarQubeEnv(credentialsId: credentialsId) {
   sh "mvn clean package sonar:sonar"
   sh podman run --rm -v "$(pwd):/usr/src" --network="host" -e SONAR_HOST_URL="http://localhost:9000" -e SONAR_SCANNER_OPTS="-Dsonar.projectKey=sonar-test-app" -e SONAR_TOKEN="abcdefgh123456789"  sonarsource/sonar-scanner-cli
} 
}