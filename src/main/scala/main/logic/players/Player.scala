package main.logic.players

import main.logic.board.Colors.Color

abstract class Player(name: String, color: Color) {
  def Name: String = name
  def Color: Color = color
  def notifyOfTurn(): Unit
}
