package main.ui

import com.outr.pixijs.{PIXI, RendererOptions}
import main.logic.Game
import org.scalajs.dom

/**
  * Created by Dorian Thiessen on 2017-12-15.
  */
object Display {

  // Resource constants
  val R: String = "images/"
  val Colors: Array[String] = Array(
    "blue",
    "green",
    "yellow",
    "red",
    "purple",
    "black")
  val Size = "32.png"

  // View-specific details of the displayed board.
  object Dimensions {
    val iwidth = 2000 // Image width
    val iheight = 2294 // Image height
    var scale = 1.00
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

  def init(game: Game): Unit = {
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

    // Resize stage & contents when window changes size
    /*dom.window.onresize = (event: UIEvent) =>{
      // Update Dimensions scale
      Dimensions.scale = 0.9 * dom.window.innerHeight / Dimensions.iheight
      if(0.9 * dom.window.innerWidth / Dimensions.iwidth < Dimensions.scale) {
        Dimensions.scale = dom.window.innerWidth / Dimensions.iwidth
      }
      dom.console.log(s"Changed scale to ${Dimensions.scale}")
      // Update renderer size
      renderer.resize(Dimensions.Width.toInt, Dimensions.Height.toInt)
      // Update board size and position
      val oldWidth = Dimensions.Width
      val oldHeight = Dimensions.Height
      board.scale.x = Dimensions.scale
      board.scale.y = Dimensions.scale
      board.x = Dimensions.Width/2
      board.y = Dimensions.Height/2
      // Update piece positions
      val pieces = game.getAllPieces
      for(p <- pieces) {
        val s = Sprites.get(p.ID)
        val tmp = Position.of(p.X, p.Y)
        s.x = tmp._1
        s.y = tmp._2
      }
    }*/

    // 3. Pieces
    //createPieceSprites(game)

    // 4. Start animation loop
    def animate(): Unit = {
      dom.window.requestAnimationFrame((_: Double) => animate())
      renderer.render(stage)
    }
    animate()
  }

  def createPieceSprites(game: Game): Unit = {
    val textures: Map[String, PIXI.Texture] = Map(
      "purple" -> PIXI.Texture.fromImage(R + "purple" + Size),
      "blue" -> PIXI.Texture.fromImage(R + "blue" + Size),
      "red" -> PIXI.Texture.fromImage(R + "red" + Size),
      "green" -> PIXI.Texture.fromImage(R + "green" + Size),
      "yellow" -> PIXI.Texture.fromImage(R + "yellow" + Size),
      "black" -> PIXI.Texture.fromImage(R + "black" + Size),
    )

    val pieces = game.getAllPieces
    for(p <- pieces) {
      val (xDisplayPos, yDisplayPos) = Position.of(p.X, p.Y)

      // Create sprite
      val sprite = new PIXI.Sprite(textures(p.Color)) {
        anchor.x = 0.5
        anchor.y = 0.5
        position.x = xDisplayPos
        position.y = yDisplayPos
      }

      Events.setupSpriteListeners(sprite, p.ID)
      stage.addChild(sprite)
      Sprites.add(sprite)
    }
  }

}
