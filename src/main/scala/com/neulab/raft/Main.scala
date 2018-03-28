package com.neulab.raft

import akka.actor.{ActorSystem, Props}
import com.neulab.lab.FooActor
import com.neulab.lab.BarActor
import com.neulab.lab.FooBar.{Bar, Foo}
import com.typesafe.config.ConfigFactory
import com.neulab.lab.RpcServerActor;

object Main extends Main with App {
  println("Hello Actor")
  val system = ActorSystem("mySystem",
    ConfigFactory.parseString("""
        akka {
          actor {
            provider = remote
          }
          remote {
            enabled-transports = ["akka.remote.netty.tcp"]
            netty.tcp {
              hostname = "127.0.0.1"
              port = 2555
            }
          }
        }
      """))
  val fooActor = system.actorOf(FooActor.props(), "fooActor")
  val barActor = system.actorOf(BarActor.props(fooActor), "barActor")
  val serverActor = system.actorOf(RpcServerActor.props(), "server")


  //barActor ! Foo

}

trait Main {

}

