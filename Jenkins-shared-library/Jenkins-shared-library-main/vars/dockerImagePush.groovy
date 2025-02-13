def call(project){
     withCredentials([usernamePassword(
             credentialsId: "hubUser",
             usernameVariable: "USER",
             passwordVariable: "PASS"
     )]) {
         sh "sudo podman login -u '$USER' -p '$PASS'"
     }
     sh "sudo podman image push ${hubUser}/${project}:${imageTag}"  
     sudo "podman push quay.io/ganesan_kandasamy/${IMAGE_REPO_NAME}:${IMAGE_TAG}"
 }