package main.logic.players

abstract class Player(name: String, color: String) {
  def Name: String = name
  def Color: String = color
  def notifyOfTurn(): Unit
}
