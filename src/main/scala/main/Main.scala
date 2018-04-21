package main

import main.logic.Colors._
import org.scalajs.dom.console

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import main.logic._
import main.logic.players.{Human, SimpleBlueBot}
import main.ui.Display
import org.scalajs.dom

/**
  * Created by Dorian Thiessen on 2017-12-15.
  */
@JSExportTopLevel("ChCheckers")
object Main {

  var activeColors: Array[Color] = Array()
  var blue: Boolean = false
  var green: Boolean = false
  var red: Boolean = false
  var purple: Boolean = false
  var black: Boolean = false
  var yellow: Boolean = false

  @JSExport
  def makeGame(): Unit = {
    console.log("ChCheckers.makeGame called")
    val game = Game.init()
    Display.init(game)
  }

  @JSExport
  def activatePlayer(dir: Int): Unit = {
    dir match {
      case 0 => blue = !blue
      case 1 => green = !green
      case 2 => red = !red
      case 3 => purple = !purple
      case 4 => black = !black
      case 5 => yellow = !yellow
    }
  }

  @JSExport
  def start(): Unit = {
    dom.console.log("start() called")

    if(blue) activeColors = activeColors :+ Blue
    if(green) activeColors = activeColors :+ Green
    if(red) activeColors = activeColors :+ Red
    if(purple) activeColors = activeColors :+ Purple
    if(black) activeColors = activeColors :+ Black
    if(yellow) activeColors = activeColors :+ Yellow

    Game.start(activeColors)
    Display.createPieceSprites(Game.Current)

    for(color <- activeColors) {
      // Create Player that has ownership over pieces of the current color
      dom.console.log("Adding a player")
      //TODO: Take name as argument
      if(color == Blue) {
        Game.Current.players = Game.Current.players :+ new SimpleBlueBot()
      } else {
        Game.Current.players = Game.Current.players :+ new Human("HUMAN", color)
      }
    }

    // Trigger first turn
    Game.Current.switchTurns()

    dom.console.log("start() ending")
  }

  // Undoes the previous move. Can be called until all
  // pieces are restored to their original positions.
  // Returns true when a move is undone.
  @JSExport
  def undo(): Boolean = {
    val (pid, x, y) = Game.Current.requestUndo()
    if(pid != -1) {
      // undo request accepted
      Display.Sprites.move(pid, x, y)
      true
    }
    false
  }

  // Restores game to it's initial state.
  /*@JSExport
  def reset(): Unit = {
    dom.console.log("Reseting game")
    Game.save()
    while(undo()) {}
    Game.reset()
  }*/
}
