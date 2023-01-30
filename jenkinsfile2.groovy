pipeline {
    agent any
    stages {
        stage("Create directory and file") {
            steps {
                script {
                    sh "mkdir /var/n_instances/${BUILD_NUMBER}"
                    sh "cd /var/n_instances/${BUILD_NUMBER} && terraform init"
                    sh "cp /var/instances/p2tf.tf /var/n_instances/${BUILD_NUMBER}"
                    sh "terraform apply -auto-approve"
                    archiveArtifacts artifacts: '/var/n_instances/${BUILD_NUMBER}/p2m-key'
                }
            }
        }
    }
}