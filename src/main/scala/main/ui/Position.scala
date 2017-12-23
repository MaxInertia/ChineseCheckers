package main.ui

import org.scalajs.dom

// Position on the display
class Position {
  var X: Double = 0
  var Y: Double = 0

  def this(x: Double, y: Double) {
    this()
    Y = y
    X = x
  }

  def set(x: Double, y: Double): Unit = {X = x; Y = y}
}

object Position {
  // Converts the coordinates of a tile into a position on the stage
  def of(xb: Int, yb: Int): (Double, Double) = {
    if(Display.board.tiles.contains(xb, yb)) {
      val hex = Display.board.tiles(xb, yb)
      //dom.console.log(s"board position mapped: ($xb, $yb) => (${hex.XD}, ${hex.YD})")
      (hex.XD, hex.YD)
    } else {
      dom.console.log(s"Position.of() Error: Map does not contain key: ($xb, $yb)")
      (0, 0)
    }
  }
}