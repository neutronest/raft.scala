package com.neulab.test

import scala.collection.mutable.ArrayBuffer
import org.scalatest._
import akka.actor.{Actor, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import akka.actor.{ Props, Deploy, Address, AddressFromURIString }
import akka.remote.RemoteScope


import com.neulab.labrpc._



class JunkArgs(var args: Int) extends RpcArgs

class JunkReply(var reply: String) extends RpcReply

class JunkClass(var log1: ArrayBuffer[Int], var log2: ArrayBuffer[String])  {

    def handle1(junkArgs: JunkArgs, junkReply: JunkReply) = {
      log1 += junkArgs.args
      junkReply.reply = junkArgs.args.toString
      println("Handle1 has been called!")
    }

    def handle4(junkArgs: JunkArgs, junkReply: JunkReply) = {
      junkReply.reply = "ha? fa? van?"
      println("Called handle4 Ok")
    }
}

object JunkActor {

  def props(junkObj: JunkClass) : Props = {
    Props(new JunkActor(junkObj))
  }
}

class JunkActor(junkObj: JunkClass) extends Actor {

  override def receive: Receive = {
    case ("handle1", junkArgs: JunkArgs, junkReply: JunkReply) => {
      if (junkArgs.args > 0) {
        junkArgs.args -= 1
        println("args: ", junkArgs.args)
        junkObj.handle1(junkArgs, junkReply)
        //sender() ! (junkArgs, junkReply)
      } else {
        println("Finally")
      }
    }
    case ("handle4", junkArgs: JunkArgs, junkReply: JunkReply) => {
      junkObj.handle4(junkArgs, junkReply)
      sender ! junkReply.reply
    }

    case _ => {
      println("received but nothing happend!")
    }
  }



}

class TestLabrpc extends TestKit(ActorSystem("Labrpc")) with ImplicitSender with WordSpecLike with Matchers with BeforeAndAfterAll {
  override def afterAll() : Unit = {
    TestKit.shutdownActorSystem(system)
  }


  "Test Basic" must {
    var junkArgs: JunkArgs = new JunkArgs(100)
    var junkReply: JunkReply = new JunkReply("van?")

    var junkObj1 = new JunkClass(new ArrayBuffer[Int](), new ArrayBuffer[String]())
    var junkObj2 = new JunkClass(new ArrayBuffer[Int](), new ArrayBuffer[String]())

    val one = akka.actor.Address("akka.tcp", "sys", "host", 9001)
    val two = akka.actor.Address("akka.tcp", "sys", "host", 9002)
    val server1Actor = system.actorOf(JunkActor.props(junkObj1).
      withDeploy(Deploy(scope = RemoteScope(one))))
    val server2Actor = system.actorOf(JunkActor.props(junkObj2).
      withDeploy(Deploy(scope = RemoteScope(two))))
    //server1Actor ! ("handle1", junkArgs, junkReply)
    //server2Actor ! ("handle1", junkArgs, junkReply)


    var net = Network.makeNetwork()
    var networkActor = system.actorOf(NetworkActor.props(net))
    var end = new ClientEnd("end1-99")
    var server = Server.makeServer()
    server.addService("junk", server1Actor)
    net.addServer("server99", server)
    net.connect("end1-99", "server99")
    net.enable("end1-99", true)
    end.call(networkActor, "handle4", junkArgs, junkReply)
    println(junkReply.reply)















  }

  "An Echo actor" must {
    "send back message unchanged" in {
      val echo = system.actorOf(TestActors.echoActorProps)
      echo ! "hello world"
      expectMsg("hello world")
    }
  }
}
