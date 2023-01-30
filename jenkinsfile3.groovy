pipeline {
    agent any
    stages {
        stage("Destroy a specific machine") {
            steps {
                script {
                    sh "cd /var/n_instances/${build_num}"
                    sh "terraform destroy -auto-approve"
                }
            }
        }
    }
}