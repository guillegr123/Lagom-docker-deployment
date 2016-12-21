/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package utils

import java.util.concurrent.CompletableFuture._
import java.util.concurrent.CompletionStage

import com.lightbend.lagom.javadsl.api.ServiceCall
import scala.language.implicitConversions
import com.lightbend.lagom.javadsl.api.transport.{RequestHeader, ResponseHeader}
import com.lightbend.lagom.javadsl.server.HeaderServiceCall
import scala.concurrent.Future
import scala.compat.java8.FutureConverters._
import scala.concurrent.ExecutionContext.Implicits.global

object Implicits {

  import scala.language.implicitConversions

  implicit def requestToHeaderServiceCallWithCompletedFuture[Req, Resp](reqFunc: (RequestHeader, Req) => Future[(ResponseHeader, Resp)]): ServiceCall[Req, Resp] = {
    new HeaderServiceCall[Req, Resp] {
      override def invokeWithHeaders(header: RequestHeader, request: Req): CompletionStage[akka.japi.Pair[ResponseHeader, Resp]] =
        reqFunc(header, request).map(r => akka.japi.Pair(r._1, r._2)).toJava
    }
  }

  implicit def requestToServiceCallWithCompletedFuture[Req, Resp](reqFunc: Req => Resp): ServiceCall[Req, Resp] = {
    new ServiceCall[Req, Resp] {
      override def invoke(request: Req): CompletionStage[Resp] = {
        completedFuture(reqFunc(request))
      }
    }
  }
/*
  implicit def requestToHeaderServiceCall[Req, Resp](reqFunc: (RequestHeader, Req) => CompletionStage[(ResponseHeader, Resp)]): ServiceCall[Req, Resp] = {
    new HeaderServiceCall[Req, Resp] {
      override def invokeWithHeaders(header: RequestHeader, request: Req): CompletionStage[akka.japi.Pair[ResponseHeader, Resp]] =
        reqFunc(header, request)
    }
  }
*/
  implicit def requestToServiceCall[Req, Resp](reqFunc: Req => CompletionStage[Resp]): ServiceCall[Req, Resp] = {
    new ServiceCall[Req, Resp] {
      override def invoke(request: Req): CompletionStage[Resp] = {
        reqFunc(request)
      }
    }
  }

  implicit def sFun1ToAkkaJapiFun[T, R](sFun1: T => R): akka.japi.function.Function[T, R] =
    new akka.japi.function.Function[T, R] {
      @scala.throws[Exception](classOf[Exception])
      override def apply(param: T): R = sFun1.apply(param)
    }

  implicit def sFunToAkkaEffect(sFun: () => Unit): akka.japi.Effect =
    new akka.japi.Effect {
      @scala.throws[Exception](classOf[Exception])
      override def apply(): Unit = sFun()
    }

  implicit def asJavaBiFunction[T, U, R](sFun: (T, U) => R): java.util.function.BiFunction[T, U, R] =
    new java.util.function.BiFunction[T, U, R] {
      override def apply(t: T, u: U): R = sFun(t, u)
    }

  implicit def asJavaConsumer[T](sFun: T => Unit): java.util.function.Consumer[T] =
    new java.util.function.Consumer[T] {
      override def accept(t: T): Unit = sFun(t)
    }

  implicit def asJavaFunction[T, R](sFun: T => R): java.util.function.Function[T, R] =
    new java.util.function.Function[T, R] {
      override def apply(t: T): R = sFun(t)
    }

  implicit def asJavaBiConsumer[T, U](sFun: (T, U) => Unit): java.util.function.BiConsumer[T, U] =
    new java.util.function.BiConsumer[T, U] {
      override def accept(t: T, u: U): Unit = sFun(t, u)
    }
}
