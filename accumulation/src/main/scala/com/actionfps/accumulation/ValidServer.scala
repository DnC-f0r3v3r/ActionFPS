package com.actionfps.accumulation

import java.time.ZoneId

import com.actionfps.gameparser.enrichers.JsonGame

/**
  * Created by William on 26/12/2015.
  */

object ValidServers {

  private implicit def strToTz(string: String): ZoneId = ZoneId.of(string)
  private implicit def strToOstr(string: String): Option[String] = Option(string)

  val validServers = List(
    ValidServer(
      logId = "104.219.54.14 tyrwoopac AssaultCube[local#1999]",
      name = "tyr 1999",
      timezone = "US/Central"
    ),
    ValidServer(
      logId = "104.219.54.14 tyrwoopac AssaultCube[local#2999]",
      name = "tyr 2999",
      timezone = "US/Central"
    ),
    ValidServer(
      logId = "104.219.54.14 tyrwoopac AssaultCube[local#3999]",
      name = "tyr 3999",
      timezone = "US/Central"
    ),
    ValidServer(
      logId = "104.219.54.14 tyrwoopac AssaultCube[local#4999]",
      name = "tyr 4999",
      timezone = "US/Central"
    ),
    ValidServer(
      logId = "104.255.33.235 la AssaultCube[local#33333]",
      name = "la 33333",
      timezone = "America/New_York"
    ),
    ValidServer(
      logId = "104.255.33.235 la AssaultCube[local#1999]",
      name = "la 1999",
      timezone = "America/New_York"
    ),
    ValidServer(
      logId = "62-210-131-155.rev.poneytelecom.eu aura AssaultCube[local#1337]",
      name = "aura 1337",
      timezone = "Europe/Paris",
      address = "aura.woop.ac:1337"
    ),
    ValidServer(
      logId = "62-210-131-155.rev.poneytelecom.eu aura AssaultCube[local#1999]",
      name = "aura 1999",
      timezone = "Europe/Paris",
      address = "aura.woop.ac:1999"
    ),
    ValidServer(
      logId = "62-210-131-155.rev.poneytelecom.eu aura AssaultCube[local#2999]",
      name = "aura 2999",
      timezone = "Europe/Paris",
      address = "aura.woop.ac:2999"
    ),
    ValidServer(
      logId = "62-210-131-155.rev.poneytelecom.eu aura AssaultCube[local#3999]",
      name = "aura 3999",
      timezone = "Europe/Paris",
      address = "aura.woop.ac:3999"
    ),
    ValidServer(
      logId = "62-210-131-155.rev.poneytelecom.eu aura AssaultCube[local#4999]",
      name = "aura 4999",
      timezone = "Europe/Paris",
      address = "aura.woop.ac:4999"
    ),
    ValidServer(
      logId = "62-210-131-155.rev.poneytelecom.eu aura AssaultCube[local#8999]",
      name = "aura 8999",
      timezone = "Europe/Paris",
      address = "aura.woop.ac:8999"
    ),
    ValidServer(
      logId = "62-210-131-155.rev.poneytelecom.eu sd-55104 AssaultCube[local#1999]",
      name = "aura 1999",
      timezone = "Europe/Paris",
      address = "aura.woop.ac:1999"
    ),
    ValidServer(
      logId = "62-210-131-155.rev.poneytelecom.eu sd-55104 AssaultCube[local#2999]",
      name = "aura 2999",
      timezone = "Europe/Paris",
      address = "aura.woop.ac:2999"
    ),
    ValidServer(
      logId = "62-210-131-155.rev.poneytelecom.eu sd-55104 AssaultCube[local#4999]",
      name = "aura 4999",
      timezone = "Europe/Paris",
      address = "aura.woop.ac:4999"
    ),
    ValidServer(
      logId = "ny.weed-lounge.me ny AssaultCube[local#1999]",
      name = "ny 1999",
      timezone = "UTC"
    ),
    ValidServer(
      logId = "ny.weed-lounge.me ny AssaultCube[local#33333]",
      name = "ny 33333",
      timezone = "UTC",
      address = "ny.weed-lounge.me:33333"
    ),
    ValidServer(
      logId = "ny.weed-lounge.me ny AssaultCube[local#44444]",
      name = "ny 44444",
      timezone = "UTC",
      address = "ny.weed-lounge.me:44444"
    ),
    ValidServer(
      logId = "ny.weed-lounge.me ny AssaultCube[local#1111]",
      name = "ny 1111",
      timezone = "UTC"
    ),
    ValidServer(
      logId = "ny.weed-lounge.me ny AssaultCube[local#2222]",
      name = "ny 2222",
      timezone = "UTC"
    ),
    ValidServer(
      logId = "153.92.126.156 weed-lounge AssaultCube[local#1111]",
      name = "se 1111",
      timezone = "America/New_York"
    ),
    ValidServer(
      logId = "153.92.126.156 weed-lounge AssaultCube[local#2222]",
      name = "se 2222",
      timezone = "America/New_York"
    ),
    ValidServer(
      logId = "153.92.126.156 weed-lounge AssaultCube[local#3333]",
      name = "se 3333",
      timezone = "America/New_York"
    ),
    ValidServer(
      logId = "192.184.63.69 califa AssaultCube[local#1999]",
      name = "califa 1999",
      timezone = "UTC",
      address = "califa.actionfps.com:1999"
    ),
    ValidServer(
      logId = "nordfinsh.com bonza AssaultCube[local#1999]",
      name = "bonza 1999",
      timezone = "UTC",
      address = "bonza.actionfps.com:1999"
    ),
    ValidServer(
      logId = "191.96.4.147 legal AssaultCube[local#1999]",
      name = "legal 1999",
      timezone = "UTC",
      address = "legal.actionfps.com:1999"
    )
  )

  val fromResource = ValidServers(validServers.map(v => v.logId -> v).toMap)

  object ImplicitValidServers {
    implicit val fromResourceVS: ValidServers = fromResource
  }

  object Validator {

    implicit class validator(jsonGame: JsonGame)(implicit validServers: ValidServers) {
      def validateServer: Boolean = {
        val server = jsonGame.server
        validServers.items.exists(item => item._1 == server && item._2.isValid)
      }
    }

  }

}

case class ValidServer(logId: String, name: String, timezone: ZoneId, address: Option[String] = None, invalid: Option[Boolean] = None) {
  def isValid: Boolean = !invalid.contains(true)
}

case class ValidServers(items: Map[String, ValidServer]) {

  object FromLog {
    def unapply(string: String): Option[ValidServer] = items.get(string)
  }

  object AddressFromLog {
    def unapply(string: String): Option[String] = items.get(string).flatMap(_.address)
  }

}


