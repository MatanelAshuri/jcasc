pipeline {
    agent any
    stages {
        stage("Destroy a specific machine") {
            steps {
                dir("/var/n_instances/${buile_number}") { 
                    sh 'terraform destroy -auto-approve'
                }
            }
        }
    }
}
