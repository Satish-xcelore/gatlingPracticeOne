package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef.*
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef.*

class LoopCountRequest extends Simulation {

  val httpConf = http.baseUrl("https://reqres.in/")
    .header("content-type", "application/json")
    .header("accept", "application/json")
    .header("x-api-key", "reqres-free-v1")

  def getAllUserRequest: ChainBuilder = {
    repeat(2) {
      exec(http("Get All the users")
        .get("api/users?page=2")
        .check(status.is(200))
      )
    }
  }

  def getSingleUserRequest: ChainBuilder = {
    repeat(2) {
      exec(http("Get Single User Data")
        .get("api/users/2")
        .check(status.is(200))
      )
    }
  }

  def addUserRequest: ChainBuilder = {
    repeat(2) {
      exec(http("Add User Data")
        .post("api/users")
        .body(RawFileBody("./src/test/resources/bodies/AddUser.json")).asJson
        .check(status.in(200 to 201))
      )
    }
  }

  def updateUserRequest: ChainBuilder={
    repeat(2){
      exec(http("Update User Data")
        .put("api/users/2")
        .body(RawFileBody("./src/test/resources/bodies/UpdateUser.json")).asJson
        .check(status is 200)
      )
    }
  }

  val scn = scenario("User request scenario")
    .exec(getAllUserRequest)
    .pause(2)
    .exec(getSingleUserRequest)
    .pause(2)
    .exec(addUserRequest)
    .pause(2)
    .exec(updateUserRequest)

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)
}
