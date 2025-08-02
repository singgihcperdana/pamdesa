pipeline {
  agent any

  tools {
    maven 'Maven-3.9.11'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build & Test') {
      steps {
        sh 'mvn clean verify'
      }
    }

    stage('Deploy to Nexus') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'nexus-cred', usernameVariable: 'NEXUS_USER', passwordVariable: 'NEXUS_PASS')]) {
          sh """
            mvn deploy -DaltDeploymentRepository=nexus::default::http://nexus.singgihcp.com:8084/repository/maven-releases/ \
              -Dnexus.username=$NEXUS_USER -Dnexus.password=$NEXUS_PASS
          """
        }
      }
    }
  }

  post {
    success {
      echo '✅ Build dan deploy berhasil!'
    }
    failure {
      echo '❌ Build gagal!'
    }
  }
}
