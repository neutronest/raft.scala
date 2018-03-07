package com.neulab.labrpc

import akka.util.ReentrantGuard

import scala.collection.mutable.HashMap


abstract class ReqMsg {

  var clientName: String
  var methodName: String
  type A
  type R
  var args: A
}

class ReplyMsg(var ok: Boolean, var reply: Any)

abstract class BasicRpcMethod {
  var methodName: String
  type Reply
  def MakeReply() : Reply
  def RpcMethod(args: Any, reply: Reply)

}

abstract class BasicRpcService {
  type S
  var serviceObj: S
  var name: String
  var methods: HashMap[String, BasicRpcMethod]

  def dispatch(rpcName: String, reqMsg: ReqMsg): Option[ReplyMsg] = {

    var rpcMethodOp = methods.get(rpcName)
    rpcMethodOp match  {
      case Some(rpcMethod) => {


        var args = reqMsg.args
        var reply = rpcMethod.MakeReply()
        rpcMethod.RpcMethod(args, reply)

        var replyMsg = new ReplyMsg(true, reply)
        return Some(replyMsg)
      }
      case _ => None
    }
  }
}
