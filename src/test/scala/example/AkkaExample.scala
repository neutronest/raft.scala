package example
import org.scalatest._
import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActors, TestKit}


class AkkaExample extends TestKit(ActorSystem("MySpec")) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {
  override def afterAll() : Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "An Echo actor" must {
    "send back message unchanged" in {
      val echo = system.actorOf(TestActors.echoActorProps)
      echo ! "hello world"
      expectMsg("hello world")
    }
  }
}
