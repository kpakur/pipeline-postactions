#!groovy

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

    def subject = "TEST2 POST ACTIONS ${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
    def summary = "${subject} (${env.BUILD_URL})"
    def details = """<p>${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
    <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>"""

    // Send notifications
    slackSend(color: colorCode, message: summary)

}

return this;