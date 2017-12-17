package main.logic

import main.ui.Display
import main.ui.Display.BoardInfo
import org.scalajs.dom

/**
  * Created by Dorian Thiessen on 2017-12-15.
  */
class Game {
  // Array of players (2 - 6)
  val players: Array[Player] = Array()
  // Map of players game pieces; Key is {player, piece}.color
  //val pieces: Map[String, Array[Piece]] = Map()
  var pc: Array[Piece] = Array[Piece]()

  // Game Move. Elements of Game.history
  class Move(ip: Position, fp: Position) {
    def initialPosition: Position = ip
    def finalPosition: Position = fp
  }
  // A record of each move in the game
  var history: Array[Game#Move] = Array()

  def registerMove(initialPos: Position, finalPos: Position): Unit = {
    // TODO: Replace dom.console logs with outr/scribe logs
    dom.console.log(
      s"Move: (${initialPos.X}, ${initialPos.Y})" +
      s" -> (${finalPos.X}, ${finalPos.Y})"
    )
    history = history :+ new Move(initialPos, finalPos)
  }

  def moveValid(piece: Piece, ix: Double, iy: Double, fx: Double, fy: Double): (Double, Double, Int, Int, Boolean) = {
    val deltaX = ix - fx
    val deltaY = iy - fy

    // Doesn't check move against other piece locations
    // TODO: Confirm pieces not blocking and that pieces exist in jump moves

    if (math.abs(deltaX) > BoardInfo.dx * 2 * 2.5 || math.abs(deltaY) > BoardInfo.dy * 2.5)
      return (0, 0, 0, 0, false) // Attempted to move distance > 2

    val (correctedX, correctedY, bx, by) = Display.focusCoords(fx, fy)
    if (math.abs(ix - correctedX) < 0.9 * Display.BoardInfo.dx)
      return (0, 0, 0, 0, false) // Attempted to move directly up or down

    val xyRatio = math.abs(iy - correctedY) / math.abs(ix - correctedX)
    if (xyRatio > 0 && xyRatio < 1.5)
      return (0, 0, 0, 0, false) // Attempted to move in 'L'

    val moveOk = piece.setPosition(bx, by)
    if(!moveOk)
      return (0, 0, 0, 0, false) // Attempted moving outside board

    (correctedX, correctedY, bx, by, true) // Looks good!
  }
}

object Game {
  private var current: Game = null
  def Current: Game = current
  def init(): Game = {
    if(current == null) current = new Game()
    current
  }
}