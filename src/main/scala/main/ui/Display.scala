package main.ui

import com.outr.pixijs.{PIXI, RendererOptions}
import main.logic.ChineseCheckers
import main.logic.board.Colors._
import org.scalajs.dom

/**
  * Created by Dorian Thiessen on 2017-12-15.
  */
object Display {
  // View-specific details of the displayed board.
  object Dimensions {
    val iwidth: Int = 2000 // Image width
    val iheight: Int = 2294 // Image height
    var scale: Double = 1.00
    def Width: Double = iwidth * scale
    def Height: Double = iheight * scale
    // Horizontal distance between neighboring board positions
    def dx: Double = 72.8 * scale
    // Vertical distance between neighboring board positions
    def dy: Double = 124.8 * scale
  }

  object Sprites {
    var sprites: Array[PIXI.Sprite] = Array()

    def add(newSprite: PIXI.Sprite): Unit =
      sprites = sprites :+ newSprite

    def get(i: Int): PIXI.Sprite =
      if(i >= sprites.length) null else sprites(i)

    /*def changeVisibility(i: Int): Boolean = {
      require (i < sprites.length)
      sprites(i).visible = !sprites(i).visible
      sprites(i).visible
    }*/

    // Moves a sprite
    def move(pid: Int, x: Int, y: Int): Unit = {
      val (displayX, displayY) = Position.of(x, y)
      Sprites.get(pid).position.set(displayX, displayY)
    }
  }

  var board: Board = _

  // PIXI Stuff
  var renderer: PIXI.SystemRenderer = _
  var stage: PIXI.Container = _

  def init(): Unit = {
    // 90% of this ratio seems to fit nicely in the window, 100% forces vertical scrolling
    Dimensions.scale = 0.9 * dom.window.innerHeight / Dimensions.iheight

    // 1. Renderer and Stage
    renderer = PIXI.autoDetectRenderer(new RendererOptions {
      height = Dimensions.Height.toInt
      width = Dimensions.Width.toInt
      antialias = true
      resolution = 1.0
      autoResize = true
      backgroundColor = 0xffffff
    })
    dom.document.getElementById("boardContainer").appendChild(renderer.view)
    stage = new PIXI.Container()

    // 2. Create board
    board = new Board(Dimensions.Width/2, Dimensions.Height/2)
    board.drawBoard()

    // 3. Start animation loop
    def animate(): Unit = {
      dom.window.requestAnimationFrame((_: Double) => animate())
      renderer.render(stage)
    }
    animate()
  }

  def createPieceSprites(): Unit = {
    val textures: Map[Color, PIXI.Texture] = Map(
      Purple -> PIXI.Texture.fromImage(Purple.imageFile),
      Blue -> PIXI.Texture.fromImage(Blue.imageFile),
      Red -> PIXI.Texture.fromImage(Red.imageFile),
      Green -> PIXI.Texture.fromImage(Green.imageFile),
      Yellow -> PIXI.Texture.fromImage(Yellow.imageFile),
      Black -> PIXI.Texture.fromImage(Black.imageFile)
    )

    val pieces = ChineseCheckers.getAllPieces
    for(p <- pieces) {
      val (xDisplayPos, yDisplayPos) = Position.of(p.X, p.Y)

      // Create sprite
      val sprite = new PIXI.Sprite(textures(p.color)) {
        anchor.x = 0.5
        anchor.y = 0.5
        position.x = xDisplayPos
        position.y = yDisplayPos
      }

      Events.setupSpriteListeners(sprite, p.id)
      stage.addChild(sprite)
      Sprites.add(sprite)
    }
  }

}
