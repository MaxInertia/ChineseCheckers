package main

import org.scalajs.dom.console

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import main.logic.{Game, Piece}
import main.ui.{Display, Position}
import main.ui.Display.{Sprites, makePlayerPieces}
import org.scalajs.dom

/**
  * Created by Dorian Thiessen on 2017-12-15.
  */
@JSExportTopLevel("ChCheckers")
object Main {

  @JSExport
  def makeGame(): Unit = {
    console.log("ChCheckers.makeGame called")
    val game = Game.init()
    Display.init(game)
  }

  @JSExport
  def activatePlayer(dir: Int): Unit = {
    var color: String = null
    dir match {
      case 0 =>
        color = "blue"
      case 1 =>
        color = "green"
      case 2 =>
        color = "red"
      case 3 =>
        color = "purple"
      case 4 =>
        color = "black"
      case 5 =>
        color = "yellow"
    }

    for(i <- 0 to 59) {
      val (p, found) = Game.Current.board.getPiece(i)
      if(found) {
        if(p.Color == color) Sprites.changeVisibility(i)
      } else {
        dom.console.log(s"Piece #$i was not found!")
      }
    }
  }

  // Undoes the previous move. Can be called until all
  // pieces are restored to their original positions.
  // Returns true when a move is undone.
  @JSExport
  def undo(): Boolean = {
    val len = Game.Current.history.length
    if(len == 0) {
      dom.console.log("No move to undo")
      return false
    }
    val lastMove = Game.Current.history(len-1)
    val ix = lastMove.initialTile.X
    val iy = lastMove.initialTile.Y
    val fx = lastMove.finalTile.X
    val fy = lastMove.finalTile.Y

    val (p, found) = Game.Current.board.getPieceAt(fx, fy)
    if(!found) {
      dom.console.log("Undo failed!")
      return false
    }

    Game.Current.history = Game.Current.history.dropRight(1)
    p.setPosition(ix, iy, remember = false)
    val (px, py) = Position.of(ix, iy)
    Sprites.get(p.ID).position.set(px, py)
    dom.console.log(s"Undo: ($fx, $fy) -> ($ix, $iy)")
    true
  }

  // Restores game to it's initial state.
  @JSExport
  def reset(): Unit = {
    dom.console.log("Reseting game")
    Game.save()
    while(undo()) {}
    Game.reset()
  }
}
