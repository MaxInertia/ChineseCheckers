package main.logic.board

/**
  * Created by Dorian Thiessen on 2018-04-21.
  */
object Colors {
  private val imgSuffix = "32.png"
  private val imgPrefix = "images/"
  sealed trait Color {
    override def toString: String = this match {
      case Blue => "blue"
      case Green => "green"
      case Red => "red"
      case Purple => "purple"
      case Black => "black"
      case Yellow => "yellow"
    }

    def imageFile: String = imgPrefix + toString + imgSuffix
  }

  sealed trait Blue extends Color
  sealed trait Green extends Color
  sealed trait Red extends Color
  sealed trait Purple extends Color
  sealed trait Black extends Color
  sealed trait Yellow extends Color

  case object Blue extends Blue
  case object Green extends Green
  case object Red extends Red
  case object Purple extends Purple
  case object Black extends Black
  case object Yellow extends Yellow
}