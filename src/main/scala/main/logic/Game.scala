package main.logic

import main.logic.hex.{Grid, Tile}
import main.logic.players.Player
import main.ui.Position
import org.scalajs.dom

/**
  * Created by Dorian Thiessen on 2017-12-15.
  */
class Game {
  // Index of active Player
  var activePI: Int = 0
  // Array of players (2 - 6)
  var players: Array[Player] = Array()
  // The game board. Contains game pieces.
  var board: Board = Board()
  // A record of each move in the game
  var history: Array[Game#Move] = Array()

  // Start game loop
  def loop(): Unit = {
    // Notify player that it is their turn
    //dom.console.log(s"Player $activePI now active")
    players(activePI).notifyOfTurn()
    //dom.console.log(s"Player $activePI now inactive")

    activePI = activePI + 1
    if(activePI == players.length) {
      activePI = 0
      return
    }
    //loop()
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

  def requestUndo(): (Int, Int, Int) = {
    val len = Game.Current.history.length
    if(len == 0) {
      dom.console.log("No move to undo")
      return (-1, -1, -1)
    }

    val lastMove = Game.Current.history(len-1)
    val ix = lastMove.iX
    val iy = lastMove.iY
    val fx = lastMove.fX
    val fy = lastMove.fY

    val p = Game.Current.board.getPieceAt(fx, fy)
    if(p == null) {
      dom.console.log("Undo failed! Piece missing...")
      return (-1, -1, -1)
    }

    Game.Current.history = Game.Current.history.dropRight(1)
    board.movePiece(p, ix, iy)
    val (px, py) = Position.of(ix, iy)
    dom.console.log(s"Undo: ($fx, $fy) -> ($ix, $iy)")
    (p.ID, ix, iy)
  }

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

  def getAllPieces: Array[Piece] = board.pieces

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
}

object Game {
  // Previous games, saved as a sequence of moves.
  private var previous: Array[Array[Game#Move]] = Array() //TODO: Save to file

  private var current: Game = _
  def Current: Game = current

  def init(): Game = {
    dom.console.log("Game.init() called")
    if(current == null) {
      current = new Game()
    }
    //dom.console.log(s"Piece count: ${current.getAllPieces.length}")
    dom.console.log("Game.init() ending")
    current
  }

  def start(colors: Array[String]): Unit = {
    for(c <- colors) {
      Current.board.createPieceSet(c)
    }
  }

  def save(): Unit = previous = previous :+ current.history.clone()

  def reset(): Unit = {
    val newGame = new Game()
    newGame.board = current.board
    newGame.players = current.players
    current = newGame
  }

  //TODO: Implement replay()
}