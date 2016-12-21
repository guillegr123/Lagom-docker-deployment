package hello.impl

import hello.api.HelloService
import com.google.inject.AbstractModule
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport


class HelloServiceModule extends AbstractModule with ServiceGuiceSupport {
  override def configure(): Unit = bindServices(serviceBinding(classOf[HelloService], classOf[HelloServiceImpl]))
}
