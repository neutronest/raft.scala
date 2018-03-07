package com.neulab.labrpc

import scala.collection.mutable.HashMap


object Server {
  def MakeServer(): Server = {

    var services = HashMap[String, BasicRpcService]()
    var server = new Server(services, 0)
    return server
  }
}

class Server
(
  var services: HashMap[String, BasicRpcService],
  var count: Int
) {

  def GetCount() : Int = {
    // non-block/lock version
    return this.count
  }








}
