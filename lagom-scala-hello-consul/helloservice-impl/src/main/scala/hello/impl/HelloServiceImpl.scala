package hello.impl

import java.net.InetAddress
import java.util.Date
import java.util.concurrent.CompletableFuture
import javax.inject.Inject

import akka.{Done, NotUsed}
import com.lightbend.lagom.javadsl.api.ServiceCall
import com.lightbend.lagom.javadsl.api.transport.{RequestHeader, ResponseHeader}
import hello.api.{HelloService, Message}
import org.slf4j.LoggerFactory

import scala.concurrent.{ExecutionContext, Future}
import scala.language.implicitConversions

class HelloServiceImpl @Inject()()(implicit ex: ExecutionContext) extends HelloService {

  private val log = LoggerFactory.getLogger(classOf[HelloServiceImpl])

  import converter.ServiceCallConverter._

  // Needed to convert some Scala types to Java
  override def hello(): ServiceCall[NotUsed, Message] = { notUsed => {
      log.info("hello reached")
      val localhost = InetAddress.getLocalHost
      Future.successful(Message(localhost.getHostName, localhost.getHostAddress, new Date))
    }
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

  override def helloOptions(): ServiceCall[NotUsed, Done] = { (requestHeader: RequestHeader, request: NotUsed) => {
      log.info("helloOptions reached")
      var resp = ResponseHeader.OK
      for ((k, v) <- headers) resp = resp.withHeader(k, v)
      CompletableFuture.completedFuture(Tuple2(resp, Done.getInstance()))
    }
  }
}
