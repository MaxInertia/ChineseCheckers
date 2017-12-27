package main.logic

import org.scalatest.FunSpec
import main.logic._
import main.logic.hex._

class BoardTests extends FunSpec {

  describe("Board Clone") {

    it("Piece in original also in clone") {
      val board = Board()
      board.createPieceSet("blue")

      var boardClone: Board = board.DeepClone()
      //boardClone.pieces = board.pieces.clone()
      //boardClone.tiles = board.tiles.clone().asInstanceOf[Map[(Int, Int), Tile]]
      val (piece, ok) = boardClone.getPiece(0)
      assert(ok)
    }

    it("Moving a piece from the clone should not change the original") {
      val board = Board()
      board.createPieceSet("blue")
      var boardClone: Board = board.DeepClone()
      assert(board != boardClone)

      val piece = boardClone.getPiece(0)._1
      val (oPiece, ok2) = board.getPiece(0)
      val ok: Boolean = boardClone.movePiece(piece, piece.x + 1, piece.y + 1)
      assert(ok && piece.x != oPiece.x && piece.y != oPiece.y)
    }

    it("Moving a piece from the original should not change the clone") {
      val board = Board()
      board.createPieceSet("blue")
      var boardClone: Board = board.DeepClone()
      assert(board != boardClone)

      val piece = board.getPiece(0)._1
      val (oPiece, ok2) = boardClone.getPiece(0)
      val ok: Boolean = board.movePiece(piece, piece.x + 1, piece.y + 1)
      assert(ok && piece.x != oPiece.x && piece.y != oPiece.y)
    }
  }
}
