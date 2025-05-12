package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.structure.ChainBuilder
import scala.concurrent.duration._

class RampupUserLoadSimulation extends Simulation {

  val httpConf = http.baseUrl("https://reqres.in")
    .proxy(Proxy("127.0.0.1", 8000))
    .header("x-api-key", "reqres-free-v1")
    .header("Accept", "application/json")
    .header("Content-Type", "application/json")

  val dataFeeder = csv("testData/ReqresData.csv").circular

  def getAUserData: ChainBuilder = {
    repeat(1) {
      feed(dataFeeder)
        .exec(http("Get a single User data")
          .get("/api/unknown/${id}")
          .check(jsonPath("$.data.name").is("${name}"))
          .check(status.in(200, 304))
        )
        .pause(2)
    }
  }

  val scn = scenario("Rampup user load Simulation").exec(getAUserData)

  setUp(
    scn.inject(
      nothingFor(5),
      constantUsersPerSec(10) during (10.seconds),
      rampUsersPerSec(1).to(5) during (20.seconds)
    ).protocols(httpConf)
  )
}
