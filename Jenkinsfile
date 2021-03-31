node{

    properties(
        [
            // 参数化构建
            parameters(

                [
                    choice(name:'host',choices: ['119.23.70.145','47.115.83.45','47.115.117.107'], description:'47.115.117.107 -> test1 \n\r 47.115.83.45 -> test2  \n\r 47.115.117.107 -> test3'),
                    choice(name:'app',choices: ['H5','ADMIN','APP','ES','PAY'], description:'需要发布的服务')
                ]
            )
        ]
    )

    def sourceFiles = null
    def h5ServerName = "seed-sunflower-h5-gateway-1.0.0-SNAPSHOT.jar"
    def adminServerName = "scaffolding-admin-0.0.1-SNAPSHOT.jar"
    def appServerName = "seed-sunflower-app-gateway-1.0.0-SNAPSHOT.jar"
    def esServerName = "seed-es-server-1.0.0-SNAPSHOT.jar"
    def payServerName = "seed-payment-server-1.0.0-SNAPSHOT.jar"
    def execCommand = "echo deploy"

    def deployHost = "${params.host}"
    def app = "${params.app}"

    def mavenHome = tool 'M3'

    stage('build'){

        // 打包H5网关
        if("${app}" == "H5"){
            sh '${mavenHome}/bin/mvn clean install package -P$PROFILE -U -T 1C -Dmaven.compile.fork=true   -Dmaven.test.skip=true -pl seed-sunflower/seed-sunflower-gateway -am'
            sourceFiles = "seed-sunflower/seed-sunflower-gateway/target/${h5ServerName}"
        }

        // 打包ADMIN网关
        if("${app}" == "ADMIN"){
            sh '${mavenHome}/bin/mvn clean install package -P$PROFILE -U -T 1C -Dmaven.compile.fork=true   -Dmaven.test.skip=true -pl scaffolding-admin -am'
            sourceFiles = "scaffolding-admin/target/${adminServerName}"
        }

        // 打包APP网关
        if("${app}" == "APP"){
            sh '${mavenHome}/bin/mvn clean install package -P$PROFILE -U -T 1C -Dmaven.compile.fork=true   -Dmaven.test.skip=true -pl seed-sunflower/seed-sunflower-app-gateway -am'
            sourceFiles = "seed-sunflower/seed-sunflower-app-gateway/target/${appServerName}"
        }

        // 打包ES网关
        if("${app}" == "ES"){
            sh '${mavenHome}/bin/mvn clean install package -P$PROFILE -U -T 1C -Dmaven.compile.fork=true   -Dmaven.test.skip=true -pl seed-es-service/seed-es-server -am'
            sourceFiles = "seed-es-service/seed-es-server/target/${esServerName}"
        }

        // 打包PAY网关
        if("${app}" == "PAY"){
            sh '${mavenHome}/bin/mvn clean install package -P$PROFILE -U -T 1C -Dmaven.compile.fork=true   -Dmaven.test.skip=true -pl seed-payment/seed-payment-server -am'
            sourceFiles = "seed-es-service/seed-es-server/target/${payServerName}"
        }
    }

    stage('deploy'){
        def removePrefix = sourceFiles.substring(0, sourceFiles.lastIndexOf("/"))
        sshPublisher(
            continueOnError: false,
            failOnError: true,
            publishers: [
                sshPublisherDesc(
                   configName: "${deployHost}",
                   verbose: true,
                   transfers: [
                      sshTransfer(
                         // 打包后的jar文件
                         sourceFiles: "${sourceFiles}",
                         // 忽略前缀文件
                         removePrefix: "${removePrefix}",
                         // 远程目录(一般在jenkins全局配置一个基础路径)
                         remoteDirectory: '',
                         // 在目标机器执行的命令
                         execCommand: "${execCommand}"
                      )
                   ]
                )
            ]
        )
    }
}
