#!groovy

def go(){
    node('master') {
        stage('empty') {
            //checkout scm
            //commitId = sh script: 'git rev-parse HEAD', returnStdout: true
            echo "Commit ID = " + commitId
        }

        deleteDir();

        stage('stage 3 Echos') {
            echo '============= BRANCH=' + branchName
            echo '============= BUILD NUMBER=' + buildNumber
            echo '============= BUILD VERSION=' + buildVersion
            echo "Commit ID = " + commitId
            echo "stage 3"
            throw new Exception('INSIDE')
        }
    }

    stage('Confirm') {
        timeout(time: 3, unit: 'MINUTES') {
            input(message: 'Ok?')
        }

        throw new Exception("tralalala");
    }

    node('master') {
        stage('stage 4 Done') {
            echo "stage 3"
            echo "Commit ID = " + commitId
            echo "stage 3"
            throw new Exception('INSIDE after confirm')
        }
    } //node
}

return this;