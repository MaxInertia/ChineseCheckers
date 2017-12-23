package main

import org.scalajs.dom.console

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import main.logic.{Game, Piece}
import main.logic.players.{Human, Player}
import main.ui.Display
import org.scalajs.dom

/**
  * Created by Dorian Thiessen on 2017-12-15.
  */
@JSExportTopLevel("ChCheckers")
object Main {

  var activeColors: Array[String] = Array()
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

    if(blue) activeColors = activeColors :+ "blue"
    if(green) activeColors = activeColors :+ "green"
    if(red) activeColors = activeColors :+ "red"
    if(purple) activeColors = activeColors :+ "purple"
    if(black) activeColors = activeColors :+ "black"
    if(yellow) activeColors = activeColors :+ "yellow"

    Game.start(activeColors)
    Display.createPieceSprites(Game.Current)

    for(color <- activeColors) {
      // Create Player that has ownership over pieces of the current color
      dom.console.log("Adding a player")
      //TODO: Take name as argument
      Game.Current.players = Game.Current.players :+ new Human("HUMAN", color)
    }

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
