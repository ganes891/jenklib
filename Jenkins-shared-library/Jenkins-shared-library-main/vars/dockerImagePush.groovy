def call(project){
     withCredentials([usernamePassword(
             credentialsId: "QuayUser",
             usernameVariable: "USER",
             passwordVariable: "PASS"
     )]) {
         sh """podman login -u '$USER' -p '$PASS' quay.io"""
     }
    // sh "sudo podman image push ${hubUser}/${project}:${imageTag}"  
     sh """sudo podman push quay.io/ganesan_kandasamy/${PROJECT}:${IMAGE_TAG}"""
 }