package main.logic

/**
  * Created by Dorian Thiessen on 2018-04-21.
  */
object Colors {
  sealed trait Color {
    override def toString: String = this match {
      case Blue => "blue"
      case Green => "green"
      case Red => "red"
      case Purple => "purple"
      case Black => "black"
      case Yellow => "yellow"
    }
  }

  case object Blue extends Color
  case object Green extends Color
  case object Red extends Color
  case object Purple extends Color
  case object Black extends Color
  case object Yellow extends Color
}