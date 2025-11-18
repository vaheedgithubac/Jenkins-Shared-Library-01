def call(Map config = [:]) {
    timeout(time: config.timeoutMinutes ?: 5, unit: "MINUTES") {
        def qg = waitForQualityGate(abortPipeline: false)
        
        echo "SonarQube Quality Gate status: ${qg.status}"
        
        //if (qg.status != 'OK') {
        //    error "Pipeline aborted due to Quality Gate failure: ${qg.status}"
        //}
    }
}


// Wait for SonarQube Quality Gate
//    def qg = waitForQualityGate()
//    if (qg.status != 'OK') {
//        error "Pipeline aborted due to SonarQube Quality Gate failure: ${qg.status}"
//    }
