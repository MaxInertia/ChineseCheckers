package main.ui

import com.outr.pixijs.PIXI.{Sprite, SystemRenderer, Texture}
import com.outr.pixijs.{PIXI, RendererOptions}
import main.logic.{Board, Game, Piece}
import org.scalajs.dom
import org.scalajs.dom.{document, window}

/**
  * Created by Dorian Thiessen on 2017-12-15.
  */
object Display {

  // Resource constants
  val R: String = "images/"
  val Colors: Array[String] = Array( //TODO: Use enum?
    "blue",
    "green",
    "yellow",
    "red",
    "purple",
    "black")
  val Size = "32.png"

  // View-specific details of the displayed board.
  object BoardInfo {
    private val width = 2000
    private val height = 2294
    val scale = 0.25
    def Width: Double = width * scale
    def Height: Double = height * scale
    def dx: Double = 72.8 * scale
    def dy: Double = 124.8 * scale
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
    dom.console.log("Making all the "+ color +" pieces!")
    val texture = PIXI.Texture.fromImage(R + color + Size)
    var pieces: Array[Piece] = Array()

    for (i <- 0 until 10) {
      var xBoard = 0 // Internal x-coord of board position
      var yBoard = 0 // Internal y-coord of board position
      color match {
        case "black" => // Top
          xBoard = Board.tbPositions(i)._1
          yBoard = Board.tbPositions(i)._2
        case "green" => // Bottom
          xBoard = Board.tbPositions(i)._1
          yBoard = -Board.tbPositions(i)._2
        case "blue" => // Top-Left
          xBoard = -Board.lrPositions(i)._2
          yBoard = -Board.lrPositions(i)._1
        case "yellow" => // Top-Left
          xBoard = -Board.lrPositions(i)._2
          yBoard = Board.lrPositions(i)._1
        case "purple" => // Top-Left
          xBoard = Board.lrPositions(i)._2
          yBoard = Board.lrPositions(i)._1
        case "red" => // Top-Left
          xBoard = Board.lrPositions(i)._2
          yBoard = -Board.lrPositions(i)._1
        case default =>
          dom.console.log("Not implemented: Color: "+color)
      }

      // A little awkward how board pieces (not only the sprites) are created
      // in the ui package. However, this prevents the sprite of a Piece from
      // being changed at a later time. Possibly desirable?
      val (newPiece, sprite) = Piece.create(texture, color, xBoard, yBoard)
      pieces = pieces ++: Array(newPiece)
      stage.addChild(sprite)
    }

    game.pieces.+(color -> pieces)
  }


  def move(sprite: PIXI.Sprite, direction: Int, distance: Int): Unit = {
    if(distance == 1 || distance == 2)
      dom.console.log(s"WHOOPS! Attempting move with invalid distance: $distance")
    if(direction < 0 || direction > 5)
      dom.console.log(s"WHOOPS! Attempting move with invalid direction: $direction")
    direction match {
      case 0 => // Right (+, 0)
        sprite.x += BoardInfo.dx * 2 * distance

      case 1 => // Down-Right (+, +)
        sprite.x += BoardInfo.dx * distance
        sprite.y += BoardInfo.dy * distance

      case 2 => // Down-Left (-, +)
        sprite.x -= BoardInfo.dx * distance
        sprite.y += BoardInfo.dy * distance

      case 3 => // Left  (-, 0)
        sprite.x -= BoardInfo.dx * 2 * distance

      case 4 => // Up-Left (-, -)
        sprite.x -= BoardInfo.dx * distance
        sprite.y -= BoardInfo.dy * distance

      case 5 => // Up-Right (+, -)
        sprite.x += BoardInfo.dx * distance
        sprite.y -= BoardInfo.dy * distance
    }
  }
}
