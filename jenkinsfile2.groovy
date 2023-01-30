pipeline {
    agent any
    stages {
        stage("Create directory") {
            steps {               
                script {
                    sh "mkdir /var/n_instances/${BUILD_NUMBER}" 
                    sh "cp /var/n_instances/highest /var/n_instances/${BUILD_NUMBER}"
                    sh "cp /var/n_instances/p2tf.tf /var/n_instances/${BUILD_NUMBER}"					
                }
            }
        }
		stage("Create directory, init tf, start instance and show key and details") {
            steps {
                dir("/var/n_instances/${BUILD_NUMBER}") { 
                    sh 'terraform init'
					}
				}
            }
		stage("Do the needful") {
            steps {               
                dir("/var/n_instances/${BUILD_NUMBER}") { 
                    sh "cd /var/n_instances/${BUILD_NUMBER} && terraform init && terraform plan"
                    sh "terraform apply -auto-approve"
                }
            }
          }            
        }
    }
