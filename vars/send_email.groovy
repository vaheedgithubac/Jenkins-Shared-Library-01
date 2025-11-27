def call(Map config = [:]) {

    // --------------------------------------------
    // 1️⃣ Validate required parameters
    // --------------------------------------------
    def required = [
        "jobName",
        "buildNumber",
        "pipelineStatus",
        "buildURL",
        "branchName",
        "duration",
        "from_mail",
        "to_mail",
        "reply_to_mail"
    ]

    required.each { key ->
        if (!config[key]) {
            error "❌ Email: Missing required parameter '${key}'"
        }
    }

    // --------------------------------------------
    // 2️⃣ Extract required parameters
    // --------------------------------------------
    def jobName        = config.jobName
    def buildNumber    = config.buildNumber
    def pipelineStatus = config.pipelineStatus
    def buildURL       = config.buildURL
    def from_mail      = config.from_mail
    def to_mail        = config.to_mail
    def reply_to_mail  = config.reply_to_mail

    // --------------------------------------------
    // 3️⃣ Optional parameters with defaults
    // --------------------------------------------
    def cc_mail       = config.cc_mail ?: ""
    def bcc_mail      = config.bcc_mail ?: ""
    def attachments   = config.attachments ?: "trivy-reports/*"
    def branchName    = config.branchName ?: "N/A"
    def duration      = config.duration ?: "N/A"

    // --------------------------------------------
    // 4️⃣ Auto determine banner color based on status
    // --------------------------------------------
    def bannerColorMap = [
        "SUCCESS" : "#28a745",   // Green
        "UNSTABLE": "#ffc107",   // Yellow
        "FAILURE" : "#dc3545",   // Red
        "ABORTED" : "#6c757d",   // Gray
        "NOT_BUILT": "#6c757d"
    ]

    def bannerColor = config.bannerColor ?: bannerColorMap[pipelineStatus.toUpperCase()] ?: "#007bff" // default blue

    // --------------------------------------------
    // 5️⃣ Build HTML body with summary table
    // --------------------------------------------
    def body = """
    <html>
      <body style="font-family: Arial, sans-serif;">
        <div style="border: 4px solid ${bannerColor}; padding: 15px; border-radius: 6px;">

          <h2 style="margin-bottom: 10px;">${jobName} - Build #${buildNumber}</h2>

          <div style="background-color: ${bannerColor}; padding: 10px; border-radius: 4px; margin-bottom: 15px;">
            <h3 style="color: white; margin: 0;">Pipeline Status: ${pipelineStatus.toUpperCase()}</h3>
          </div>

          <table style="border-collapse: collapse; width: 100%; margin-bottom: 15px;">
            <tr>
              <th style="border: 1px solid #ddd; padding: 8px;">Parameter</th>
              <th style="border: 1px solid #ddd; padding: 8px;">Value</th>
            </tr>
            <tr>
              <td style="border: 1px solid #ddd; padding: 8px;">Job Name</td>
              <td style="border: 1px solid #ddd; padding: 8px;">${jobName}</td>
            </tr>
            <tr>
              <td style="border: 1px solid #ddd; padding: 8px;">Build Number</td>
              <td style="border: 1px solid #ddd; padding: 8px;">${buildNumber}</td>
            </tr>
            <tr>
              <td style="border: 1px solid #ddd; padding: 8px;">Branch</td>
              <td style="border: 1px solid #ddd; padding: 8px;">${branchName}</td>
            </tr>
            <tr>
              <td style="border: 1px solid #ddd; padding: 8px;">Duration</td>
              <td style="border: 1px solid #ddd; padding: 8px;">${duration}</td>
            </tr>
          </table>

          <p>
            Check full details in the
            <a href="${buildURL}" style="color: #1a73e8;">Jenkins Console Output</a>.
          </p>

        </div>
      </body>
    </html>
    """

    // --------------------------------------------
    // 6️⃣ Send email
    // --------------------------------------------
    emailext(
        subject: "${jobName} - Build #${buildNumber} - ${pipelineStatus.toUpperCase()}",
        body: body,
        mimeType: "text/html",
        to: to_mail,
        from: from_mail,
        replyTo: reply_to_mail,
        cc: cc_mail,
        bcc: bcc_mail,
        attachmentsPattern: attachments
    )
}
