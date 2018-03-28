package com.neulab.labrpc

import akka.actor.ActorRef
import akka.actor.{Actor, ActorSystem, Props}
import akka.util.Timeout
import scala.concurrent.duration._
import akka.pattern.ask
import scala.concurrent.Await
import scala.collection.mutable.HashMap
import scala.concurrent.Await


object Network {

  def makeNetwork() : Network = {

    var reliable = true
    var ends = HashMap[String, ClientEnd]()
    var enabled = HashMap[String, Boolean]()
    var servers = HashMap[String, Server]()
    var connections = HashMap[String, String]()
    var net = new Network(reliable=reliable,
      longDelays = false,
      longReordering = false,
      ends = ends,
      enabled = enabled,
      servers = servers,
      connections = connections)
    return net
  }
}

class Network
(
  var reliable : Boolean,
  var longDelays : Boolean,
  var longReordering : Boolean,
  var ends: HashMap[String, ClientEnd],
  var enabled: HashMap[String, Boolean],
  var servers: HashMap[String, Server],
  var connections: HashMap[String, String]
) {


  def makeEnd(endName: String): Unit = {
      this.ends += (endName -> new ClientEnd(endName))
  }

  def setReliable(reliable: Boolean) : Unit = {
    this.reliable = reliable
  }

  def setLongReordering(longReordering: Boolean) : Unit = {
    this.longReordering = longReordering
  }

  def setLongDelays(longDelays: Boolean) : Unit = {
    this.longDelays = longDelays
  }

  def addServer(serverName: String, serverObj: Server): Unit = {
    this.servers += (serverName -> serverObj)
  }

  def connect(endName: String, serverName: String): Unit = {
    this.connections += (endName -> serverName)
  }

  def enable(endName: String, ifEnable: Boolean): Unit = {
    this.enabled += (endName -> ifEnable)
  }


}

object NetworkActor {

  def props(net: Network) : Props = {
    Props(new NetworkActor(net))
  }
}

class NetworkActor(var net: Network) extends Actor {

  override def receive: Receive = {
    case (endName: String, methodName: String, args: RpcArgs, reply: RpcReply) => {
      var serverName = net.connections.getOrElse(endName, "")
      var server = net.servers.getOrElse(serverName, null)
      println("methodName: ", methodName)
      var actor = server.services.getOrElse("junk", null)

      implicit val timeout = Timeout(5 seconds)

      val future = actor ? ((methodName, args, reply))
      val result = Await.result(future, timeout.duration).asInstanceOf[String]
      sender ! result
      //actor ! ((methodName, args, reply))
      /*
      var targetServerNameOp = net.connections.get(endName)
      targetServerNameOp match {
        case Some(targetServer) => {
          println("----1----")
          var actorOp = targetServer.services.get(methodName)
          actorOp match {
            case Some(actor: ActorRef) => {
              println("----2---")
              actor ! ((args, reply))
            }
            case None => {
              println("fuck1")
            }
          }
        }
        case None => {
          println("fuck2")
        }
      }
      */
    }
    case _ =>
  }

}

