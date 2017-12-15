package main.ui

import com.outr.pixijs.PIXI.{Sprite, SystemRenderer, Texture}
import com.outr.pixijs.{PIXI, RendererOptions}
import main.logic.Game
import org.scalajs.dom
import org.scalajs.dom.{document, window}

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
  object Board {
    private val width = 2000
    private val height = 2294
    var scale = 0.25
    def Width: Double = width * scale
    def Height: Double = height * scale
    def dx = 72.8 * scale
    def dy = 124.8 * scale
  }
  // PIXI
  var renderer: SystemRenderer = _
  var stage: PIXI.Container = _

  def init(game: Game): Unit = {
    // 1. Renderer and Stage
    renderer = PIXI.autoDetectRenderer(new RendererOptions {
      height = 573
      width = 500
      antialias = true
      resolution = 1.0
      backgroundColor = 0xffffff
    })
    document.body.appendChild(renderer.view)
    stage = new PIXI.Container()


    // 2. Board
    val board = new PIXI.Sprite(PIXI.Texture.fromImage(R + "board.png"))
    board.scale.set(0.25, 0.25)
    stage.addChild(board)

    // 3. Pieces
    var pc = 32
    for(color <- Colors) {
      val texture = PIXI.Texture.fromImage(R + color + Size)
      var pieces: Array[Sprite] = Array()
      for (i <- 1 to 6) {
        var piece = makePiece(texture, (i + 1) * 34 + 150, pc)
        pieces = pieces ++: Array(piece)
        stage.addChild(piece)
      }
      game.pieces.+(color -> pieces)
      pc += 34
    }

    // 4. Start animation loop
    def animate(): Unit = {
      window.requestAnimationFrame((_: Double) => animate())
      renderer.render(stage)
    }
    animate()
  }

  def makePiece(texture: Texture, xPos: Int, yPos: Int): PIXI.Sprite = {
    val sprite = new PIXI.Sprite(texture) {
      anchor.x = 0.5
      anchor.y = 0.5
      position.x = xPos
      position.y = yPos
    }

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
      sprite.scale.x = 1.4
      sprite.scale.y = 1.4
    }
    // Action performed when mouse up over sprite
    val onUp = () => {
      sprite.scale.x = 1.1
      sprite.scale.y = 1.1
    }

    sprite.interactive = true
    sprite.on("mouseover", onOver)
    sprite.on("mouseout", onOut)
    sprite.on("mousedown", onDown)
    sprite.on("mouseup", onUp)
    sprite
  }

  def moveTest(): Unit = {
    val color = "black"
    val texture = PIXI.Texture.fromImage(R + color + Size)
    val ref1 = new PIXI.Sprite(texture) {
      anchor.x = 0.5
      anchor.y = 0.5
      scale.x = 0.9
      scale.y = 0.9
    }

    ref1.interactive = true
    ref1.position.set(Board.Width/2, Board.Height/2)//628)

    val dx = 18.2
    val dy = 31.2

    ref1.on("mousedown", () => {
      dom.console.log("on ref movement dx: "+dx+", dy: "+dy)
      ref1.x += Board.dx
      ref1.y += Board.dy
    })

    stage.addChild(ref1)
  }
}
