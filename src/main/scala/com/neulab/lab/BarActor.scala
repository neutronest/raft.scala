package com.neulab.lab

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.event.Logging
import com.neulab.lab.FooBar


object BarActor {

  def props(fooActor: ActorRef): Props = {
    Props(new BarActor(fooActor))
  }


}

class BarActor(fooActor: ActorRef) extends Actor {

  //override val log = Logging(context.system, this)


  override def receive: Receive = {


    case FooBar.Foo => {
      fooActor ! FooBar.Bar
      println("BarActor receive a Foo! => Send a Bar")
    }
    case _ =>
  }

}
