package com.neulab.lab

import akka.actor.{Actor, Props}


case class AkkaMessage
(
  var message: Any
)

case class Response
(
  var response: Any
)


class RpcServerActor extends Actor {

  override def receive: Receive = {

    case msg: AkkaMessage => {
      println("Server message: " + msg.message)
      sender ! Response("response " + msg.message)
    }
    case _ => {
      println("Not supported yet!")
    }
  }
}

object RpcServerActor  {
  def props() : Props = {
    Props(new RpcServerActor())
  }


}
