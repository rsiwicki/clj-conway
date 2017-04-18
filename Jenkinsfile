pipeline {
  agent any
  stages {
    stage('sleep') {
      steps {
        sleep 2
      }
    }
    stage('unix') {
      steps {
        isUnix()
      }
    }
    stage('print') {
      steps {
        sh 'echo "hello Conway"'
      }
    }
    stage('leiningen') {
      steps {
        sh '/Users/robsiwicki/Engineering/lein/lein compile'
      }
    }
  }
}