pipeline {
    agent any
    stages {
        stage('Generate Random Numbers') {
            parallel {
                stage('Number 1') {
                    steps {
                        script {
                            int number1 = (Math.random()*30000+20000)
                            env.NUMBER1 = number1
                        }
                    }
                }
                stage('Number 2') {
                    steps {
                        script {
                            int number2 = (Math.random()*30000+20000)
                            env.NUMBER2 = number2
                        }
                    }
                }
                stage('Number 3') {
                    steps {
                        script {
                            int number3 = (Math.random()*30000+20000)
                            env.NUMBER3 = number3
                        }
                    }
                }
                stage('Number 4') {
                    steps {
                        script {
                            int number4 = (Math.random()*30000+20000)
                            env.NUMBER4 = number4
                        }
                    }
                }
				stage('Number 5') {
                    steps {
                        script {
                            int number5 = (Math.random()*30000+20000)
                            env.NUMBER5 = number5
                        }
                    }
                }
				stage('Number 6') {
                    steps {
                        script {
                            int number6 = (Math.random()*30000+20000)
                            env.NUMBER6 = number6
                        }
                    }
                }
				stage('Number 7') {
                    steps {
                        script {
                            int number7 = (Math.random()*30000+20000)
                            env.NUMBER7 = number7
                        }
                    }
                }
				stage('Number 8') {
                    steps {
                        script {
                            int number8 = (Math.random()*30000+20000)
                            env.NUMBER8 = number8
                        }
                    }
                }
				stage('Number 9') {
                    steps {
                        script {
                            int number9 = (Math.random()*30000+20000)
                            env.NUMBER9 = number9
                        }
                    }
                }
				stage('Number 10') {
                    steps {
                        script {
                            int number10 = (Math.random()*30000+20000)
                            env.NUMBER10 = number10
                        }
                    }
                }
            }
        }
        stage('Choose Higher Number') {
            steps {
                script {
                    def numbers = [env.NUMBER1, env.NUMBER2, env.NUMBER3, env.NUMBER4, env.NUMBER5, env.NUMBER6, env.NUMBER7, env.NUMBER8, env.NUMBER9, env.NUMBER10]
                    def highest = numbers.sort()[-1]
                    env.HIGHEST_NUMBER = highest
                    writeFile file: 'highest', text: highest.toString()
                }
            }
        }
        stage('Keep as Artifact') {
            steps {
                script {
                    archiveArtifacts 'highest'
                }
            }
        }
        stage('copy') {
            steps {
                sh "cp highest /var/n_instance"
            }
        }
    }
}
