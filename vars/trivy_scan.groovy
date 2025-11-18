def call(Map config = [:]) {

	// Validate required parameters
	if (!config.mode) { error "Parameter 'mode' is required ('fs' or 'image')" }
	else { mode = config.mode }

	if (!config.output_report_format) { error "Parameter 'format' is required" }
	else { format = config.output_report_format }

	if (!config.target) { error "Parameter 'target' is required (filesystem path or image name)" }
	else { target = config.target }

	sh "trivy ${mode} --format ${format} --output ${mode}-results.${format} --severity MEDIUM,HIGH,CRITICAL ${target}"
}
