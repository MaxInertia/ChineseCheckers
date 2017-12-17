package main.logic

import org.scalajs.dom

/**
  * Created by Dorian Thiessen on 2017-12-15.
  */
class Piece(color: String, id: Int) {
  private var position: Tile = _

  def ID: Int = id
  def Color: String = color

  def Pos: Tile = position
  def setPosition(x: Int, y: Int, remember: Boolean = true): Boolean = {
    if(!Tile.isValid(x, y)) {
      dom.console.log(s"piece.setPosition received invalid tile: ($x, $y)")
      return false
    }
    val oldPosition = position
    position = new Tile(x, y)
    if(remember && oldPosition != null) Game.Current.registerMove(oldPosition, position)
    true
  }
}

object Piece {

  // Counter to keep track of the number of pieces created in each color
  val pc: Array[Int] = Array[Int](0, 0, 0, 0, 0, 0)
  def numPieces: Int = pc.sum // Same as: pc.fold(0)(_ + _)

  // Creates a new piece.
  // Returns: ID, x position, and y position of the piece as a triple
  def create(color: String): (Int, Int, Int) = {
    var xBoard = 0
    var yBoard = 0
    color match {
      case "purple" => // Bottom
        xBoard = Board.tbPositions(pc(0))._1
        yBoard = Board.tbPositions(pc(0))._2
        pc(0) += 1
      case "blue" => // Top
        xBoard = Board.tbPositions(pc(1))._1
        yBoard = -Board.tbPositions(pc(1))._2
        pc(1) += 1
      case "yellow" => // Top-Left
        xBoard = -Board.lrPositions(pc(2))._2
        yBoard = -Board.lrPositions(pc(2))._1
        pc(2) += 1
      case "black" => // Bottom-Left
        xBoard = -Board.lrPositions(pc(3))._2
        yBoard = Board.lrPositions(pc(3))._1
        pc(3) += 1
      case "red" => // Bottom-Right
        xBoard = Board.lrPositions(pc(4))._2
        yBoard = Board.lrPositions(pc(4))._1
        pc(4) += 1
      case "green" => // Top-Right
        xBoard = Board.lrPositions(pc(5))._2
        yBoard = -Board.lrPositions(pc(5))._1
        pc(5) += 1
      case default =>
        dom.console.log(s"Unkown color: $color")
    }

    val piece = new Piece(color, numPieces - 1)
    piece.setPosition(xBoard, yBoard)
    Game.Current.board.addPiece(piece)
    (piece.ID, xBoard, yBoard)
  }
}