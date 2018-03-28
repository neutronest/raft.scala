package com.neulab.test

import org.scalatest._
import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActors, TestKit}

class JunkArgs(var args: Int)

class JunkReply(var reply: String)

class JunkServer(var log1: List[JunkArgs], var log2: List[JunkReply]) {

    def handle1(args: JunkArgs, reply: JunkReply) = {
        
    } 
}




class TestLabrpc extends TestKit(ActorSystem("Labrpc")) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {
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
