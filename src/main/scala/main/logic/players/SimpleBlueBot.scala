package main.logic.players

import main.logic.{Blue, Game, Piece}
import main.ui.Display
import org.scalajs.dom

/**
  * Bot that only works (although only partially) for the blue team.
  * This bot exists mostly to test player switching for each turn.
  *
  * Looks only one move ahead, selects the move that results in the
  * largest decrease in y-coordinate (the move that moves it furthest
  * down the table).
  *
  * Created by Dorian Thiessen on 2017-12-23.
  */
class SimpleBlueBot() extends Player("BLUE BOT", Blue) {

  def notifyOfTurn(): Unit = {
    dom.console.log(s"It is now $Name's turn")

    val pieces = Game.Current.getAllPieces

    var largestDrop = 0 // Largest drop in y-value of any move
    var piece: Piece = null
    var move: (Int, Int) = null

    // For all pieces owned by this player
    for(p <- pieces if p.Color == Color) {
      val moves = Game.Current.requestPossibleMoves(p.ID)
      for(m <- moves) {
        //dom.console.log(s"Drop for this move: ${m._2 - p.Y}")
        if(m._2 - p.Y > largestDrop) {
          largestDrop = m._2 - p.Y
          piece = p
          move = m
        }
      }
    }

    if(piece == null || move == null) {
      dom.console.log(s"$Name was unable to choose a move!")
      return
    }

    val moveOk = Game.Current.requestMove(piece.X, piece.Y, move._1, move._2)
    if(!moveOk) {
      dom.console.log(s"$Name selected a move which was rejected...")
    } else {
      Display.Sprites.move(piece.ID, move._1, move._2)
    }

    // Switch turns, regardless of whether it could move or not.
    Game.Current.switchTurns()
  }



}
