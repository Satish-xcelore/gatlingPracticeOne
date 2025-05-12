package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class AddUserSimulation extends Simulation {


  val httpConf=http.baseUrl("https://reqres.in")
    .header("Accept","application/json")
    .header("content-type","application/json")

  val scn=scenario("Add User")
    .exec(http("Add User request")
      .post("/api/users")
      .body(RawFileBody("./src/test/resources/bodies/AddUser.json")).asJson
      .header("x-api-key","reqres-free-v1")
      .check(status is 201)

    )
    .pause(3)
    .exec(http("Get User Request")
      .get("/api/users/2")
      .check(status is 200)
    )
    .pause(3)
    .exec(http("Get All User Request")
      .get("/api/users?page=2")
      .check(status is 200)
    )

  setUp(scn.inject(atOnceUsers(1))).protocols(httpConf)

}
