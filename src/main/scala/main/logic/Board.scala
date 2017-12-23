package main.logic

import main.logic.hex.{Grid, Tile}
import org.scalajs.dom

/**
  * Created by Dorian Thiessen on 2017-12-15.
  */
class Board extends Grid {
  private[logic] var pieces: Array[Piece] = Array[Piece]()
  tiles = Grid.createTiles()
  dom.console.log(s"Grid created with ${tiles.size} tiles.")

  def addPiece(piece: Piece, x: Int, y: Int): Unit = {
    val tile: Tile = tiles((x, y))
    if(tile.content != null) print("Warning: Added piece to occupied location.")
    tile.content = piece
    pieces = pieces :+ piece
  }

  // Get piece via unique identifier
  def getPiece(id: Int): (Piece, Boolean) = {
    if (id >= pieces.length) (null, false)
    else (pieces(id), true)
  }

  // Get piece via board coordinates
  def getPieceAt(x: Int, y: Int): Piece = {
    var tile = tiles((x, y))
    if(tile != null) tile.content else null
  }

  def movePiece(piece: Piece, x: Int, y: Int): Boolean = {
    if(piece == null) {
      dom.console.log("Error: Initial position of requested move is empty")
      return false
    }
    if(!Grid.isValidTile(x, y)) {
      dom.console.log(s"Error: requested move is to an invalid position: ($x, $y)")
      return false
    }

    //TODO: Check if move is allowed

    // Here it is assumed that the move is valid. Perform the move.
    tiles(piece.x, piece.y).content = null
    tiles(x, y).content = piece
    piece.x = x
    piece.y = y
    true
  }

  private val pc: Array[Int] = Array[Int](0, 0, 0, 0, 0, 0)
  def numPieces: Int = pc.sum

  def createPieceSet(color: String): Unit = for(i <- 0 to 9) createPiece(color)

  def createPiece(color: String) {
    var xBoard = 0
    var yBoard = 0
    color match {
      case "purple" => // Bottom
        xBoard = -Grid.pSpawnPositions(pc(0))._1
        yBoard = Grid.pSpawnPositions(pc(0))._2
        pc(0) += 1
      case "blue" => // Top
        xBoard = Grid.pSpawnPositions(pc(1))._1
        yBoard = -Grid.pSpawnPositions(pc(1))._2
        pc(1) += 1

      case "black" => // Bottom-Left
        xBoard = -Grid.pSpawnPositions(pc(2))._2
        yBoard = Grid.pSpawnPositions(pc(2))._1
        pc(2) += 1
      case "green" => // Top-Right
        xBoard = Grid.pSpawnPositions(pc(3))._2
        yBoard = -Grid.pSpawnPositions(pc(3))._1
        pc(3) += 1

      case "yellow" => // Top-Left
        xBoard = -Grid.sSpawnPositions(pc(4))._1
        yBoard = -Grid.sSpawnPositions(pc(4))._2
        pc(4) += 1
      case "red" => // Bottom-Right
        xBoard = Grid.sSpawnPositions(pc(5))._1
        yBoard = Grid.sSpawnPositions(pc(5))._2
        pc(5) += 1
      case default =>
        dom.console.log(s"Unkown color: $color")
    }

    dom.console.log(s"Creating and adding $color piece to board at ($xBoard, $yBoard)")
    val piece = new Piece(color, numPieces - 1)
    piece.x = xBoard
    piece.y = yBoard
    addPiece(piece, xBoard, yBoard)
  }
}

object Board {
  // Counter to keep track of the number of pieces created in each color

  // Creates a new piece.
  // Returns: ID, x position, and y position of the piece as a triple
  def apply(): Board = {
    new Board()
    /*for(i <- 0 to 59) {
      if(i<10) {
        createPiece("purple")
      } else if(i<20) {
        createPiece("blue")
      } else if(i<30) {
        createPiece("yellow")
      } else if(i<40) {
        createPiece("black")
      } else if(i<50) {
        createPiece("red")
      } else {
        createPiece("green")
      }
    }*/
  }


}