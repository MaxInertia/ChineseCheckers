package main.logic

import org.scalajs.dom

/**
  * Created by Dorian Thiessen on 2017-12-15.
  */
class Board() {
  private var pieces: Array[Piece] = Array[Piece]()

  def addPiece(piece: Piece): Unit = {
    dom.console.log("Adding piece to board")
    pieces = pieces :+ piece
  }

  // Get piece via unique identifier
  def getPiece(id: Int): (Piece, Boolean) = {
    if (id >= pieces.length) (null, false)
    else (pieces(id), true)
  }

  // Get piece via board coordinates
  def getPieceAt(x: Int, y: Int): (Piece, Boolean) = {
    for (p <- pieces if p.Pos.X == x && p.Pos.Y == y) return (p, true)
    dom.console.log(s"Piece not found at ($x, $y)")
    (null, false)
  }
}

object Board {
  // For position of top & bottom pieces
  val tbPositions = Array( // (dx, dy)
    (-3, 5), (-1, 5), (1, 5), (3, 5),
    (-2, 6), (0, 6), (2, 6),
    (1, 7), (-1, 7),
    (0, 8))
  // For position of pieces on {top | bottom} + {left | right}
  val lrPositions = Array( // (dy, dx)
    (4, 12), (4, 10), (4, 8), (4, 6),
    (3, 11), (3, 9), (3, 7),
    (2, 10), (2, 8),
    (1, 9))
}