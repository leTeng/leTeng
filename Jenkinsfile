pipeline {
  agent any
  stages {
    stage('git pull') {
      steps {
        echo '=================================================== git pull branch  ${DEPLOY_BRANCH} ==========================================================='
      }
    }

    stage('build') {
      steps {
        echo 'build: ${DEPLOY_BRANCH}}'
      }
    }

    stage('deploy') {
      steps {
        echo 'deploy: ${DEPLOY_HOST}}'
      }
    }

  }
  tools {
    maven 'M3'
    jdk 'JDK8'
    git 'GIT'
  }
  environment {
    DEPLOY_HOST = "${params.testHost}"
    DEPLOY_BRANCH = "${params.branch}"
  }
  parameters {
    choice(name: 'host', choices: '119.23.70.145 | 47.115.83.45 | 47.115.117.107', description: '47.115.117.107 -> test1 | 47.115.83.45 -> test2 | 47.115.117.107 -> test3')
    string(name: 'branch', defaultValue: '', description: '分支')
  }
}