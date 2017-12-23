package main.logic

import main.logic.hex.Tile

/**
  * Generates list of possible moves.
  * Turns are returned as an array of tuples: (x, y)
  *
  * Created by Dorian Thiessen on 2017-12-22.
  */
private[logic] object MoveGenerator {

  def getMoves(tile: Tile, board: Board): Array[(Int, Int)] = {
    var seen: Map[(Int, Int), Boolean] = Map((tile.X, tile.Y) -> true)
    var possibleMoves: Array[(Int, Int)] = Array()
    var pending: Array[Tile] = Array()

    def nextNbrOver(t: Tile, dir: Int): Unit = {
      // The neighbor of nbr, in direction nDir
      val nbr2 = board.getNeighbor(t, dir)

      if (nbr2 != null && !nbr2.isOccupied) {
        val nbr2Coords = (nbr2.X, nbr2.Y)
        if(seen.contains(nbr2Coords)) return

        seen += (nbr2Coords -> true)
        possibleMoves = possibleMoves :+ nbr2Coords
        pending = pending :+ nbr2
      }
    }

    val immediateNeighbors = board.getNeighborsOf(tile)
    for((nDir, nbr) <- immediateNeighbors) {
      if(!nbr.isOccupied) possibleMoves = possibleMoves :+ (nbr.X, nbr.Y)
      else nextNbrOver(nbr, nDir)
    }

    while(pending.nonEmpty) {
      // Get the neighbors of pending tile
      var nbrs = board.getNeighborsOf(pending.head)
      pending = pending.tail

      // Check each of the selected tiles neighbors
      for ((nDir, nbr) <- nbrs if !seen.contains(nbr.X, nbr.Y)) {
        if (nbr.isOccupied) nextNbrOver(nbr, nDir)
      }
    }

    //dom.console.log(s"Found ${possibleMoves.length} possible moves")
    possibleMoves
  }

}
