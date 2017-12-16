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
    makePlayerPieces("red", game)
    makePlayerPieces("green", game)
    makePlayerPieces("blue", game)
    makePlayerPieces("yellow", game)
    makePlayerPieces("purple", game)
    makePlayerPieces("black", game)

    // 4. Start animation loop
    def animate(): Unit = {
      window.requestAnimationFrame((_: Double) => animate())
      renderer.render(stage)
    }
    animate()
  }

  def makePlayerPieces(color: String, game: Game): Unit = {
    val texture = PIXI.Texture.fromImage(R + color + Size)
    var pieces: Array[Sprite] = Array()
    // For position of top & bottom pieces
    val tbPositions = Array( // (dx, dy)
      (-3, 5), (-1, 5), (1, 5), (3, 5),
      (-2, 6), (0, 6), (2, 6),
      (1, 7), (-1, 7),
      (0, 8))
    // For position of pieces on {top | bottom} + {left | right}
    val lrPositons = Array( // (dy, dx)
      (4, 12), (4, 10), (4, 8), (4, 6),
      (3, 11), (3, 9), (3, 7),
      (2, 10), (2, 8),
      (1, 9))

    for (i <- 0 until 10) {
      var x = Board.Width/2
      var y = Board.Height/2
      color match {
        case "black" => // Top
          x += Board.dx * tbPositions(i)._1
          y += Board.dy * tbPositions(i)._2
        case "green" => // Bottom
          x += Board.dx * tbPositions(i)._1
          y -= Board.dy * tbPositions(i)._2
        case "blue" => // Top-Left
          x -= Board.dx * lrPositons(i)._2
          y -= Board.dy * lrPositons(i)._1
        case "yellow" => // Top-Left
          x -= Board.dx * lrPositons(i)._2
          y += Board.dy * lrPositons(i)._1
        case "purple" => // Top-Left
          x += Board.dx * lrPositons(i)._2
          y += Board.dy * lrPositons(i)._1
        case "red" => // Top-Left
          x += Board.dx * lrPositons(i)._2
          y -= Board.dy * lrPositons(i)._1
        case default => dom.console.log("Not implemented: Color: "+color)
      }

      var piece = makePiece(texture, x, y)
      pieces = pieces ++: Array(piece)
      stage.addChild(piece)
    }

    game.pieces.+(color -> pieces)
  }

  def makePiece(texture: Texture, xPos: Double, yPos: Double): PIXI.Sprite = {
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

  def move(sprite: PIXI.Sprite, direction: Int, distance: Int): Unit = {
    require(distance == 1 || distance == 2)
    direction match {
      case 0 => // Right (+, 0)
        sprite.x += Board.dx * 2 * distance

      case 1 => // Down-Right (+, +)
        sprite.x += Board.dx * distance
        sprite.y += Board.dy * distance

      case 2 => // Down-Left (-, +)
        sprite.x -= Board.dx * distance
        sprite.y += Board.dy * distance

      case 3 => // Left  (-, 0)
        sprite.x -= Board.dx * 2 * distance

      case 4 => // Up-Left (-, -)
        sprite.x -= Board.dx * distance
        sprite.y -= Board.dy * distance

      case 5 => // Up-Right (+, -)
        sprite.x += Board.dx * distance
        sprite.y -= Board.dy * distance
    }
  }
}
