package greeting.impl

import greeting.api.GreetingService
import hello.api.HelloService
import com.google.inject.AbstractModule
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport


class GreetingServiceModule extends AbstractModule with ServiceGuiceSupport {
  override def configure(): Unit = {
    bindServices(serviceBinding(classOf[GreetingService], classOf[GreetingServiceImpl]))
    bindClient(classOf[HelloService])
  }
}
