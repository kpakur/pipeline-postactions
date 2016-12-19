#!groovy


    post {
        // No matter what the build status is, run these steps. There are other conditions
        // available as well, such as "success", "failed", "unstable", and "changed".
        always {
            notifyBuild("Always")
        }
    }

    stages {
        node('master') {
            stage('checkout') {
                notifyBuild()
                checkout scm
            }

            stage('stage 1') {
                echo 'stage 1'
            }

            stage('stage 2') {
                echo "stage 2"
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
    if (buildStatus == 'STARTED') {
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


