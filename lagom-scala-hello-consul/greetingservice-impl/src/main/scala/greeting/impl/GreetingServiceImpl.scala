package greeting.impl

import java.net.InetAddress
import java.util.concurrent.CompletableFuture

import greeting.api.{Greeting, GreetingService}
import hello.api.HelloService
import javax.inject.Inject

import com.lightbend.lagom.javadsl.api.ServiceCall
import akka.{Done, NotUsed}
import com.lightbend.lagom.javadsl.api.transport.{RequestHeader, ResponseHeader}
import hello.api.Message
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext
import utils.ServerError

class GreetingServiceImpl @Inject()(helloservice: HelloService)(implicit ex: ExecutionContext) extends GreetingService {

  private val log = LoggerFactory.getLogger(classOf[GreetingServiceImpl])

  // Needed to convert some Scala types to Java
  override def greeting(): ServiceCall[NotUsed, Greeting] = (_: NotUsed) => {
    log.info("greeting reached")
    helloservice.hello().invoke().thenCompose(
      (msg: Message) => msg match {
        case message: Message => CompletableFuture.completedFuture[Greeting](
          new Greeting("InetAddress.getLocalHost.getHostName", "InetAddress.getLocalHost.getHostAddress",
            message.hostname, message.ip, message.time))
        case _ => {
          val v = new CompletableFuture[Greeting]()
          v.completeExceptionally(ServerError("Could not get message from internal service"))
          v
        }
      }
    )
  }

  import converter.HeaderServiceCallConverter._

  // CORS pre-flight support
  def headers = Map(
    "Access-Control-Allow-Origin" -> "*",
    "Access-Control-Allow-Methods" -> "GET, POST, OPTIONS, DELETE, PUT",
    "Access-Control-Max-Age" -> "3600",
    "Access-Control-Allow-Headers" -> "Origin, Content-Type, Accept, Authorization",
    "Access-Control-Allow-Credentials" -> "true"
  )

  override def greetingOptions(): ServiceCall[NotUsed, Done] = { (requestHeader: RequestHeader, request: NotUsed) => {
      log.info("greetingOptions reached")
      var resp = ResponseHeader.OK
      for ((k, v) <- headers) resp = resp.withHeader(k, v)
      CompletableFuture.completedFuture(Tuple2(resp, Done.getInstance()))
    }
  }
}
