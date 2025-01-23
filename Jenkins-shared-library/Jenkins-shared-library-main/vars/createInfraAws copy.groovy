def call(project){
     withCredentials([usernamePassword(
             credentialsId: "AWS_ACCESS_KEY",
             usernameVariable: "USER",
             passwordVariable: "PASS"
     )]) {
            sh """
                   terraform init
                   terraform plan 
               """
            }
     }
    
 