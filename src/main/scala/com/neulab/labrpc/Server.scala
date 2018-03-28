package com.neulab.labrpc

import akka.actor.{Actor, ActorRef}

import scala.collection.mutable.HashMap


object Server {
  def makeServer(): Server = {

    var services = HashMap[String, ActorRef]()
    var server = new Server(services, 0)
    return server
  }
}

class Server
(
  var services: HashMap[String, ActorRef],
  var count: Int
) {

  def getCount() : Int = {
    // non-block/lock version
    return this.count
  }

  def addService(serviceName: String, serviceActorRef: ActorRef): Unit = {
    this.services += (serviceName -> serviceActorRef)
  }






}
