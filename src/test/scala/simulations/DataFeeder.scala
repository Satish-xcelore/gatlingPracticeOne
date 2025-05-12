package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.structure.ChainBuilder

class DataFeeder extends Simulation {

  val httpConf=http.baseUrl("https://gorest.co.in/")
    .header("Authorization","Bearer 4ff8fdebfe194b3a97c372da5399e522dfa00b0ddd88946e9ccdc4bd75d7d355")

  val dataFeeder=csv("testData/userFeeder.csv").circular

  def getSingleUser : ChainBuilder={
  repeat(7){
    exec(http("get single user data")
      .get("public/v2/users/${userId}")
      .check(jsonPath("$.name").is("${username}"))
      .check(status.in(200,304))
    )
      .pause(2)
  }
  }

  val scn=scenario("csv data feeder test ").feed(dataFeeder).exec(getSingleUser)

  setUp(scn.inject(atOnceUsers(1))).protocols(httpConf)


}
