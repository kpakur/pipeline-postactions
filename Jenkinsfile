#!groovy

commitId = ''
branchName = BRANCH_NAME
buildNumber = BUILD_NUMBER
buildVersion = "0.${BUILD_NUMBER}.0.0"
if (branchName != 'master'){
    buildVersion =  BRANCH_NAME.replaceAll('/', '') +'.' + BUILD_NUMBER
}
currentBuild.displayName = buildVersion

echo '============= BRANCH=' + branchName
echo '============= BUILD NUMBER=' + buildNumber
echo '============= BUILD VERSION=' + buildVersion


def notifyBuild(String buildStatus = 'STARTED', String additionalMessage = '') {
    // this function needs to be used in the node, otherwise it cannot get the correct list of recipients.

    def colorCode = '#FF0000' //red
    if (buildStatus == 'STARTED') {
        colorCode = '#FFFF00' //yellow
    } else if (buildStatus == 'SUCCESSFUL') {
        colorCode = '#00FF00' //gree
    }

    def jName = env.JOB_NAME.replaceAll('%2F', '/')
    def subject = "${buildStatus}: Job '${jName} [${env.BUILD_NUMBER}]'"
    def summary = "${subject} (${env.BUILD_URL}) ${additionalMessage}"
    def details = """<p>${buildStatus}: Job '${jName} [${env.BUILD_NUMBER}]':</p>
    <p>Build output at &QUOT;<a href='${env.BUILD_URL}'>${jName} [${env.BUILD_NUMBER}]</a>&QUOT;</p>
    <p>Console output at &QUOT;<a href='${env.BUILD_URL}console'>${jName} [${env.BUILD_NUMBER}]</a>&QUOT;</p>
    <p>${additionalMessage}</p>"""

    slackSend(color: colorCode, message: summary)
}


// get sources so pipelines are downloaded too
node('master') {
	stage('ask'){
		def choice = new ChoiceParameterDefinition('Param name', ['option1', 'option2'] as String[], 'Description')
		def userInput = input(message: 'Select one', parameters: [choice])
		echo userInput
	}
	
	stage('ask2'){
		def userInput = 'random';
            timeout(time: 60, unit: 'MINUTES') {
				def choicePMC = choice(choices: "random\npmc\npmc2\npmc3\npmc4\n", description: 'You may need to wait a while until PMC is available for you. The best to check locks list which is available or just try a random one if you are feeling lucky', name: 'selectpmc');
				userInput = input(message: 'Do you want to deploy to PMC (pre master check) environment?', parameters: [choicePMC])
            }
			
			if (userInput == 'random') {
				// if yes then use PMC environment
				def pmcNo = System.currentTimeMillis() % 4
				if (pmcNo == 0) {
					environment = 'pmc'
					environmentCreds = 'pmc'
				} else {
					environment = 'pmc' + pmcNo
					environmentCreds = 'pmc'
				}
			} else {
				environment = userInput
				environmentCreds = 'pmc'
			}
			
            echo "Deploying to ${environment}"
	}
	
    stage('checkout') {
        checkout scm
        commitId = sh script: 'git rev-parse HEAD', returnStdout: true
        echo "Commit ID = " + commitId

        stash name: "ops-pipeline", includes: "ops-pipeline/**/*"

        utils = load('ops-pipeline/utils.groovy')
        standardFlow = load('ops-pipeline/standard.groovy')

        deleteDir();
    }
}







try{
    notifyBuild('STARTED')
    pipelineStandard()
    //pipelineProd()
    notifyBuild('SUCCESSFUL')
} catch (Exception ex){
    notifyBuild('FAILED', ex.getMessage())
    throw ex;
} finally {
    notifyBuild('ENDED')
}

def pipelineStandard(){
    node('master') {
        stage('nothig') {
            //unstash name: "ops-pipeline"
        }


        deleteDir();
    }

    standardFlow.go();
}



