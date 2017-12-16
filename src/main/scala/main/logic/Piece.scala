package main.logic

import com.outr.pixijs.PIXI
import main.ui.Display
import main.ui.Display.BoardInfo
import org.scalajs.dom

/**
  * Created by Dorian Thiessen on 2017-12-15.
  */
class Piece(sprite: PIXI.Sprite, color: String) {
  private var position: Position = _

  def Color: String = color
  def Position: Position = position
  def setPosition(x: Int, y: Int): Unit = {
    position = new Position(x, y)
  }
}

object Piece {
  def create(texture: PIXI.Texture, color: String, xPos: Int, yPos: Int): (Piece, PIXI.Sprite) = {

    // Calculate coordinates for sprites position on display
    val xDisplayPos = BoardInfo.Width/2 + BoardInfo.dx * xPos
    val yDisplayPos = BoardInfo.Height/2 + BoardInfo.dy * yPos

    // Create sprite
    val sprite = new PIXI.Sprite(texture) {
      anchor.x = 0.5
      anchor.y = 0.5
      position.x = xDisplayPos
      position.y = yDisplayPos
    }
    sprite.interactive = true
    sprite.buttonMode = true

    val newPiece = new Piece(sprite, color)
    newPiece.setPosition(xPos, yPos)

    // Action performed when mouse over sprite
    val onOver = () => {
      dom.console.log("isOver "+color)
      sprite.scale.x = 1.1
      sprite.scale.y = 1.1
    }
    // Action performed when mouse leaves sprite
    val onOut = () => {
      sprite.scale.x = 1
      sprite.scale.y = 1
    }
    // Action performed when mouse down over sprite
    val onDown = () => {
      sprite.scale.x = 1.2
      sprite.scale.y = 1.2
    }
    // Action performed when mouse up over sprite
    val onUp = () => {
      sprite.scale.x = 1.1
      sprite.scale.y = 1.1
    }

    sprite.on("mouseover", onOver)
    sprite.on("mouseout", onOut)
    sprite.on("mousedown", onDown)
    sprite.on("mouseup", onUp)


    var data: PIXI.interaction.InteractionData = null
    var dragging: Boolean = false
    var ix: Double = 0
    var iy: Double = 0

    val onDragStart = (event: PIXI.interaction.InteractionEvent) => {
      //TODO: Only allow dragging piece owned by player
      //TODO: Only allow dragging on the players turn
      ix = sprite.x
      iy = sprite.y
      // store a reference to the data
      // the reason for this is because of multitouch
      // we want to track the movement of this particular touch
      data = event.data
      dragging = true
    }

    val onDragEnd = () => {
      if(dragging) {
        var finalPosition = data.getLocalPosition(Display.stage)
        // Check if final position is valid
        val deltaX = ix - finalPosition.x
        val deltaY = iy - finalPosition.y
        if (math.abs(deltaX) < BoardInfo.dx * 2.5 && math.abs(deltaY) < BoardInfo.dy * 2.5) {
          dom.console.log("Valid move!")
        } else {
          dom.console.log("Invalid move!")
          sprite.x = ix
          sprite.y = iy
        }

        dragging = false
        // set the interaction data to null
        data = null
      }
    }

    val onDragMove = () => {
      if (dragging) {
        var newPosition = data.getLocalPosition(Display.stage)
        sprite.x = newPosition.x
        sprite.y = newPosition.y
      }
    }

    sprite.on("pointerdown", onDragStart)
    .on("pointerup", onDragEnd)
    //.on("pointerupoutside", onDragEnd)
    .on("pointermove", onDragMove)

    (newPiece, sprite)
  }
}