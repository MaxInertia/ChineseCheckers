package main.logic.board

import Colors._

/**
  * Created by Dorian Thiessen on 2018-04-21.
  */
object Pieces {

  def build(color: Color, id: Int): Piece = Piece(color, id)

  case class Piece(color: Color, id: Int) {
    private[logic] var x: Int = _
    private[logic] var y: Int = _
    def X: Int = x
    def Y: Int = y
  }

  /*trait Piece {
    private[logic] var x: Int = _
    private[logic] var y: Int = _
    def color: Color
    def X: Int = x
    def Y: Int = y
  }

  case class BluePiece(id: Int) extends Piece {
    def ID: Int = id
    def color: Color = Blue
  }

  case class GreenPiece(id: Int) extends Piece {
    def ID: Int = id
    def color: Color = Green
  }

  case class RedPiece(id: Int) extends Piece {
    def ID: Int = id
    def color: Color = Red
  }

  case class PurplePiece(id: Int) extends Piece {
    def ID: Int = id
    def color: Color = Purple
  }

  case class BlackPiece(id: Int) extends Piece {
    def ID: Int = id
    def color: Color = Black
  }

  case class YellowPiece(id: Int) extends Piece {
    def ID: Int = id
    def color: Color = Yellow
  }*/
}
