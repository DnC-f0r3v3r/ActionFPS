package controllers

import java.time.ZonedDateTime
import javax.inject.Inject

import com.actionfps.accumulation.{Clan, CurrentNickname, FullProfile, User}
import com.actionfps.api.{Game, GameAchievement, GamePlayer, GameTeam}
import com.actionfps.clans.CompleteClanwar
import com.actionfps.clans.Conclusion.Namer
import com.actionfps.gameparser.Maps
import com.actionfps.ladder.parser.Aggregate.RankedStat
import com.actionfps.ladder.parser.UserStatistics
import com.actionfps.pinger._
import lib.Clanner
import play.api.mvc.Action
import play.api.mvc.Results._
import play.api.routing.Router.Routes
import play.api.routing.SimpleRouter
import play.api.routing.sird._
import play.twirl.api.Html

/**
  * Created by me on 11/12/2016.
  */

class Dev @Inject()(common: Common) extends SimpleRouter {

  private implicit class RichHtml(html: Html) {
    def transform(f: String => String): Html = {
      Html(f(html.body))
    }
  }

  override def routes: Routes = {
    case GET(p"/live-template/") => liveTemplate
    case GET(p"/clanwars/") => clanwarTemplate
    case GET(p"/sig.svg") => Action { request =>
      views.player.Signature(interrank = Some(1),
        playername = "w00p|Drakas",
        ladderrank = Some(2),
        gamecount = Some(4),
        map = request.getQueryString("map")
      ).result
    }
    case GET(p"/sig/") => Action {
      Ok(Html("""<img src="../sig.svg">"""))
    }
    case GET(p"/player/") => Action { implicit req =>
      Ok(common.renderTemplate(None, supportsJson = true, None)(views.player.Player.render(
        Dev.fullProfile,
        Some(Dev.rankedStat)
      )
        .transform(_.replaceAllLiterally("/player/signature.svg?id=boo", "/dev/sig.svg?map=ac_depot"))
      ))
    }
  }

  private def clanwarTemplate = Action { implicit req =>
    implicit val namer = Dev.namer
    implicit val clanner = Dev.clanner
    val mapping = Maps.mapToImage
    val html = views.clanwar.Clanwar.render(
      clanwar = Dev.completeClanwar.meta.named,
      showPlayers = true
    )
    val fh = Html(html.body + "<hr/>")
    Ok(common.renderTemplate(None, supportsJson = false, None)(fh))
  }

  private def liveTemplate = Action { implicit req =>
    val mapping = Maps.mapToImage
    val html = views.rendergame.Live.render(mapMapping = mapping, game = Dev.game)
    val fh = Html(html.body + "<hr/>")
    Ok(common.renderTemplate(None, supportsJson = false, None)(fh))
  }
}

object Dev {
  val rankedStat = RankedStat(user = "w00p|User", rank = 23,
    userStatistics = UserStatistics(frags = 12, gibs = 13, flags = 14,
      lastSeen = ZonedDateTime.now(), timePlayed = 123L))

  val fullProfile = FullProfile(
    recentGames = Nil,
    achievements = None,
    rank = None,
    playerGameCounts = None,
    user = User(
      id = "boo",
      name = "Boo",
      countryCode = None,
      email = None,
      previousNicknames = None,
      registrationDate = ZonedDateTime.now().minusDays(5),
      nickname = CurrentNickname(nickname = "w00p|Boo", countryCode = None, from = ZonedDateTime.now().minusDays(2))
    )
  )

  val gamePlayer = GamePlayer(name = "Newbie", host = None, score = None, flags = Some(2), frags = 54, deaths = 12,
    user = Some("newbie"), clan = Some("woop"), countryCode = None, countryName = None, timezone = None)
  val gameTeam = GameTeam(name = "RVSF", flags = Some(3), frags = 99, clan = Some("woop"),
    players = List(gamePlayer))
  val otherTeam: GameTeam = gameTeam.copy(name = "CLA", clan = Some("bleh"), flags = Some(3),
    players = List(
      gamePlayer.copy(name = "Bewbie", user = Some("bewbie"), clan = Some("bleh")),
      gamePlayer.copy(frags = 51, flags = Some(3), name = "Zewbie", user = Some("xewbie"), clan = Some("bleh"))

    ))
  val completedGame = Game(
    id = "2015-01-01T03:04:05Z", endTime = ZonedDateTime.now(), map = "ac_depot", mode = "ctf", state = "WHAT",
    server = "aura.woop.ac:1999", duration = 15, clangame = Some(Set("woop", "bleh")), clanwar = Some("id"),
    achievements = Some(List(GameAchievement("newbie", "won it all"))),
    teams = List(gameTeam, otherTeam)
  )
  val game = CurrentGameStatus(
    when = "now",
    reasonablyActive = true,
    hasFlags = true,
    map = Some("ac_shine"),
    mode = Some("ctf"),
    minRemain = 5,
    now = CurrentGameNow(
      CurrentGameNowServer(
        server = "aura.woop.us:1234",
        connectName = "aura.woop.us 1239",
        shortName = "aura 123",
        description = "blah bang"
      )
    ),
    updatedTime = "abc",
    players = Some(List("John", "Peter")),
    spectators = Some(List("Smith", "Dave")),
    teams = List(
      CurrentGameTeam(
        name = "rvsf", flags = Some(12), frags = 123,
        spectators = Some(List(CurrentGamePlayer(name = "Speccy", flags = Some(10), frags = 100))),
        players = List(CurrentGamePlayer(name = "peepe", flags = Some(2), frags = 23))
      ),
      CurrentGameTeam(
        name = "cla", flags = Some(13), frags = 114,
        spectators = Some(List(CurrentGamePlayer(name = "Ceppy", flags = None, frags = 99))),
        players = List(CurrentGamePlayer(name = "prepe", flags = None, frags = 29))
      )
    )
  )

  val completeClanwar = CompleteClanwar(
    winner = Some("woop"),
    clans = Set("woop", "bleh"),
    scores = Map("woop" -> 2, "bleh" -> 1),
    games = List(Dev.completedGame, Dev.completedGame)
  )
  implicit val namer = Namer(Map("newbie" -> "w00p|Newbie").get)
  val woopCln = Clan(id = "woop", name = "w00p", fullName = "Woop Clan",
    tag = None, tags = None, website = None, teamspeak = None,
    logo = "https://cloud.githubusercontent.com/assets/2464813/12814066/25c656a4-cb34-11e5-87a7-dbff30d759c6.png")

  implicit val clanner = Clanner(
    Map("woop" -> woopCln,
      "bleh" -> woopCln.copy(id = "bleh", name = "BLEH", logo = "https://cloud.githubusercontent.com/assets/5359646/12004841/d10c564a-ab7b-11e5-8d41-00e673cc0096.png")).get
  )
}
