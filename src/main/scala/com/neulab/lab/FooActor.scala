package com.neulab.lab;

import akka.actor.{Actor, ActorLogging, Props}
import akka.event.Logging

import com.neulab.lab.FooBar

object FooActor {

  def props() : Props = {
    Props(new FooActor())
  }
}

class FooActor extends Actor {

  override def receive: Receive = {
    case FooBar.Bar => {
      sender() ! FooBar.Foo
      println("FooActor receive a Bar! => Send a Foo")
    }
    case _ =>
  }

}
