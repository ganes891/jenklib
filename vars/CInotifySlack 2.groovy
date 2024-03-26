def call(String channel, String message, String color, String slack_branch) {
    print "notifyStack(${channel}, ${message}, ${color}, ${slack_branch})"

    if ( slack_branch == 'develop' ) {
        slackSend botUser: false,
            channel: channel,
            color: getColor(color),
            message: message,
            tokenCredentialId: 'dev-CI-Notify'
        true

        }
    else if ( slack_branch == 'qa' ) {
        slackSend botUser: false,
            channel: channel,
            color: getColor(color),
            message: message,
            tokenCredentialId: 'qa-CI-Notify'
        true
        }
    else if ( slack_branch == 'sandbox' ) {
        slackSend botUser: false,
            channel: channel,
            color: getColor(color),
            message: message,
            tokenCredentialId: 'sdx-CI-Notify'
        true
        }
    else if ( slack_branch == 'primary' ) {
        slackSend botUser: false,
            channel: channel,
            color: getColor(color),
            message: message,
            tokenCredentialId: 'prod-CI-Notify'
        true
        }
    else  {
        slackSend botUser: false,
            channel: channel,
            color: getColor(color),
            message: message,
            tokenCredentialId: 'CI-Notify'
        true
        }

}


def getColor(String colorName) {
    def color = '#000000'
    switch(colorName) {
        case 'red':
        case 'error':
        case 'danger':
            color = '#FF0000'
            break
        case 'green':
        case 'good':
        case 'ok':
            color = '#00FF00'
            break
        case 'amber':
        case 'yellow':
        case 'warning':
            color = '#FFFF00'
            break
    }
    color
}
