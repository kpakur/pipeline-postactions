#!groovy

def go(){
    node() {
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
            error('INSIDE')
        }
    }

    stage('Confirm') {
        timeout(time: 3, unit: 'MINUTES') {
            input(message: 'Ok?')
        }

        error("tralalala");
    }

    node() {
        stage('stage 4 Done') {
            echo "stage 3"
            echo "Commit ID = " + commitId
            echo "stage 3"
            error('INSIDE after confirm')
        }
    } //node
}

return this;