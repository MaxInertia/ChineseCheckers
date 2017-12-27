package main.ui

import com.outr.pixijs.PIXI
import main.logic.hex.Grid
import main.ui.Display.stage

/**
  * Created by Dorian Thiessen on 2017-12-21.
  */
class Board(centerX: Double, centerY: Double) {
  private val hexSide = 28
  private val hexHeight: Double = hexSide * 2
  private val hexWidth: Double = hexSide * math.sqrt(3)

  private val hmso2: Double = (hexHeight - hexSide)/2  // Height minus side over 2

  var tiles: Map[(Int, Int), Hex] = Map()

  // Offset from directions in hex.Grid by 2
  val directions = Array(
    new Position(-hexWidth/2, hexHeight - hmso2), // 2
    new Position(-hexWidth, 0),                   // 3
    new Position(-hexWidth/2, hmso2 - hexHeight), // 4
    new Position(hexWidth/2, hmso2 - hexHeight),  // 5
    new Position(hexWidth, 0),                    // 0
    new Position(hexWidth/2, hexHeight - hmso2),  // 1
  )

  // Draws the game board
  def drawBoard(): Unit = {
    drawHexagon(centerX, centerY, 0, 0) // Center tile
    // All other central tiles (rings 1-4)
    for(r <- 1 to 4) drawRing(r)
    // Spawn tiles
    for(r <- 5 to 8) drawPartialRing(r)
  }

  private def drawRing(ring: Int): Unit = {
    var xD = centerX + ring*hexWidth
    var yD = centerY

    var xM = ring
    var yM = 0

    for (i <- 0 to 5) {
      val dir = directions(i)
      val mDir = Grid.directions( (i+2)%6 )

      var counter = ring
      while(counter > 0) {
        // Update display coordinates
        xD += dir.X
        yD += dir.Y
        // Update model coordinates
        xM += mDir.X
        yM += mDir.Y
        // Draw the hexagon
        drawHexagon(xD, yD, xM, yM)
        counter -= 1
      }
    }
  }

  private def drawPartialRing(ring: Int): Unit = {
    var xD = centerX + ring*hexWidth
    var yD = centerY
    var xM = ring
    var yM = 0

    var skip = ring - 4
    for (i <- 0 to 5) {
      val dir = directions(i)
      val mDir = Grid.directions( (i+2)%6 )

      var counter = ring
      while(counter > 0) {
        // Update display coordinates
        xD += dir.X
        yD += dir.Y
        // Update model coordinates
        xM += mDir.X
        yM += mDir.Y
        // Draw the hexagon
        if(counter < 6 && counter > skip) drawHexagon(xD, yD, xM, yM, i)
        counter -= 1
      }
    }
  }

  // Draw hexagon
  private def drawHexagon(xD: Double, yD: Double, xM: Int, yM: Int, spawn: Int = -1): Unit = {
    val graphics = new PIXI.Graphics()
    // Set fill and line color
    graphics.lineStyle(2, 0x000000, 1)
    spawn match{
      case 0 => graphics.beginFill(0xffaaaa) // red
      case 1 => graphics.beginFill(0xeeccff) // purple
      case 2 => graphics.beginFill(0xaaaaaa) // black
      case 3 => graphics.beginFill(0xffffb4) // yellow
      case 4 => graphics.beginFill(0xaaccff) // blue
      case 5 => graphics.beginFill(0xbbff99) // green
      case -1 => graphics.beginFill(0xdddddd) // default
    }

    graphics.moveTo(xD, yD - hexHeight/2) // Top of the hexagon
    graphics.lineTo(xD+ hexWidth/2, yD - hmso2)
    graphics.lineTo(xD+ hexWidth/2, yD + hmso2) // Right edge
    graphics.lineTo(xD, yD + hexHeight/2) // Bottom of the hexagon
    graphics.lineTo(xD- hexWidth/2, yD + hmso2)
    graphics.lineTo(xD- hexWidth/2, yD - hmso2) // Left edge
    graphics.lineTo(xD, yD - hexHeight/2) // Back to the top

    graphics.endFill()
    val hex: Hex = new Hex(graphics, xD, yD)
    Events.setupTileListeners(graphics, xD, yD, xM, yM)
    tiles += ((xM, yM) -> hex)
    stage.addChild(graphics)
  }

}
