package main.logic

/**
  * Created by Dorian Thiessen on 2017-12-15.
  */
class Board() {
  val pieces: Array[Piece] = Array()
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