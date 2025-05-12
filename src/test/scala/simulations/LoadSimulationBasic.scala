package simulations

import io.gatling.core.Predef.Simulation
import io.gatling.core.Predef.*
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef.*
import scala.concurrent.duration.DurationInt

import scala.language.postfixOps


class LoadSimulationBasic extends Simulation{

  val httpConf=http.baseUrl("https://reqres.in")
    .proxy(Proxy("127.0.0.1",8000))
    .header("x-api-key","reqres-free-v1")

  val dataFeeder=csv("testData/ReqresData.csv").circular

  def getAUser: ChainBuilder={
    repeat(1){
      feed(dataFeeder)
        .exec(http("Get Single user request")
          .get("/api/unknown/${id}")
          .check(jsonPath("$.data.name").is("${name}"))
          .check(status.in(200,304))
        )
        .pause(2)
    }
  }

  val scn=scenario("basic load simulation").exec(getAUser)

  setUp(
    scn.inject(nothingFor(5),
      atOnceUsers(5),
      rampUsers(10) during(10 seconds)
    ).protocols(httpConf)
  )

}
