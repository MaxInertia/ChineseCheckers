package main.ui

import com.outr.pixijs.PIXI
import main.logic.Game
import main.ui.Display.Dimensions
import org.scalajs.dom

object Events {
  def setupListeners(sprite: PIXI.Sprite, id: Int): Unit = {
    // Action performed when mouse over sprite
    val onOver = () => {
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
      data = event.data
      dragging = true
    }

    val onDragEnd = () => {
      if(dragging) {
        var finalPosition = data.getLocalPosition(Display.stage)
        // Check if final position is valid.
        val (position, ok) = moveAppearsValid(ix, iy, finalPosition.x, finalPosition.y)
        if(ok) {
          // Move piece to new tile
          val (piece, foundPiece) = Game.Current.board.getPiece(id)
          var moveOk = false
          if(foundPiece) moveOk = piece.setPosition(position.boardX, position.boardY)
          if(moveOk) {
            sprite.position.set(position.X, position.Y)
          } else {
            // Restore pre-drag position
            dom.console.log("Invalid move attempted!")
            sprite.x = ix
            sprite.y = iy
          }
        } else {
          // Restore pre-drag position
          dom.console.log("Invalid move attempted!")
          sprite.x = ix
          sprite.y = iy
        }

        dragging = false
        data = null // set the interaction data to null
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
      .on("pointermove", onDragMove)
      .on("pointermoveout", onDragEnd)
  }

  // Checks if the player dragged the piece to a valid board location.
  // Return: If the move is not valid (null, false) is returned.
  // If valid, (display.Position, true) is returned.
  //    position returned is the final position, centered on the tile moved to
  def moveAppearsValid(ix: Double, iy: Double, fx: Double, fy: Double): (Position, Boolean) = {
    val deltaX = ix - fx
    val deltaY = iy - fy

    // Doesn't check move against other piece locations
    // TODO: Confirm pieces not blocking and that pieces exist in jump moves

    if (math.abs(deltaX) > Dimensions.dx * 2 * 2.5 || math.abs(deltaY) > Dimensions.dy * 2.5)
      return (null, false) // Attempted to move distance > 2

    val position = new Position()
    position.set(fx, fy)
    position.centerOnTile()
    if (math.abs(ix - position.X) < 0.9 * Display.Dimensions.dx)
      return (null, false) // Attempted to move directly up or down

    val xyRatio = math.abs(iy - position.Y) / math.abs(ix - position.X)
    if (xyRatio > 0 && xyRatio < 1.5)
      return (null, false) // Attempted to move in 'L'

    (position, true) // Looks good!
  }
}
