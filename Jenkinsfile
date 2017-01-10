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


// get sources so pipelines are downloaded too
node('master') {
    stage('checkout') {
        checkout scm
        commitId = sh script: 'git rev-parse HEAD', returnStdout: true
        echo "Commit ID = " + commitId

        stash name: "ops-pipeline", includes: "ops-pipeline/**/*"

        def standardFlow = load('ops-pipeline/utils.groovy')

        deleteDir();
    }
}

try{
    utils.notifyBuild('STARTED')
    pipelineStandard()
    //pipelineProd()
    utils.notifyBuild('SUCCESSFUL')
} catch (Exception ex){
    utils.notifyBuild('FAILED')
} finally {
    utils.notifyBuild('ENDED')
}

def pipelineStandard(){
    node('master') {
        stage('checkout') {
            unstash name: "ops-pipeline"
        }

        def standardFlow = load('ops-pipeline/standard.groovy')

        deleteDir();
    }

    standardFlow.go();
}






