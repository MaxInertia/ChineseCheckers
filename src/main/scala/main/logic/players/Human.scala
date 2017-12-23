package main.logic.players

import org.scalajs.dom

class Human(name: String, color: String) extends Player(name, color) {
  override def notifyOfTurn(): Unit = {
    // TODO: Notify player of turn
    dom.console.log(s"It is now your turn $Name!")
  }
}
