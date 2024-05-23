def call(String channel, String message, String color) {
     def chn = '#build'
      print "notifyStack(${chn}, ${message}, ${color})"
     slackSend botUser: false,
         channel: chn,
         color: getColor(color),
         message: message,
         tokenCredentialId: 'slack-token'
     true
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
