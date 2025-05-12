package docsoraAPISimulation

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class CreateUser extends Simulation {


  val httpConf=http.baseUrl("https://api-dev.docsora.com/")
    .header("content-type","application/json")
    .header("accept","application/json")

  val scn=scenario("Create User")
    .exec(http("Create new user")
      .post("api/v1/user/create-user")
      .body(RawFileBody("./src/test/resources/docsoraBodies/CreateUser.json")).asJson
      .check(status.is(201))
    )

  setUp(scn.inject(atOnceUsers(1))).protocols(httpConf)

}
