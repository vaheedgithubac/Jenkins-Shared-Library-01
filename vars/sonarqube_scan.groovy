def call(Map config = [:]) {

    // Validate required parameters
    /*
    if (!config.sonarQubeAPI) { error "Parameter 'sonarQubeAPI' is required" }
    else { sonarQubeAPI = config.sonarQubeAPI }

    if (!config.scannerHome) { error "Parameter 'scannerHome' is required" }
    else { scannerHome = config.scannerHome }

    if (!config.projectName) { error "Parameter 'projectName' is required" }
    else { projectName = config.projectName }

    if (!config.projectKey) { error "Parameter 'projectKey' is required" }
    else { projectKey = config.projectKey }
    */

    def sources      = config.sources ?: "."

    def required = [
        "sonarQubeAPI",
        "scannerHome",
        "projectName",
        "projectKey"
    ]

    required.each { key ->
        if (!config[key]) {
            error "❌ Sonarqube: Missing required parameter '${key}'"
        }
    }

    def sonarQubeAPI = config.sonarQubeAPI
    def scannerHome  = config.scannerHome
    def projectName  = config.projectName
    def projectKey   = config.projectKey

    withSonarQubeEnv(sonarQubeAPI) {
        sh """
            ${scannerHome}/bin/sonar-scanner \
            -Dsonar.projectName='${projectName}' \
            -Dsonar.projectKey='${projectKey}' -X
        """
    }
}
