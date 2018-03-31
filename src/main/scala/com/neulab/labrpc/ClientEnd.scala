package com.neulab.labrpc

import akka.actor.ActorRef
import akka.util.Timeout
import scala.concurrent.duration._
import akka.pattern.ask
import scala.concurrent.Await


abstract class RpcArgs()

abstract class RpcReply() {

  type R
  def setValues(values: R) : Unit
  def getValues() : R
}

class ClientEnd(var endName : String) {

  def call(actorRef: ActorRef, methodName: String,  args: RpcArgs, reply: RpcReply): Unit = {

    implicit val timeout = Timeout(5 seconds)
    val future = actorRef ? ((endName, methodName, args, reply))
    val result = Await.result(future, timeout.duration).asInstanceOf[reply.R]
    reply.setValues(result)
  }
}
