package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.core.structure.ChainBuilder
import scala.concurrent.duration._

class DocsoraLoginLoadSimulation extends Simulation {

  val httpConf = http.baseUrl("https://api-dev.docsora.com/")
    .header("Accept", "application/json")
    .header("Content-Type", "application/json")
    .proxy(Proxy("127.0.0.1", 8000))


  val generateEmail = exec { session =>
    val randomNum = scala.util.Random.nextInt(100000)
    val email = s"user$randomNum@domain.com"
    session.set("email", email)
  }

  def createAUser: ChainBuilder = {
    repeat(1) {
      exec(generateEmail)
        .exec(http("Create a new user")
          .post("api/v1/user/create-user")
          .body(ElFileBody("src/test/resources/docsoraBodies/CreateUser.json")).asJson
          .check(status.in(200, 201, 204))
        )
        .pause(2)
    }
  }

  val scn = scenario("Create Single User").exec(createAUser)

  setUp(
    scn.inject(
      nothingFor(5),
      constantUsersPerSec(10) during (10.seconds),
      rampUsersPerSec(1).to(5) during (20.seconds)
    ).protocols(httpConf)
  )
}
