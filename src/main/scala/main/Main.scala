package main

import org.scalajs.dom.console
import scala.scalajs.js.annotation.{JSExportTopLevel, JSExport}

import main.logic.Game
import main.ui.Display

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

}
