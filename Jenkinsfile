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
	stage('ask no node'){
		def choice = new ChoiceParameterDefinition('Param name', ['option1', 'option2'] as String[], 'Description')
		def userInput = input(message: 'Select one', parameters: [choice])
		echo userInput
	}


stage('ask combo and checkboxes'){
	def choiceParam = new ChoiceParameterDefinition('Param name', ['option1', 'option2'] as String[], 'Description here')
	def runSmokeParam = booleanParam(defaultValue: true, description: '', name: 'runSmoke')
	def runFuncParam = booleanParam(defaultValue: true, description: '', name: 'runFunc')
	def runParallelFuncParam = booleanParam(defaultValue: true, description: '', name: 'runParallelFunc')
	def runPerf = booleanParam(defaultValue: true, description: '', name: 'runPerf')

	def userInput = input(id: 'Xxid', message: 'What to run', ok: 'OK great', parameters: [choiceParam, runSmokeParam, runFuncParam, runParallelFuncParam, runPerf])
	echo userInput
}

	stage('ask2 no node'){
		def userInput = 'random';
            timeout(time: 60, unit: 'MINUTES') {
				def choicePMC = choice(choices: "random\npmc\npmc2\npmc3\npmc4\n", description: 'The best to check locks to find which is available or just try a random one if you are feeling lucky', name: 'env-to-run');
				userInput = input(message: 'Deploy to PMC (pre master check) environment?', parameters: [choicePMC])
            }
			
			echo "Selected env ${userInput}"
			
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

node() {
	stage('ask'){
		def choice = new ChoiceParameterDefinition('Param name', ['option1', 'option2'] as String[], 'Description')
		def userInput = input(message: 'Select one', parameters: [choice])
		echo userInput
	}
	
	stage('ask2'){
		def userInput = 'random';
            timeout(time: 60, unit: 'MINUTES') {
				def choicePMC = choice(choices: "random\npmc\npmc2\npmc3\npmc4\n", description: 'The best to check locks to find which is available or just try a random one if you are feeling lucky', name: 'env-to-run');
				userInput = input(message: 'Deploy to PMC (pre master check) environment?', parameters: [choicePMC])
            }
			
			echo "Selected env ${userInput}"
			
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



