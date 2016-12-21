package hello.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.javadsl.api.ScalaService._
import com.lightbend.lagom.javadsl.api.Service.named
import com.lightbend.lagom.javadsl.api.{Descriptor, Service, ServiceCall}
import com.lightbend.lagom.javadsl.api.transport.Method


trait HelloService extends Service {
  // TODO Test for Consul (de)registration code at service Start/Stop
  // TODO Add Swagger API generation @see https://github.com/swagger-api/swagger-play/tree/master/play-2.5/swagger-play2

  def hello(): ServiceCall[NotUsed, Message]
  def helloOptions(): ServiceCall[NotUsed, Done]

  def descriptor: Descriptor = {
    named("helloservice").withCalls(
      restCall(Method.GET, "/v1/hello", hello _),
      restCall(Method.OPTIONS, "/v1/hello", helloOptions _)
    ).withAutoAcl(true)
  }
}

