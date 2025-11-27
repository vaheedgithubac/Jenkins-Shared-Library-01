def call(Map config = [:]) {

    // Validate required parameters
    def required = [
        "nexus_version", 
        "nexus_protocol", 
        "nexus_host", 
        "nexus_port",  
        "nexus_grp_id", 
        "nexus_artifact_version", 
        "nexus_credentials_id", 
        "nexus_base_repo"
    ]

    required.each { key ->
        if (!config[key]) {
            error "❌ Nexus: Missing required parameter '${key}'"
        }
    }

    def nexus_version          = config.nexus_version
    def nexus_protocol         = config.nexus_protocol
    def nexus_host             = config.nexus_host
    def nexus_port             = config.nexus_port
    def nexus_grp_id           = config.nexus_grp_id
    def nexus_artifact_version = config.nexus_artifact_version
    def nexus_credentials_id   = config.nexus_credentials_id
    def nexus_base_repo        = config.nexus_base_repo
    
    //  Read pom.xml
    def pom = steps.readMavenPom file: "pom.xml"

    def pom_artifactId  = pom.getArtifactId()                   // pom.artifactId
    def pom_version     = pom.getVersion()                      // pom.version
    def pom_name        = pom.getName() ?: pom.getArtifactId()  // pom.name ?: pom_artifactId
    def pom_groupId     = pom.getGroupId()                      // pom.groupId
    def pom_packaging   = pom.getPackaging()                    // pom.packaging

    def filesByGlob = steps.findFiles(glob: "target/*.${pom_packaging}")

    if (!filesByGlob || filesByGlob.size() == 0) {
        error "❌ No artifact found in /target directory with extension *.${pom_packaging}"
    }
                    
    steps.echo "File Path: ${filesByGlob[0].path}"
    steps.echo "File Name: ${filesByGlob[0].name}"
    steps.echo "Is Directory: ${filesByGlob[0].directory}"
    steps.echo "File Length: ${filesByGlob[0].length}"
    steps.echo "File Last Modified: ${filesByGlob[0].lastModified}"

    def artifactPath = filesByGlob[0].path;
    def artifactExists = steps.fileExists(artifactPath)
    def final_nexus_repo = pom_version.endsWith("SNAPSHOT") ? "${nexus_base_repo}-SNAPSHOT" : "${nexus_base_repo}-RELEASE"

    steps.echo "📤 Uploading to Nexus repository: ${final_nexus_repo}"

    if(artifactExists) {
        steps.nexusArtifactUploader(
            nexusVersion: nexus_version,
            protocol: nexus_protocol,
            nexusUrl: "${nexus_host}:${nexus_port}",
            groupId: nexus_grp_id,                               
            version: nexus_artifact_version,
            repository: final_nexus_repo, 
            credentialsId: nexus_credentials_id,

            artifacts: [
               [artifactId: pom_artifactId,
                classifier: '',
                file: artifactPath,
                type: pom_packaging]
            ]
        )
        steps.echo "✅ Nexus upload complete."
    } 
    else { error "❌*** File: ${artifactPath}, could not be found" }
}
