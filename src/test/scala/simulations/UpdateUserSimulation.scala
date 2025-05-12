package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class UpdateUserSimulation extends Simulation {

  val httpConf=http.baseUrl("https://reqres.in/")
    .header("content-type","application/json")
    .header("accept","application/json")

  val scn=scenario("Update User Details")
    .exec(http("Update User")
      .put("/api/users/2")
      .header("x-api-key","reqres-free-v1")
      .body(RawFileBody("./src/test/resources/bodies/Updateuser.json")).asJson
      .check(status.in(200 to 204))

    )
    setUp(scn.inject(atOnceUsers(1))).protocols(httpConf)

}
