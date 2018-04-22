package main.logic

import main.logic.board.Colors.Color
import main.logic.board.Pieces.Piece
import main.logic.players.Player
import main.ui.Position
import org.scalajs.dom

/**
  * Created by Dorian Thiessen on 2017-12-15.
  */
object ChineseCheckers {
  // Index of active Player
  var activePI: Int = -1
  // Array of players (2 - 6)
  var players: Array[Player] = Array()
  // The game board. Contains game pieces.
  lazy val board: Board = Board()
  // A record of each move in the game
  var history: Array[Move] = Array()

  def switchTurns(): Unit = {
    // Increment active player index
    activePI = (activePI + 1) % players.length
    // Notify player that it is their turn
    dom.console.log(s"Player $activePI now active")
    players(activePI).notifyOfTurn()
  }

  /** Called when a player attempts making a move.
    * Performs the move and returns true if it is a valid move.
    * Returns false if move is invalid.*/
  def requestMove(ix: Int, iy: Int, fx: Int, fy: Int): Boolean = {
    // Get piece being moved
    val piece = board.getPieceAt(ix, iy)

    // Move the piece on the board, register move if successful
    if(board.movePiece(piece, fx, fy)) {
      registerMove(ix, iy, fx, fy)
      return true
    }
    false
  }

  /** Called when a player requests a move to be undone. */
  def requestUndo(): (Int, Int, Int) = {
    val len = ChineseCheckers.history.length
    if(len == 0) {
      dom.console.log("No move to undo")
      return (-1, -1, -1)
    }

    val lastMove = ChineseCheckers.history(len-1)
    val ix = lastMove.iX
    val iy = lastMove.iY
    val fx = lastMove.fX
    val fy = lastMove.fY

    val p = ChineseCheckers.board.getPieceAt(fx, fy)
    if(p == null) {
      dom.console.log("Undo failed! Piece missing...")
      return (-1, -1, -1)
    }

    ChineseCheckers.history = ChineseCheckers.history.dropRight(1)
    board.movePiece(p, ix, iy)
    val (px, py) = Position.of(ix, iy)
    dom.console.log(s"Undo: ($fx, $fy) -> ($ix, $iy)")
    (p.id, ix, iy)
  }

  /** Returns an array of all possible moves for the piece specified by id */
  def requestPossibleMoves(id: Int): Array[(Int, Int)] = {
    val (piece, found) = board.getPiece(id)
    if(!found) {
      dom.console.log(s"requestPossibleMoves() Error: Piece not found with id: $id")
      return null
    }
    if(!board.tiles.contains((piece.X, piece.Y))) {
      dom.console.log(s"requestPossibleMoves() Error: Tile not found at piece coordinates: (${piece.X}, ${piece.Y})")
    }
    MoveGenerator.getMoves(board.tiles(piece.X, piece.Y), board)
  }

  /** Returns an array of all game pieces in play */
  def getAllPieces: Array[Piece] = board.pieces

  /** Saves the specified move in the game history */
  private def registerMove(ix: Int, iy: Int, fx: Int, fy: Int): Unit = {
    // TODO: Replace dom.console logs with outr/scribe logs
    dom.console.log(s"Move: ($ix, $iy)" + s" -> ($fx, $fy)")
    history = history :+ new Move(ix, iy, fx, fy)
  }

  // Game Move. Elements of Game.history
  class Move(ix: Int, iy: Int, fx: Int, fy: Int) {
    def iX: Int = ix
    def iY: Int = iy
    def fX: Int = fx
    def fY: Int = fy
  }

  // --------------------------------

  def start(colors: Array[Color]): Unit = {
    for(c <- colors) {
      board.createPieceSet(c)
    }
  }
}