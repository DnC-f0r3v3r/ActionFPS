package com.actionfps.players

import java.time.ZonedDateTime

import scala.collection.immutable.ListMap

/**
  * Created by me on 26/05/2016.
  */
object PlayerGameCounts {
  def empty: PlayerGameCounts = PlayerGameCounts(games = List.empty, counts = ListMap.empty)
}

case class PlayerGameCounts(games: List[ZonedDateTime], counts: ListMap[ZonedDateTime, Int]) {
  def include(zonedDateTime: ZonedDateTime): PlayerGameCounts = {
    val dateKey = zonedDateTime.withHour(0).withMinute(0).withSecond(0).withNano(0)
    copy(
      games = games :+ zonedDateTime,
      counts = counts.updated(dateKey, counts.getOrElse(dateKey, 0) + 1)
    )
  }
}
