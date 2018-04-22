/**
* Inspiration for this package, as well as much of it's contents,
* comes from here: https://www.redblobgames.com/grids/hexagons/
*/
package main.logic.board



/**
  * A grid composed of hexagonal tiles.
  *
  * There are 121 tiles total.
  *   Inner tiles: 61
  *   Spawn tiles: 6 spawns each with 10 tiles
  *
  * Created by Dorian Thiessen on 2017-12-20.
  */
private class BoardGrid {
  var tiles: Map[(Int, Int), Tile] = _

  /** Returns an array of tuples (direction, tile).
    *   direction: The direction of the neighbor tile
    *   tile: The neighbor
    */
  def getNeighborsOf(tile: Tile): Array[(Int, Tile)] = getNeighborsOf(tile.X, tile.Y)
  def getNeighborsOf(tx: Int, ty: Int): Array[(Int, Tile)]= {
    var nbrs: Array[(Int, Tile)] = Array()

    var dirNum = 0
    for(dir <- Grid.directions) {
      if(Grid.isValidTile(tx + dir.X, ty + dir.Y))
        nbrs = nbrs :+ (dirNum, tiles((tx + dir.X, ty + dir.Y)))
      dirNum += 1
    }

    nbrs
  }

  /** Returns the neighbor of tile in the specified direction */
  def getNeighbor(tile: Tile, direction: Int): Tile = getNeighbor(tile.X, tile.Y, direction)
  def getNeighbor(tx: Int, ty: Int, direction: Int): Tile = {
    val dx = Grid.directions(direction).X
    val dy = Grid.directions(direction).Y
    if(Grid.isValidTile(tx + dx, ty + dy)) return tiles((tx + dx, ty + dy))
    null
  }

}

object Grid {
  // Tile transformations to possible neighboring positions
  // Some of these will be invalid for tiles on an edge.
  var directions: Array[Tile] = Array(
    Tile(1, 0), Tile( 0, 1), // RR, RD
    Tile(-1,  1), Tile(-1, 0), // LD, LL
    Tile( 0, -1), Tile(+1, -1)) // LU, RU

  // Tile transformations to positions within distance
  // 2 that cannot be moved to in a single move.
  var diagonals: Array[Tile] = Array(
    Tile(1, 1), Tile(-1, 2),
    Tile(-2, 1), Tile(-1, -1),
    Tile(1, -2), Tile(2, -1))

  def apply(): Grid = {
    val grid = new Grid()
    grid.tiles = createTiles()
    grid
  }

  def createTiles(): Map[(Int, Int), Tile] = {
    var tiles = Map[(Int, Int), Tile]()

    // For each possible tile adjacent to the tile at (x,y),
    // create that tile if the position is valid and it does not yet exist.
    def createNeighbors(x: Int, y: Int): Unit = {
      for(direction <- directions) {
        var tile = tiles.get(x+direction.X, y+direction.Y)
        if( tile.isEmpty && isValidTile(x+direction.X, y+direction.Y) ) {
          var tile = new Tile(x+direction.X, y+direction.Y)
          tiles += ((x+direction.X, y+direction.Y) -> tile)
          createNeighbors(tile.X, tile.Y)
        }
      }
    }

    var centerTile = new Tile(0, 0)
    tiles += ((0, 0) -> centerTile)
    createNeighbors(centerTile.X, centerTile.Y)

    tiles // Returning the grid
  }

  // Coordinates of the triangle in direction 4.5 (up)
  val pSpawnPositions = Array( // (dx, dy)
    (1, 5), (2, 5), (3, 5), (4, 5),
    (2, 6), (3, 6), (4, 6),
    (3, 7), (4, 7),
    (4, 8))

  // Coordinates of the triangle in direction 1 (down-right)
  val sSpawnPositions = Array( // (dx, dy)
    (1, 4), (2, 3), (3, 2), (4, 1),
    (2, 4), (3, 3), (4, 2),
    (3, 4), (4, 3),
    (4, 4))

  // Checks if a position, specified by x and y coordinates, is valid.
  // Only used when initializing the main.logic.hex grid.
  def isValidTile(x: Int, y: Int): Boolean = {
    val xP = math.abs(x)
    val yP = math.abs(y)

    // Central tiles
    if(xP < 5 && yP < 5) return true

    // Handles spawn triangles
    for(p <- pSpawnPositions)
      if(x*y<0 && ( (p._1 == xP && p._2 == yP) || (p._2 == xP && p._1 == yP) )) {
        return true
      }
    for(p <- sSpawnPositions)
      if( (p._1 == x && p._2 == y) || (-p._1 == x && -p._2 == y) ) {
        return true
      }

    false // Position is not valid
  }
}