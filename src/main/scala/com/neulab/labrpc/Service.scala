package com.neulab.labrpc

import akka.actor.Actor
import akka.util.ReentrantGuard

import scala.collection.mutable.HashMap



abstract class Service {
  var name: String
  var actor: Actor
}
