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
    stage('bx') {
      steps {
        sh '/Applications/VirtualBox.app/Contents/MacOS/VBoxManage startvm {6d900a60-26e8-4098-8229-3587a85fe8bd} --type headless'
      }
    }
  }
}