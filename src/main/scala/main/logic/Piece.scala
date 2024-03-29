package main.logic

import main.logic.hex.{Grid, Tile}
import org.scalajs.dom

/**
  * Created by Dorian Thiessen on 2017-12-15.
  */
class Piece(color: String, id: Int) {
  private[logic] var x: Int = _
  private[logic] var y: Int = _

  def ID: Int = id
  def Color: String = color
  def X: Int = x
  def Y: Int = y
}