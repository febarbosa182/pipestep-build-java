
package br.com.dynamic.build

class Build{
    def call (jenkins) {
        jenkins.podTemplate(
            containers: [
                jenkins.containerTemplate(name: 'java', image: jenkins.env.CI_IMAGE, ttyEnabled: true, command: 'cat')
            ],
            yamlMergeStrategy: jenkins.merge(),
            workspaceVolume: jenkins.persistentVolumeClaimWorkspaceVolume(
                claimName: "pvc-${jenkins.env.JENKINS_AGENT_NAME}",
                readOnly: false
            )
        )

        {
            jenkins.node(jenkins.POD_LABEL){
                jenkins.container('java'){
                    jenkins.sh label: "Build application", script: "gradle clean build"
                }
            }
        }
    }
}