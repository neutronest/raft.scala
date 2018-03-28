package com.neulab.labrpc

import scala.collection.mutable.HashMap


object Network {

  def makeNetwork() : Network = {
    return null
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

  def setReliable(reliable: Boolean) : Unit = {
    this.reliable = reliable
  }

  def setLongReordering(longReordering: Boolean) : Unit = {
    this.longReordering = longReordering
  }

  def setLongDelays(longDelays: Boolean) : Unit = {
    this.longDelays = longDelays
  }

  


}
