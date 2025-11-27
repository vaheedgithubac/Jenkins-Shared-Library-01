def call(Map config = [:]) {

    // --------------------------------
    // 1️⃣ Validate required parameters
    // --------------------------------
    def required = [
        "project_name",
        "component",
        "mode",
        "output_report_format",
        "target"
    ]

    required.each { key ->
        if (!config[key]) {
            error "❌ Trivy: Missing required parameter '${key}'"
        }
    }

    def project_name = config.project_name
    def component    = config.component
    def mode         = config.mode
    def format       = config.output_report_format
    def target       = config.target

    // -----------------------------------
    // 2️⃣ Determine proper file extension
    // -----------------------------------
    def ext = [
        "table": "txt",
        "json" : "json",
        "sarif": "sarif",
        "yaml" : "yaml"
    ][format] ?: format  // fallback to format if unknown

    def output_report = "${project_name}-${component}-${mode}.${ext}"

    // -------------------------
    // 3️⃣ Log info
    // -------------------------
    steps.echo "🛡 Running Trivy scan"
    steps.echo "📄 Output: ${output_report}"
    steps.echo "🎯 Target: ${target}"

    // ----------------------------------------------------
    // 4️⃣ Run Trivy safely (handle any special characters)
    // ----------------------------------------------------
    steps.sh(
        script: [
            "trivy",
            mode,
            "--format", format,
            "--output", output_report,
            "--severity", "MEDIUM,HIGH,CRITICAL",
            target
        ],
        returnStdout: false
    )

    steps.echo "✅ Trivy scan completed successfully."
}
