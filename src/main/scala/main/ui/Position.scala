package main.ui

import main.ui.Display.Dimensions
import org.scalajs.dom

// Position on the display
class Position {
  var X: Double = 0
  var Y: Double = 0

  def set(x: Double, y: Double): Unit = {X = x; Y = y}

  def boardX: Int = {
    val tmp = (X - Dimensions.Width/2) / Dimensions.dx
    var result: Int = 0
    //dom.console.log("")
    //dom.console.log(s"tx: $tmp")
    if(tmp < 0) {
      if(tmp - tmp.toInt < -0.5) {
        result = math.floor(tmp).toInt
      } else {
        result = math.ceil(tmp).toInt
      }
    } else {
      if(tmp - tmp.toInt >= 0.5) {
        result = math.ceil(tmp).toInt
      } else {
        result = math.floor(tmp).toInt
      }
    }
    //dom.console.log(s"tx result: $result")
    //dom.console.log("")
    result
  }

  def boardY: Int = {
    val tmp = (Y - Dimensions.Height/2) / Dimensions.dy
    var result: Int = 0
    //dom.console.log("")
    //dom.console.log(s"ty: $tmp")
    if(tmp < 0) {
      if(tmp - tmp.toInt < -0.5) {
        result = math.ceil(tmp).toInt
      } else {
        result = math.floor(tmp).toInt
      }
    } else {
      if(tmp - tmp.toInt >= 0.5) {
        result = math.ceil(tmp).toInt
      } else {
        result = math.floor(tmp).toInt
      }
    }
    //dom.console.log(s"ty result: $result")
    //dom.console.log("")
    result
  }

  def centerOnTile(): (Int, Int) = {
    // Distance from board center
    val x2c = X - Dimensions.Width/2
    val y2c = Y - Dimensions.Height/2

    // Determine board position closest to given coordinates
    // y-coord
    var boardY = y2c/Dimensions.dy
    if(math.abs(boardY - boardY.toInt) <= 0.5) {
      boardY = boardY.toInt
    } else if(math.abs(boardY - boardY.toInt) > 0.5) {
      if(boardY > 0) boardY = boardY.toInt + 1
      else boardY = boardY.toInt - 1
    }
    // x-coord
    var boardX = x2c/Dimensions.dx
    if(math.abs(math.ceil(boardX))%2 == math.abs(boardY)%2) {
      boardX = math.ceil(boardX)
    } else {
      boardX = math.floor(boardX)
    }

    //dom.console.log(s"Attempting to move to ($boardX, $boardY)")
    //X = Dimensions.Width/2 + boardX * Dimensions.dx
    //Y = Dimensions.Height/2 + boardY * Dimensions.dy
    val temp = Position.of(boardX.toInt, boardY.toInt)
    X = temp._1
    Y = temp._2
    (boardX.toInt, boardY.toInt)
  }
}

object Position {
  // Converts the coordinates of a tile into a position on the stage
  def of(x: Int, y: Int): (Double, Double) = (
    Dimensions.Width/2 + x * Dimensions.dx,
    Dimensions.Height/2 + y * Dimensions.dy
    )
}