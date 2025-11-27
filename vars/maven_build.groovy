def call(Map config = [:]) {
    // Normalize skipTests
    def skipTests = (config.skipTests in [true, 'true']) ? 'true' : 'false'
    def goals = config.goals ?: 'clean package'

    // Correct: steps.echo and steps.sh
    steps.echo "Running Maven: ${goals} -DskipTests=${skipTests}"

    try {
        steps.sh "mvn ${goals} -DskipTests=${skipTests}"
    } catch (Exception ex) {
        steps.echo "Maven build failed: ${ex.message}"
        error "Build failed"
    }
}

