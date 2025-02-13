sgctcrhclient02:

Server Details:
IP : 10.12.201.178
Hostname: sgctcrhclient02
Username: appuser
pa: ********
root: Feb@2025
os: Red Hat Enterprise Linux release 8.4 (Ootpa)
CPU: 8
RAM: 32G
App filesystem: /data

 
Jankins details: 
Jenkins : http://10.12.201.178:8080/manage/
User: opsuser
Pa: ********
10.12.201.178

#####



######
refer --> https://medium.com/@index23/start-sonarqube-server-and-run-analyses-locally-with-docker-4550eb7112a3

sonarqube:

podman run -dit --name sonarqube -p 9000:9000 sonarqube:latest

login admin/admin

Example command should look like this and run from project root:

docker run \
    --rm \
    -v "$(pwd):/usr/src" \
    --network="host" \
    -e SONAR_HOST_URL="http://localhost:9000" \
    -e SONAR_SCANNER_OPTS="-Dsonar.projectKey=sonar-test-app" \
    -e SONAR_TOKEN="abcdefgh123456789" \
    sonarsource/sonar-scanner-cli


#####
gitlab setup:

export GITLAB_HOME=/var/opt/gitlab

sudo docker run --detach \
  --hostname 10.12.201.178 \
  --publish 443:443 --publish 8081:80 --publish 22:22 \
  --name gitlab \
  --restart always \
  --volume $GITLAB_HOME/config:/etc/gitlab \
  --volume $GITLAB_HOME/logs:/var/log/gitlab \
  --volume $GITLAB_HOME/data:/var/opt/gitlab \
  gitlab/gitlab-ce:17.7.0-ce.0
  
Parameterized declarative pipeline as a SL
 https://johnson-tito.medium.com/streamlining-your-jenkins-pipeline-with-shared-libraries-b02c5987c55f

####
Quay.io
podman login -u='ganesan_kandasamy+ganesh891' -p='XQM76MUEDN3DUQKJBDAWRJA87EDF0JE4LJ9CAC3QHDR9O1NJKJUNCNEKJ3WN798Q' quay.io





