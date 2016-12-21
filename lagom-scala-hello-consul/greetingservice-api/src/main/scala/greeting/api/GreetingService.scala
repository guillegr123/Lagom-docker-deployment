package greeting.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.javadsl.api.ScalaService._
import com.lightbend.lagom.javadsl.api.Service.named
import com.lightbend.lagom.javadsl.api.transport.Method
import com.lightbend.lagom.javadsl.api.{Descriptor, Service, ServiceCall}


trait GreetingService extends Service {
  // TODO Test for Consul (de)registration code at service Start/Stop
  // TODO Add Swagger API generation @see https://github.com/swagger-api/swagger-play/tree/master/play-2.5/swagger-play2

  def greeting(): ServiceCall[NotUsed, Greeting]
  def greetingOptions(): ServiceCall[NotUsed, Done]

  def descriptor: Descriptor = {
    named("greetingservice").withCalls(
      restCall(Method.GET, "/v1/greeting", greeting _),
      restCall(Method.OPTIONS, "/v1/greeting", greetingOptions _)
    ).withAutoAcl(true)
  }
}

