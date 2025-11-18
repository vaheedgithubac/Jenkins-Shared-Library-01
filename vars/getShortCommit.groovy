def call() {
    try { return sh(script: "git rev-parse --short=7 HEAD", returnStdout: true).trim() } 
    catch (e) { return "no-sha" }
}
