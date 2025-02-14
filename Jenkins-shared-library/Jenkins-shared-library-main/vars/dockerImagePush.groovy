def call(project){
     withCredentials([usernamePassword(
             credentialsId: "QuayUser",
             usernameVariable: "USER",
             passwordVariable: "PASS"
     )]) {
         sh """sudo podman login -u '$USER' -p '$PASS' quay.io"""
         sh """sudo podman push quay.io/ganesan_kandasamy/${PROJECT}:${IMAGE_TAG}"""
     }
    // sh "sudo podman image push ${hubUser}/${project}:${imageTag}"  
     sh """sudo podman push quay.io/ganesan_kandasamy/${PROJECT}:${IMAGE_TAG}"""
 }