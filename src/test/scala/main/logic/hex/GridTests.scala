package main.logic.hex

/**
  * Created by Dorian Thiessen on 2017-12-21.
  */

import main.logic.board.Grid
import org.scalatest.FunSpec

class GridTests extends FunSpec {
  var grid = Grid() // Pre-testing setup

  describe("Grid Properties"){
    it("should be initialized with 121 tiles") {
      assert(grid.tiles.size == 121)
    }
  }

  describe("Central Tiles") {
    it("Tile (0, 0) should have 6 neighbors") {
      val nbrs = grid.getNeighborsOf(tx = 0, ty = 0)
      assert(nbrs.length == 6)
    }

    it("Tile (0, 0) should have tile (1, 0) to the right (direction 0)") {
      val nbr = grid.getNeighbor(tx = 0, ty = 0, direction = 0)
      assert(nbr.X == 1 && nbr.Y == 0)
    }

    it("Tile (0,0) should have tile (-1, 0) to it's left (direction 3)") {
      val nbr = grid.getNeighbor(tx = 0, ty = 0, direction = 3)
      assert(nbr.X == -1 && nbr.Y == 0)
    }

    // Inner corners

    it("Tile (4, 0) should have no neighbor in direction 0 (right)") {
      val nbr = grid.getNeighbor(tx = 4, ty = 0, direction = 0)
      assert(nbr == null)
    }

    it("Tile (4, 0) should have 5 neighbors") {
      val nbrs = grid.getNeighborsOf(tx = 4, ty = 0)
      assert(nbrs.length == 5)
    }

    it("Tile (0, 4) should have 5 neighbors") {
      val nbrs = grid.getNeighborsOf(tx = 0, ty = 4)
      assert(nbrs.length == 5)
    }

    it("Tile (0, -4) should have 5 neighbors") {
      val nbrs = grid.getNeighborsOf(tx = 0, ty = -4)
      assert(nbrs.length == 5)
    }

    it("Tile (-4, 4) should have 5 neighbors") {
      val nbrs = grid.getNeighborsOf(tx = -4, ty = 4)
      assert(nbrs.length == 5)
    }

    it("Tile (-4, 0) should have 5 neighbors") {
      val nbrs = grid.getNeighborsOf(tx = -4, ty = 0)
      assert(nbrs.length == 5)
    }

    it("Tile (4, -4) should have 5 neighbors") {
      val nbrs = grid.getNeighborsOf(tx = 4, ty = -4)
      assert(nbrs.length == 5)
    }

    it("Tile (0, 4) should have no neighbor in direction 1 (down-right)") {
      val nbr = grid.getNeighbor(tx = 0, ty = 4, direction = 1)
      assert(nbr == null)
    }

    it("Tile (-4, 4) should have no neighbor in direction 2 (down-left)") {
      val nbr = grid.getNeighbor(tx = -4, ty = 4, direction = 2)
      assert(nbr == null)
    }

    it("Tile (-4, 0) should have no neighbor in direction 3 (left)") {
      val nbr = grid.getNeighbor(tx = -4, ty = 0, direction = 3)
      assert(nbr == null)
    }

    it("Tile (0, -4) should have no neighbor in direction 4 (up-left)") {
      val nbr = grid.getNeighbor(tx = 0, ty = -4, direction = 4)
      assert(nbr == null)
    }


    it("Tile (4, -4) should have no neighbor in direction 5 (up-right)") {
      val nbr = grid.getNeighbor(tx = 4, ty = -4, direction = 5)
      assert(nbr == null)
    }

  }

  describe("Spawn Tiles") {

    it("Tile (4, 4) should have 2 neighbors") {
      val nbrs = grid.getNeighborsOf(tx = 4, ty = 4)
      assert(nbrs.length == 2)
    }

    it("Tile (-4, 8) should have 2 neighbors") {
      val nbrs = grid.getNeighborsOf(tx = -4, ty = 8)
      assert(nbrs.length == 2)
    }

    it("Tile (-8, 4) should have 2 neighbors") {
      val nbrs = grid.getNeighborsOf(tx = -8, ty = 4)
      assert(nbrs.length == 2)
    }

    it("Tile (-4, -4) should have 2 neighbors") {
      val nbrs = grid.getNeighborsOf(tx = -4, ty = -4)
      assert(nbrs.length == 2)
    }

    it("Tile (4, -8) should have 2 neighbors") {
      val nbrs = grid.getNeighborsOf(tx = 4, ty = -8)
      assert(nbrs.length == 2)
    }

    it("Tile (8, -4) should have 2 neighbors") {
      val nbrs = grid.getNeighborsOf(tx = 8, ty = -4)
      assert(nbrs.length == 2)
    }
  }
}