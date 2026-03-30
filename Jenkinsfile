pipeline {
    agent any

    environment {
        DOCKER_IMAGE  = "krishna07k/springboot-app"
        APP_SERVER_IP = "54.79.252.162"
        APP_USER      = "ubuntu"
    }

    stages {

        stage('Code Pull') {
            steps {
                echo 'GitHub se code aa raha hai...'
                checkout scm
            }
        }

        stage('JAR Build') {
            steps {
                echo 'Maven se JAR ban raha hai...'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Image Build') {
            steps {
                echo 'Docker image ban rahi hai...'
                sh "docker build -t ${DOCKER_IMAGE}:${BUILD_NUMBER} ."
                sh "docker tag ${DOCKER_IMAGE}:${BUILD_NUMBER} ${DOCKER_IMAGE}:latest"
            }
        }

        stage('Docker Hub Push') {
            steps {
                echo 'Docker Hub pe push ho raha hai...'
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-creds',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin"
                    sh "docker push ${DOCKER_IMAGE}:latest"
                }
            }
        }

        stage('App Server pe Deploy') {
            steps {
                echo 'App server pe deploy ho raha hai...'
                withCredentials([sshUserPrivateKey(
                    credentialsId: 'app-server-ssh',
                    keyFileVariable: 'SSH_KEY'
                )]) {
                    sh """
                        ssh -i ${SSH_KEY} \
                          -o StrictHostKeyChecking=no \
                          ${APP_USER}@${APP_SERVER_IP} '
                            docker pull ${DOCKER_IMAGE}:latest &&
                            docker stop springboot-app || true &&
                            docker rm   springboot-app || true &&
                            docker run -d \
                              --name springboot-app \
                              --restart always \
                              -p 8080:8080 \
                              -v /home/ubuntu/config:/app/config \
                              ${DOCKER_IMAGE}:latest
                          '
                    """
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline successful! App live hai.'
        }
        failure {
            echo 'Kuch gadbad hua — logs dekho.'
        }
    }
}