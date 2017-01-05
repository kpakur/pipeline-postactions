#!groovy

commitId = ''
branchName = BRANCH_NAME
buildNumber = BUILD_NUMBER
buildVersion = '0.0.' + BUILD_NUMBER
if (branchName != 'master'){
    buildVersion =  BRANCH_NAME.replaceAll('/', '') +'.' + BUILD_NUMBER
}
currentBuild.displayName = buildVersion

echo '============= BRANCH=' + branchName
echo '============= BUILD NUMBER=' + buildNumber
echo '============= BUILD VERSION=' + buildVersion


node('master') {
    stage('checkout') {
        checkout scm
        commitId = sh script: 'git rev-parse HEAD', returnStdout: true
        echo "Commit ID = " + commitId
    }

    stage('<strong>stage 1</strong>') {
        echo 'stage 1'
    }
    deleteDir();
}
node('master') {
    stage('\u001B[34m stage 2') {
        echo "stage 2"
    }

    wrap([$class: 'AnsiColorBuildWrapper']) {
        stage('\u001B[35m stage 3') {
            echo '============= BRANCH=' + branchName
            echo '============= BUILD NUMBER=' + buildNumber
            echo '============= BUILD VERSION=' + buildVersion
            echo "Commit ID = " + commitId
            echo "stage 3"
        }
    }
}


try{
    notifyBuild('STARTED')
    pipeline()
    notifyBuild('SUCCESSFUL')
} catch (Exception ex){
    notifyBuild('FAILED')
} finally {
    notifyBuild('ENDED')
}

def pipeline(){
    node('master') {
        stage('checkout') {
            checkout scm
            commitId = sh script: 'git rev-parse HEAD', returnStdout: true
            echo "Commit ID = " + commitId
        }

        stage('<strong>stage 1</strong>') {
            echo 'stage 1'
        }
        deleteDir();
    }
    node('master') {
        stage('\u001B[34m stage 2') {
            echo "stage 2"
        }

        wrap([$class: 'AnsiColorBuildWrapper', colorMapName: 'xterm']) {
            stage('\u001B[35m stage 3') {
                echo '============= BRANCH=' + branchName
                echo '============= BUILD NUMBER=' + buildNumber
                echo '============= BUILD VERSION=' + buildVersion
                echo "Commit ID = " + commitId
                echo "stage 3"
            }
        }
    }

    input( message: 'Ok?')

    node('master') {
        stage('stage 4') {
            echo "stage 3"
        }
    } //node
}


def notifyBuild(String buildStatus = 'STARTED') {
    // this function needs to be used in the node, otherwise it cannot get the correct list of recipients.
    // build status of null means successful
    buildStatus = buildStatus ?: 'SUCCESSFUL'

    // Default values
    def colorCode = '#FF0000'
    // Override default values based on build status
    if (buildStatus == 'STARTED' || buildStatus == 'ENDED') {
        color = 'YELLOW'
        colorCode = '#FFFF00'
    } else if (buildStatus == 'SUCCESSFUL') {
        color = 'GREEN'
        colorCode = '#00FF00'
    } else {
        color = 'RED'
        colorCode = '#FF0000'
    }

    def subject = "TEST POST ACTIONS ${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
    def summary = "${subject} (${env.BUILD_URL})"
    def details = """<p>${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
    <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>"""

    // Send notifications
    slackSend(color: colorCode, message: summary)

}


