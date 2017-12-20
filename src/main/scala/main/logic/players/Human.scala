package main.logic.players

class Human(name: String, color: String) extends Player(name, color) {
  override def notifyOfTurn(): Unit = {
    // TODO: Notify player of turn
  }
}
