def call(Map config = [:]) {

    // Validate required parameters
    
    if (!config.projectName) { error "Parameter 'projectName' is required" }
    else { projectName = config.projectName }

    if (!config.component) { error "Parameter 'component' is required" }
    else { component = config.component }

    if (!config.imageTag) { error "Parameter 'imageTag' is required" }
    else { imageTag = config.imageTag }

    if (!config.credentialsId) { error "Parameter 'credentialsId' is required" }
    else { credentialsId = config.credentialsId }

    
    withCredentials([usernamePassword(
        credentialsId: credentialsId, 
        usernameVariable: 'dockerUser', 
        passwordVariable: 'dockerPass'
    )]) {

        echo "Building Docker Image"
        sh "docker build -t '${dockerUser}/${projectName}-${component}:${imageTag}' ."

        echo "Logging in to Docker Hub registry as ${dockerUser}"
        sh "echo '${dockerPass}' | docker login -u '${dockerUser}' --password-stdin"

        echo "Pushing Docker Image"
        sh "docker push '${dockerUser}/${projectName}-${component}:${imageTag}'"

        echo "Logging out from Docker Hub registry"
        sh "docker logout"
    }
}

