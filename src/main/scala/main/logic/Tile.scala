package main.logic

import org.scalajs.dom

/**
  * Created by Dorian Thiessen on 2017-12-15.
  */
class Tile(x: Int, y: Int) {
  if(!Tile.isValid(x, y))
    dom.console.log(s"WHOOPS! Invalid board position created: ($x, $y)")

  def X: Int = x
  def Y: Int = y
}

object Tile {
  def isValid(xPos: Int, yPos: Int): Boolean = {
    val x: Int = math.abs(xPos)
    val y: Int = math.abs(yPos)

    // Pieces not in a spawn location + a few in spawn location
    if( x <= 8 && y <= 4 ) {
      if (x % 2 == y % 2) return true
      else {
        dom.console.log(s"x: $x, y: $y")
        return false
      }
    }

    // Exclusively pieces in a spawn location
    for(p <- Board.tbPositions)
      if(x == math.abs(p._1) && y == math.abs(p._2)) return true
    for(p <- Board.lrPositions)
      if(x == math.abs(p._2) && y == math.abs(p._1)) return true
    false
  }
}