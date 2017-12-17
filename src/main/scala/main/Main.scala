package main

import org.scalajs.dom.console

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import main.logic.{Game, Piece}
import main.ui.Display
import org.scalajs.dom

/**
  * Created by Dorian Thiessen on 2017-12-15.
  */
@JSExportTopLevel("IFMain")
object Main {

  @JSExport
  def makeGame(): Unit = {
    console.log("IFMain.makeGame called")
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
    for(p <- Game.Current.pc) {
      if(p.Color == color) {
        p.setVisibility(!p.Visible())
      }
    }
  }

  @JSExport
  def undo(): Unit = {
    val len = Game.Current.history.length
    if(len == 0) {
      dom.console.log("No move to undo")
      return
    }
    val lastMove = Game.Current.history(len-1)
    val ix = lastMove.initialPosition.X
    val iy = lastMove.initialPosition.Y
    val fx = lastMove.finalPosition.X
    val fy = lastMove.finalPosition.Y
    dom.console.log(s"Undo: ($fx, $fy) -> ($ix, $iy)")
    Game.Current.history = Game.Current.history.dropRight(1)
    for(p <- Game.Current.pc) {
      if(p.Pos.X == fx && p.Pos.Y == fy) {
        p.setPosition(ix, iy, add2History = false)
        p.Sprite.position.set(
          Display.BoardInfo.Width/2 + Display.BoardInfo.dx*ix,
          Display.BoardInfo.Height/2 + Display.BoardInfo.dy*iy
        )
        dom.console.log("Undo performed")
      }
    }
  }

}
