package com.neulab.test

import scala.collection.mutable.ArrayBuffer
import org.scalatest._
import akka.actor.{Actor, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import akka.actor.{ Props, Deploy, Address, AddressFromURIString }
import akka.remote.RemoteScope


import com.neulab.labrpc._



class JunkIntArgs(var args: Int) extends RpcArgs

class JunkReply(var reply: String) extends RpcReply

class JunkClass(var log1: ArrayBuffer[Int], var log2: ArrayBuffer[String])  {

  def handle1(junkArgs: JunkIntArgs, junkReply: JunkReply) = {
    log1 += junkArgs.args
    junkReply.reply = junkArgs.args.toString
    println("Handle1 has been called!")
  }

  def handle2(junkArgs: JunkIntArgs, junkReply: JunkReply) = {
    log1 += junkArgs.args
    junkReply.reply = log1.sum.toString
  }

  def handle4(junkArgs: JunkIntArgs, junkReply: JunkReply) = {
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
    case ("handle1", junkArgs: JunkIntArgs, junkReply: JunkReply) => {
      if (junkArgs.args > 0) {
        junkArgs.args -= 1
        println("args: ", junkArgs.args)
        junkObj.handle1(junkArgs, junkReply)

      } else {
        println("Finally")
      }
    }

    case ("handle2", junkArgs: JunkIntArgs, junkReply: JunkReply) => {
      junkObj.handle2(junkArgs, junkReply)
      sender ! junkReply.reply
    }
    case ("handle4", junkArgs: JunkIntArgs, junkReply: JunkReply) => {
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

/*
  def setup = new {

    // rpc object init
    var junkObj1 = new JunkClass(new ArrayBuffer[Int](), new ArrayBuffer[String]())
    // akka remoting init
    val address1 = akka.actor.Address("akka.tcp", "sys", "host", 9001)
    val serverActor1 = system.actorOf(JunkActor.props(junkObj1).
      withDeploy(Deploy(scope = RemoteScope(address1))))

    var net = Network.makeNetwork()
    var networkActor = system.actorOf(NetworkActor.props(net))
  }
*/

  "Test Basic" in {

    // rpc object init
    var junkObj1 = new JunkClass(new ArrayBuffer[Int](), new ArrayBuffer[String]())
    // akka remoting init
    val address1 = akka.actor.Address("akka.tcp", "sys", "host", 9001)
    val serverActor1 = system.actorOf(JunkActor.props(junkObj1).
      withDeploy(Deploy(scope = RemoteScope(address1))))

    var net = Network.makeNetwork()
    var networkActor = system.actorOf(NetworkActor.props(net))
    // args & replys prepare
    var junkArgs: JunkIntArgs = new JunkIntArgs(100)
    var junkReply: JunkReply = new JunkReply("ha? fa? van?")
    var end = new ClientEnd("end1-99")
    var server = Server.makeServer()
    server.addService("junk", serverActor1)
    net.addServer("server99", server)
    net.connect("end1-99", "server99")
    net.enable("end1-99", true)
    end.call(networkActor, "handle4", junkArgs, junkReply)
    junkReply.reply should be("ha? fa? van?")
  }


/*
  "Test Counts" in {

    var junkArgs: JunkIntArgs = new JunkIntArgs(1)
    var junkReply: JunkReply = new JunkReply("van?")
    var junkObj1 = new JunkClass(new ArrayBuffer[Int](), new ArrayBuffer[String]())
    val address1 = akka.actor.Address("akka.tcp", "sys", "host", 9001)
    val serverActor1 = system.actorOf(JunkActor.props(junkObj1).
      withDeploy(Deploy(scope = RemoteScope(address1))))
    var net = Network.makeNetwork()
    var networkActor = system.actorOf(NetworkActor.props(net))
    var end = new ClientEnd("end1-99")

    var server = Server.makeServer()
    server.addService("junk", serverActor1)

    // add server with specific server-name
    // make connection between end1-99 and server
    net.addServer("server99", server)
    net.connect("end1-99", "server99")
    net.enable("end1-99", true)

    for (i <- 1 to 20) {
      end.call(networkActor, "handle2", junkArgs, junkReply)
      println("reply result: ", junkReply.reply)
    }
    junkReply.reply should be("20")
    server.getCount() should be(20)
  }
*/
  "An Echo actor" must {
    "send back message unchanged" in {
      val echo = system.actorOf(TestActors.echoActorProps)
      echo ! "hello world"
      expectMsg("hello world")
    }
  }
}
