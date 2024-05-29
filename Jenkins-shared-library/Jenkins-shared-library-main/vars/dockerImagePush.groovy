def call(project){
     withCredentials([usernamePassword(
             credentialsId: "hubUser",
             usernameVariable: "USER",
             passwordVariable: "PASS"
     )]) {
         sh "sudo docker login -u '$USER' -p '$PASS'"
     }
     sh "sudo docker image push ${hubUser}/${project}:${imageTag}"  
 }