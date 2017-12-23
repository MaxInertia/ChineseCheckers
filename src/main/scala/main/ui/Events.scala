package main.ui

import com.outr.pixijs.PIXI
import main.logic.Game

object Events {
  val defaultTileOpacity = 1
  val selectedTileOpacity = 0.3

  object SelectedPiece {
    var sprite: PIXI.Sprite = _
    var id: Int = -1
    var possibleMoves: Array[(Int, Int)] = _

    // Deselects the currently selected piece.
    // All highlighted tiles are restored to their defaults.
    def clear(): Unit = {
      if(SelectedPiece.possibleMoves != null) {
        for (pm <- SelectedPiece.possibleMoves) {
          val hex = Display.board.tiles(pm)
          hex.Tile.buttonMode = false
          hex.Tile.interactive = false
          hex.Tile.alpha = defaultTileOpacity
        }
      }

      SelectedPiece.possibleMoves = null
      SelectedPiece.sprite = null
      SelectedPiece.id = -1
    }

    def exists(): Boolean = sprite != null && id != -1
  }

  def setupSpriteListeners(sprite: PIXI.Sprite, id: Int): Unit = {
    sprite.interactive = true
    sprite.buttonMode = true
    sprite.visible = true

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
    val onClick = () => {
      // Clear the previous selection

      if(SelectedPiece.sprite == sprite) {
        SelectedPiece.clear()
      } else {
        SelectedPiece.clear()
        // Select this piece and display moves
        SelectedPiece.sprite = sprite
        SelectedPiece.id = id
        SelectedPiece.possibleMoves = Game.Current.requestPossibleMoves(id)
        if (SelectedPiece.possibleMoves != null) {
          for (pm <- SelectedPiece.possibleMoves) {
            //dom.console.log(s"\t(${pm._1}, ${pm._2})")
            val hex = Display.board.tiles(pm)
            hex.Tile.buttonMode = true
            hex.Tile.interactive = true
            hex.Tile.alpha = selectedTileOpacity
          }
        }
      }
    }

    sprite.on("mouseover", onOver)
    sprite.on("mouseout", onOut)
    sprite.on("click", onClick)
  }

  def setupTileListeners(graphics: PIXI.Graphics, x: Double, y: Double, xM: Int, yM :Int): Unit = {
    graphics.interactive = false
    graphics.buttonMode = false
    graphics.alpha = 1

    val mouseOver = (event: PIXI.interaction.InteractionEvent) =>
      event.currentTarget.alpha = selectedTileOpacity

    val mouseOut = (event: PIXI.interaction.InteractionEvent) =>
      event.currentTarget.alpha = defaultTileOpacity

    val onClick = () => {
      if(SelectedPiece.exists()) {
        SelectedPiece.sprite.x = x
        SelectedPiece.sprite.y = y

        val (piece, found) = Game.Current.board.getPiece(SelectedPiece.id)
        if(found) {
          val moveOccurred = Game.Current.requestMove(piece.X, piece.Y, xM, yM)
        }

        SelectedPiece.clear()
      }
    }

    //graphics.on("mouseover", mouseOver)
    //graphics.on("mouseout", mouseOut)
    graphics.on("click", onClick)
  }
}
