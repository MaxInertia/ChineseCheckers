package main.ui

import com.outr.pixijs.PIXI.{Sprite, SystemRenderer, Texture}
import com.outr.pixijs.{PIXI, RendererOptions}
import main.logic.{Board, Game, Piece}
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
  object BoardInfo {
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

  // PIXI
  var renderer: SystemRenderer = _
  var stage: PIXI.Container = _

  def init(game: Game): Unit = {
    // 90% of this ratio seems to fit nicely in the window, 100% forces vertical scrolling
    BoardInfo.scale = 0.9 * dom.window.innerHeight / BoardInfo.iheight

    // 1. Renderer and Stage
    renderer = PIXI.autoDetectRenderer(new RendererOptions {
      height = BoardInfo.Height.toInt
      width = BoardInfo.Width.toInt
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
      scale.x = BoardInfo.scale
      scale.y = BoardInfo.scale
      x = BoardInfo.Width/2
      y = BoardInfo.Height/2
    }
    dom.console.log(s"Board Scale: ${BoardInfo.scale}")
    board.rotation = math.Pi/3
    stage.addChild(board)

    // Resize stage & contents when window changes size
    dom.window.onresize = (event: UIEvent) =>{
      // Update BoardInfo scale
      BoardInfo.scale = 0.9 * dom.window.innerHeight / BoardInfo.iheight
      if(0.9 * dom.window.innerWidth / BoardInfo.iwidth < BoardInfo.scale) {
        BoardInfo.scale = dom.window.innerWidth / BoardInfo.iwidth
      }
      dom.console.log(s"Changed scale to ${BoardInfo.scale}")
      // Update renderer size
      renderer.resize(BoardInfo.Width.toInt, BoardInfo.Height.toInt)
      // Update board size and position
      val oldWidth = BoardInfo.Width
      val oldHeight = BoardInfo.Height
      board.scale.x = BoardInfo.scale
      board.scale.y = BoardInfo.scale
      board.x = BoardInfo.Width/2
      board.y = BoardInfo.Height/2
      // Update piece positions
      for(p <- Game.Current.pc) {
        p.Sprite.x = BoardInfo.Width/2 + p.Pos.X * BoardInfo.dx
        p.Sprite.y = BoardInfo.Height/2 + p.Pos.Y * BoardInfo.dy
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
    dom.console.log("Making all the "+ color +" pieces!")
    val texture = PIXI.Texture.fromImage(R + color + Size)

    for (i <- 0 until 10) {
      var xBoard = 0 // Internal x-coord of board position
      var yBoard = 0 // Internal y-coord of board position
      color match {
        case "purple" => // Top
          xBoard = Board.tbPositions(i)._1
          yBoard = Board.tbPositions(i)._2
        case "blue" => // Bottom
          xBoard = Board.tbPositions(i)._1
          yBoard = -Board.tbPositions(i)._2
        case "yellow" => // Top-Left
          xBoard = -Board.lrPositions(i)._2
          yBoard = -Board.lrPositions(i)._1
        case "black" => // Top-Left
          xBoard = -Board.lrPositions(i)._2
          yBoard = Board.lrPositions(i)._1
        case "red" => // Top-Left
          xBoard = Board.lrPositions(i)._2
          yBoard = Board.lrPositions(i)._1
        case "green" => // Top-Left
          xBoard = Board.lrPositions(i)._2
          yBoard = -Board.lrPositions(i)._1
        case default =>
          dom.console.log("Not implemented: Color: "+color)
      }

      // TODO: Separate ui & game logic (No PIXI in logic package)
      val (newPiece, sprite) = Piece.create(texture, color, xBoard, yBoard)
      Game.Current.pc = Game.Current.pc ++: Array(newPiece)
      stage.addChild(sprite)
    }

    //Game.Current.pieces.+(color -> pieces)
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

  def focusCoords(displayX: Double, displayY: Double): (Double, Double, Int, Int) = {
    // Distance from board center
    val x2c = displayX - BoardInfo.Width/2
    val y2c = displayY - BoardInfo.Height/2

    // Determine board position closest to given coordinates
    // y-coord
    var boardY = y2c/BoardInfo.dy
    if(math.abs(boardY - boardY.toInt) <= 0.5) {
      boardY = boardY.toInt
    } else if(math.abs(boardY - boardY.toInt) > 0.5) {
      if(boardY > 0) boardY = boardY.toInt + 1
      else boardY = boardY.toInt - 1
    }
    // x-coord
    var boardX = x2c/BoardInfo.dx
    if(math.abs(math.ceil(boardX))%2 == math.abs(boardY)%2) {
      boardX = math.ceil(boardX)
    } else {
      boardX = math.floor(boardX)
    }

    //dom.console.log(s"Attempting to move to ($boardX, $boardY)")
    val x = BoardInfo.Width/2 + boardX * BoardInfo.dx
    val y = BoardInfo.Height/2 + boardY * BoardInfo.dy
    (x, y, boardX.toInt, boardY.toInt)
  }

}
