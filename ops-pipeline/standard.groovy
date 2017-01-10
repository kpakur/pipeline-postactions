#!groovy

def go(){
    node('master') {
        stage('checkout') {
            checkout scm
            commitId = sh script: 'git rev-parse HEAD', returnStdout: true
            echo "Commit ID = " + commitId
        }

        deleteDir();

        stage('stage 3 Echos') {
            echo '============= BRANCH=' + branchName
            echo '============= BUILD NUMBER=' + buildNumber
            echo '============= BUILD VERSION=' + buildVersion
            echo "Commit ID = " + commitId
            echo "stage 3"
        }
    }

    stage('Confirm') {
        timeout(time: 10, unit: 'MINUTES') {
            input(message: 'Ok?')
        }
    }

    node('master') {
        stage('stage 4 Done') {
            echo "stage 3"
        }
    } //node
}

return this;