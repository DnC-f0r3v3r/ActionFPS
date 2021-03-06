package views.ladder

import java.time.format.DateTimeFormatter

import com.actionfps.ladder.parser.Aggregate
import org.jsoup.Jsoup
import play.twirl.api.Html

/**
  * Created by me on 17/12/2016.
  */
object Table {
  def render(aggregate: com.actionfps.ladder.parser.Aggregate)(showTime: Boolean = false): Html = {
    val doc = Jsoup.parse(lib.Soup.wwwLocation.resolve("ladder_table.html").toFile, "UTF-8")
    val tr = doc.select("tbody > tr")
    aggregate.ranked.map { case Aggregate.RankedStat(id, rankM1, us) =>
      val target = tr.first().clone()
      target.select(".rank").first().text(s"${rankM1}")
      target.select(".user a").attr("href", s"/player/?id=$id").first().text(id)
      target.select(".points").first().text(s"${us.points}")
      target.select(".flags").first().text(s"${us.flags}")
      target.select(".frags").first().text(s"${us.frags}")
      target.select(".gibs").first().text(s"${us.gibs}")
      target.select(".time-played").first().text(us.timePlayedText)

      target.select(".last-seen time").attr("datetime", us.lastSeenText).first().text(us.lastSeenText)
      target
    }.foreach(doc.select("tbody").first().appendChild)

    tr.remove()

    if (!showTime) {
      // this is nice, no stupid conditional statements!
      doc.select(".rank, .time-played").remove()
    }
    Html(doc.body().html())
  }
}
