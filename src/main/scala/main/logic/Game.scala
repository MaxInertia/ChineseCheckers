package main.logic

import org.scalajs.dom

/**
  * Created by Dorian Thiessen on 2017-12-15.
  */
class Game {
  // Array of players (2 - 6)
  val players: Array[Player] = Array()
  // Map of players game pieces; Key is {player, piece}.color
  val pieces: Map[String, Array[Piece]] = Map()

  // TODO: Implement movement validation

  // Game Move. Elements of Game.history
  class Move(ip: Position, fp: Position) {
    def initialPosition: Position = ip
    def finalPosition: Position = fp
  }
  // A record of each move in the game
  var history: Array[Game#Move] = Array()
  def registerMove(initialPos: Position, finalPos: Position): Unit = {
    // TODO: Replace dom.console logs with outr/scribe
    dom.console.log(
      s"Move: (${initialPos.X}, ${initialPos.Y}) -> " +
        s"(${finalPos.X}, ${finalPos.Y})")
    history = history :+ new Move(initialPos, finalPos)
  }
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