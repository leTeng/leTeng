pipeline{
    
    agent any
    
    // 参数化构建
    parameters {
        choice(name:'host',choices: ['119.23.70.145','47.115.83.45','47.115.117.107'], description:'47.115.117.107 -> test1    47.115.83.45 -> test2   47.115.117.107 -> test3')
        string(name: 'branch', defaultValue: '', description: '分支')
    }
    
    environment {
        DEPLOY_HOST = "${params.host}"
        DEPLOY_BRANCH = "${params.branch}"
    }
    
    tools {
        //工具名称必须在Jenkins 管理Jenkins → 全局工具配置中预配置。
        maven 'M3'
        jdk 'JDK8'
        git 'GIT'
    }
    stages {
        
        stage('git pull'){
            
            steps {
                
                echo '=================================================== git pull branch  ${DEPLOY_BRANCH} ==========================================================='
                
            }
        }
        
        stage('build'){
            steps {
                    
                echo 'build: ${DEPLOY_BRANCH}}'
            }
        }
        
        stage('deploy'){
             steps {
                    
                echo 'deploy: ${DEPLOY_HOST}}'
            }
        }
    }
}
