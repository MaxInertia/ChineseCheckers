package main.logic.players

import main.logic.Colors.Color
import org.scalajs.dom

class Human(name: String, color: Color) extends Player(name, color) {
  override def notifyOfTurn(): Unit = {
    // TODO: Notify player of turn
    dom.console.log(s"It is now your turn $Name!")
  }
}
