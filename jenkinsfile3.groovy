pipeline {
    agent any
    stages {
        stage("Destroy a specific machine") {
            steps {
                dir("/var/n_instances/${BUILD_NUMBER}") { 
                    sh 'terraform destroy -auto-approve'
                }
            }
        }
    }
}
