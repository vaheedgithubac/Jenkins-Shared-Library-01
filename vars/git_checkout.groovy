def call(Map config = [:]) {

    // Validate required parameters
    if (!config.gitUrl) { error "Parameter 'gitUrl' is required" }
    else { gitUrl = config.gitUrl }

    def gitBranch        = config.gitBranch ?: 'main'
    def gitCredentialsId = config.gitCredentialsId ?: null

    // Perform Git checkout
    git(
        url: gitUrl,
        branch: gitBranch,
        credentialsId: gitCredentialsId
    )
    
    echo "Checked out Branch:'${gitBranch}' from ${gitUrl} Successfully..."
}
