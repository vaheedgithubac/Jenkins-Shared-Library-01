def call(Map config = [:]) {

    def required = [
        "git_config_username",
        "git_config_email",
        "github_username",
        "github_token",
        "git_latest_commit_id",
        "repo_name",
        "image_name",
        "deployment_file",
        "branch"
    ]

    required.each { key ->
        if (!config[key]) {
            error "❌ GitHub: Missing required parameter '${key}'"
        }
    }

    def git_config_username  = config.git_config_username
    def git_config_email     = config.git_config_email
    def github_username      = config.github_username
    def github_token         = config.github_token
    def git_latest_commit_id = config.git_latest_commit_id
    def repo_name            = config.repo_name
    def image_name           = config.image_name
    def deployment_file      = config.deployment_file
    def branch               = config.branch ?: 'main'

    // === CONFIGURE GIT ===
    steps.sh "git config user.name '${git_config_username}'"
    steps.sh "git config user.email '${git_config_email}'"

    // === ENSURE CLEAN WORKSPACE ===
    steps.sh "git checkout ${branch}"
    steps.sh "git pull origin ${branch}"

    // === PRINTING COMMIT HASH ===
    steps.echo "Using Commit Hash: ${git_latest_commit_id}"

    // === UPDATE DEPLOYMENT FILE ===
    steps.echo "Updating ${deployment_file} to use image tag: ${git_latest_commit_id}"

    steps.sh """
        sed -i.bak 's#image: ${image_name}:.*#image: ${image_name}:${git_latest_commit_id}#' ${deployment_file}
    """

    // === COMMIT AND PUSH ===
    steps.sh """
        git add ${deployment_file}
        git commit -m 'chore: update image tag to ${git_latest_commit_id}' || echo 'No changes to commit'
        git push https://${github_username}:${github_token}@github.com/${github_username}/${repo_name}.git ${branch}
    """

    steps.echo "✅ Deployment file updated and pushed to ${branch}"
}



/* Please check thoroughly the parametres before using
def params = [
  "git_config_username":  env.MY_GIT_CONFIG_USERNAME,
  "git_config_email":     env.MY_GIT_CONFIG_EMAIL,
  "github_username":      env.MY_GITHUB_USERNAME,
  "github_token":         env.MY_GITHUB_TOKEN,
  "git_latest_commit_id": env. MY_GIT_LATEST_COMMIT_ID,
  "repo_name":            env.MY_GIT_REPO_NAME,
  "image_name":           env.MY_IMAGE_NAME,
  "deployment_file":      env.DEPLOYMENT_FILE,
  "branch":               env.MY_BRANCH
]
*/
*/
