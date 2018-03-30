package com.neulab.labrpc

import akka.actor.ActorRef
import akka.util.Timeout
import scala.concurrent.duration._
import akka.pattern.ask
import scala.concurrent.Await


abstract class RpcArgs()

abstract class RpcReply()

class ClientEnd(var endName : String) {

  def call(actorRef: ActorRef, methodName: String,  args: RpcArgs, reply: RpcReply): String = {

    implicit val timeout = Timeout(5 seconds)
    val future = actorRef ? ((endName, methodName, args, reply))
    val result = Await.result(future, timeout.duration).asInstanceOf[String]
    println("result: ", result)
    return result

  }
}
