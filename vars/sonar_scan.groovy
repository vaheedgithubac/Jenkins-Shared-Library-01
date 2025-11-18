def call(Map config = [:]) {

    // Validate required parameters

    if (!config.sonarQubeAPI) { error "Parameter 'sonarQubeAPI' is required" }
    else { sonarQubeAPI = config.sonarQubeAPI }

    if (!config.scannerHome) { error "Parameter 'scannerHome' is required" }
    else { scannerHome = config.scannerHome }

    if (!config.projectName) { error "Parameter 'projectName' is required" }
    else { projectName = config.projectName }

    if (!config.projectKey) { error "Parameter 'projectKey' is required" }
    else { projectKey = config.projectKey }

    def sources      = config.sources ?: "."

    withSonarQubeEnv(sonarQubeAPI) {
        sh """
            ${scannerHome}/bin/sonar-scanner \
            -Dsonar.projectName='${projectName}' \
            -Dsonar.projectKey='${projectKey}' 
        """
    }
}
