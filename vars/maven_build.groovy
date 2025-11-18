def call(Map config = [:]){

  // Validate required parameters
  // def skipTests = config.skipTests ?: 'false'
  def skipTests = (config.skipTests in [true, 'true']) ? 'true' : 'false'

  sh "mvn clean package -DskipTests=${skipTests}"
}
