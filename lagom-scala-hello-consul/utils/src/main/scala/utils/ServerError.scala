package utils

case class ServerError(msg: String) extends Exception(msg)
