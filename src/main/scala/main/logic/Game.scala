package main.logic

/**
  * Created by Dorian Thiessen on 2017-12-15.
  */
class Game {
  // Array of players (2 - 6)
  val players: Array[Player] = Array()
  // Map of players game pieces; Key is {player, piece}.color
  val pieces: Map[String, Array[Piece]] = Map()

  // TODO: Implement movement validation

  // A record of each move in the game
  val history: Array[State] = null // TODO: Implement history
  // Game State. Elements of Game.history
  class State {} // TODO: Implement State
}

object Game {
  private var current: Game = _
  def Current: Game = current
  def init(): Game = {
    require(current == null)
    current = new Game()
    current
  }
}