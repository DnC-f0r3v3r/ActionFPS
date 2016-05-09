package controllers

/**
  * Created by me on 09/05/2016.
  */

import javax.inject._

import akka.agent.Agent
import com.actionfps.ladder.SshTailer
import com.actionfps.ladder.connecting.RemoteSshPath
import com.actionfps.ladder.parser.{Aggregate, LineParser, PlayerMessage, UserProvider}
import play.api.{Configuration, Logger}
import play.api.inject.ApplicationLifecycle
import play.api.mvc.{Action, Controller}
import play.twirl.api.Html
import providers.ReferenceProvider

import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class LadderController @Inject
()(applicationLifecycle: ApplicationLifecycle,
   common: Common,
   configuration: Configuration,
   referenceProvider: ReferenceProvider)
(implicit executionContext: ExecutionContext) extends Controller {

  import collection.JavaConverters._

  val tailers = configuration.getStringList("af.ladder.sources").get.asScala.map { sshUrl =>
    Logger.info(s"Logging from ${sshUrl}")
    val q = new SshTailer(
      endOnly = false,
      file = RemoteSshPath.unapply(sshUrl).get
    )(includeLine)
    q
  }

  val prs = LineParser(atYear = 2016)
  val agg = Agent(Aggregate.empty)

  import concurrent.duration._

  val usrs = Await.result(referenceProvider.users, 10.seconds)
  val nick2UserId = usrs.map { u => u.nickname.nickname -> u.id }.toMap
  val up = new UserProvider {
    override def username(nickname: String): Option[String] = {
      nick2UserId.get(nickname)
    }
  }

  def includeLine(input: String): Unit = input match {
    case prs(_, PlayerMessage(pm)) =>
      agg.send(_.includeLine(pm)(up))
    case _ =>
  }

  applicationLifecycle.addStopHook(() => Future.successful(tailers.foreach(_.shutdown())))

  def ladder = Action { implicit req =>
    val hx =
      <article id="profile">
        <div class="profile">
          <h1>Ladder</h1>
          <table>
            <thead>
              <tr>
                <th>User</th> <th>Points</th> <th>Flags</th> <th>Frags</th> <th>Gibs</th>
              </tr>
            </thead>
            <tbody>
              {val sorted = agg.get().users.toList.sortBy(_._2.points).reverse
            sorted.map {
              case (id, us) =>
                <tr>
                  <th>
                    <a href={s"/player/?id=$id"}>
                      {id}
                    </a>
                  </th>
                  <td>
                    {us.points}
                  </td>
                  <td>
                    {us.flags}
                  </td>
                  <td>
                    {us.frags}
                  </td>
                  <td>
                    {us.gibs}
                  </td>
                </tr>
            }}
            </tbody>
          </table>
        </div>
      </article>

    Ok(common.renderTemplate(
      title = Some("Ladder"),
      supportsJson = false,
      login = None)
    (Html(hx.toString())))
  }
}