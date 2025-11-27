job('java-app-con-maven-dsl-v2') {
    description('Java Maven App con DSL para el curso de Jenkins')
    scm {
        git('https://github.com/magadiflo/simple-java-maven-app.git', 'master') { node ->
            node / gitConfigName('Martín Díaz')
            node / gitConfigEmail('magadiflo@gmail.com')
        }
    }
    steps {
        maven {
            mavenInstallation('Maven en Jenkins')
            goals('-B -DskipTests clean package')
        }
        maven {
            mavenInstallation('Maven en Jenkins')
            goals('test')
        }
        shell('''
          echo "Entrega: Desplegando la aplicación" 
          java -jar /var/jenkins_home/workspace/java-app-con-maven-dsl/target/my-app-1.0-SNAPSHOT.jar
        ''')
    }
    publishers {
        archiveArtifacts('target/*.jar')
        archiveJunit('target/surefire-reports/*.xml')
        slackNotifier {
            notifyAborted(true)
            notifyEveryFailure(true)
            notifyNotBuilt(false)
            notifyUnstable(false)
            notifyBackToNormal(true)
            notifySuccess(true)
            notifyRepeatedFailure(false)
            startNotification(false)
            includeTestSummary(false)
            includeCustomMessage(false)
            customMessage(null)
            sendAs(null)
            commitInfoChoice('NONE')
            teamDomain(null)
            authToken(null)
        }
    }
}
