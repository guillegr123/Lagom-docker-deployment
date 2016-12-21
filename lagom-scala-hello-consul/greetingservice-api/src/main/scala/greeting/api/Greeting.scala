package greeting.api

import java.util.Date

case class Greeting(hostname: String, ip: String, remoteHostname: String, remoteIp: String, remoteTime: Date)

