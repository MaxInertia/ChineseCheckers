package main.space

/**
  * A space of arbitrary dimensionality.
  *   1D: Row, Column, List, ...
  *   2D: Square grid, hexagonal grid, ...
  *   3D: Quantized space (Made up of quanta: Cells, ie: Not continuous)
  *   ...
  *
  * Created by Dorian Thiessen on 2018-04-21.
  */
trait Space[D <: Direction, L <: Location] {
  /**
    * @param location Some location in a Space
    * @return List of tuples corresponding to each adjacent location.
    *         (1) The direction of the adjacent location
    *         (2) and a reference to the location
    */
  def getNeighborsOf(location: L): List[(D, L)]
  def getNeighbor(location: L, direction: D): L
}