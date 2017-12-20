package main.ui

import com.outr.pixijs.PIXI.{Sprite, SystemRenderer, Texture}
import com.outr.pixijs.{PIXI, RendererOptions}
import main.logic.{Game, Piece}
import org.scalajs.dom
import org.scalajs.dom.raw.UIEvent

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
    var sprites: Array[Sprite] = Array()

    def add(newSprite: Sprite): Unit =
      sprites = sprites :+ newSprite

    def get(i: Int): Sprite =
      if(i >= sprites.length) null else sprites(i)

    def changeVisibility(i: Int): Boolean = {
      require (i < sprites.length)
      sprites(i).visible = !sprites(i).visible
      sprites(i).visible
    }
  }

  // PIXI Stuff
  var renderer: SystemRenderer = _
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

    // 2. Board
    val board = new PIXI.Sprite(PIXI.Texture.fromImage(R + "board.png")) {
      anchor.x = 0.5
      anchor.y = 0.5
      scale.x = Dimensions.scale
      scale.y = Dimensions.scale
      x = Dimensions.Width/2
      y = Dimensions.Height/2
    }
    dom.console.log(s"Board Scale: ${Dimensions.scale}")
    board.rotation = math.Pi/3
    stage.addChild(board)

    // Resize stage & contents when window changes size
    dom.window.onresize = (event: UIEvent) =>{
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
      for(i <- 0 to Sprites.sprites.length) {
        val (p, found) = Game.Current.board.getPiece(i) // Too many dots?!
        if(found) {
          val s = Sprites.get(i)
          val tmp = Position.of(p.Pos.X, p.Pos.Y)
          s.x = tmp._1
          s.y = tmp._2
        }
      }
    }

    // 3. Pieces
    makePlayerPieces("purple", game)
    makePlayerPieces("blue", game)
    makePlayerPieces("red", game)
    makePlayerPieces("green", game)
    makePlayerPieces("yellow", game)
    makePlayerPieces("black", game)

    // 4. Start animation loop
    def animate(): Unit = {
      dom.window.requestAnimationFrame((_: Double) => animate())
      renderer.render(stage)
    }
    animate()
  }

  def makePlayerPieces(color: String, game: Game): Unit = {
    //dom.console.log("Making all the "+ color +" pieces!")
    val texture = PIXI.Texture.fromImage(R + color + Size)

    for(i <- 0 to 9) {
      val (id, xPos, yPos) = Piece.create(color)

      // Calculate coordinates for sprites position on display
      val xDisplayPos = Dimensions.Width / 2 + Dimensions.dx * xPos
      val yDisplayPos = Dimensions.Height / 2 + Dimensions.dy * yPos

      //dom.console.log(s"Adding Sprite to ($xDisplayPos, $yDisplayPos)")

      // Create sprite
      val sprite = new PIXI.Sprite(texture) {
        anchor.x = 0.5
        anchor.y = 0.5
        position.x = xDisplayPos
        position.y = yDisplayPos
      }
      sprite.interactive = true
      sprite.buttonMode = true
      sprite.visible = false
      Events.setupListeners(sprite, id)
      stage.addChild(sprite)
      Sprites.add(sprite)
    }
  }

  // Only required for bots
  def move(sprite: PIXI.Sprite, direction: Int, distance: Int): Unit = {
    if(distance != 1 && distance != 2)
      dom.console.log(s"WHOOPS! Attempting move with invalid distance: $distance")
    if(direction < 0 || direction > 5)
      dom.console.log(s"WHOOPS! Attempting move with invalid direction: $direction")
    direction match {
      case 0 => // Right (+, 0)
        sprite.x += Dimensions.dx * 2 * distance

      case 1 => // Down-Right (+, +)
        sprite.x += Dimensions.dx * distance
        sprite.y += Dimensions.dy * distance

      case 2 => // Down-Left (-, +)
        sprite.x -= Dimensions.dx * distance
        sprite.y += Dimensions.dy * distance

      case 3 => // Left  (-, 0)
        sprite.x -= Dimensions.dx * 2 * distance

      case 4 => // Up-Left (-, -)
        sprite.x -= Dimensions.dx * distance
        sprite.y -= Dimensions.dy * distance

      case 5 => // Up-Right (+, -)
        sprite.x += Dimensions.dx * distance
        sprite.y -= Dimensions.dy * distance
    }
  }

}
