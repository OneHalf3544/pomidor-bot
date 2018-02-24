pipeline {
    agent any
    stages {
        stage('Build project') {
            steps {
                sh './gradlew build --info'
            }
        }
        stage('Create docker image') {
            steps {
                echo 'fake call command "./gradlew docker --info"'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}