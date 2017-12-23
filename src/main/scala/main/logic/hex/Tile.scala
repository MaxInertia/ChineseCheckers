package main.logic.hex

import main.logic.Piece

import scala.scalajs.js.annotation.JSExportTopLevel

/** Tile that defines a position on a hexagonal grid with two numbers: x and y
  * as well as it's contents.
  *
  * Created by Dorian Thiessen on 2017-12-21.
  */
@JSExportTopLevel("Tile")
class Tile(x: Int, y: Int) {
  var content: Piece = _

  def X: Int = x
  def Y: Int = y
  /** For internal use only. Allows this to be treated like a cube Tile. */
  private def Z: Int = -x - y

  def isOccupied: Boolean = content != null
}

object Tile {
  def apply(x: Int, y: Int): Tile = new Tile(x, y)
}