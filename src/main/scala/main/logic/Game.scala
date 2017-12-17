package main.logic

import org.scalajs.dom

/**
  * Created by Dorian Thiessen on 2017-12-15.
  */
class Game {
  // Array of players (2 - 6)
  val players: Array[Player] = Array()
  // The game board. Contains game pieces.
  var board: Board = new Board()
  // A record of each move in the game
  var history: Array[Game#Move] = Array()

  def registerMove(initialT: Tile, finalT: Tile): Unit = {
    // TODO: Replace dom.console logs with outr/scribe logs
    dom.console.log(
      s"Move: (${initialT.X}, ${initialT.Y})" +
      s" -> (${finalT.X}, ${finalT.Y})")
    history = history :+ new Move(initialT, finalT)
  }

  // Game Move. Elements of Game.history
  class Move(it: Tile, ft: Tile) {
    def initialTile: Tile = it
    def finalTile: Tile = ft
  }
}

object Game {
  // Previous games, saved as a sequence of moves.
  private var previous: Array[Array[Game#Move]] = Array() //TODO: Save to file

  private var current: Game = _
  def Current: Game = current

  def init(): Game = {
    if(current == null) current = new Game()
    current
  }

  def save(): Unit = previous = previous :+ current.history.clone()

  def reset(): Unit = {
    val newGame = new Game()
    newGame.board = current.board
    current = newGame
  }

  //TODO: Implement replay()
}