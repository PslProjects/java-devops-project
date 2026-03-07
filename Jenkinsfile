pipeline {
    agent any

    stages {

        stage('Clone Code') {
            steps {
                git 'https://github.com/PslProjects/java-devops-project'
            }
        }

        stage('Build Jar') {
            steps {
                sh './mvnw clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t scada-app .'
            }
        }

        stage('Run Container') {
            steps {
                sh 'docker stop scada-container || true'
                sh 'docker rm scada-container || true'
                sh 'docker run -d -p 8888:8888 --name scada-container scada-app'
            }
        }

    }
}