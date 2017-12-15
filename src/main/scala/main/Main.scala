package main

import com.outr.pixijs.PIXI.{Sprite, Texture}
import org.scalajs.dom.{console, document, window}

import scala.scalajs.js.annotation.{JSExportStatic, JSExportTopLevel, ScalaJSDefined}
import scala.scalajs.js
import scala.scalajs.js.annotation._
import com.outr.pixijs._
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

    Display.moveTest()
  }

}
