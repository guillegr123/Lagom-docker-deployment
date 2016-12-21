/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package converter

import java.util.concurrent.CompletionStage

import com.lightbend.lagom.javadsl.api.ServiceCall
import com.lightbend.lagom.javadsl.api.transport.{RequestHeader, ResponseHeader}
import com.lightbend.lagom.javadsl.server.HeaderServiceCall

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.implicitConversions

object ServiceCallConverter extends CompletionStageConverters {
  implicit def liftToServiceCall[Request, Response](f: Request => CompletionStage[Response]): ServiceCall[Request, Response] =
    new ServiceCall[Request, Response] {
      def invoke(request: Request): CompletionStage[Response] = f(request)
    }
}

object HeaderServiceCallConverter extends CompletionStageConverters {
  implicit def liftToHeaderServiceCall[Request, Response](function: (RequestHeader, Request) => CompletionStage[(ResponseHeader, Response)]): ServiceCall[Request, Response] = {
    new HeaderServiceCall[Request, Response] {
      override def invokeWithHeaders(header: RequestHeader, request: Request): CompletionStage[akka.japi.Pair[ResponseHeader, Response]] =
        function(header, request).map(r => akka.japi.Pair(r._1, r._2))
    }
  }
}
