def call(String Project, String ImageTag, String hubUser){
     withCredentials([usernamePassword(
             credentialsId: "hubUser",
             usernameVariable: "USER",
             passwordVariable: "PASS"
     )]) {
         sh "docker login -u '$USER' -p '$PASS'"
     }
     sh "docker image push ${hubUser}/${Project}:${ImageTag}"
     sh "docker image push ${hubUser}/${Project}:latest"   
 }