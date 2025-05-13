package simulations

import io.gatling.core.Predef.{Simulation, during, *}
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef.*
import io.gatling.core.Predef.during

import scala.concurrent.duration.*


class LoginLoadSimulation extends Simulation{

  val httpConf=http.baseUrl("https://api-dev.docsora.com")
    .header("Content-type","application/json")
    .header("Accept","application/json")
    .proxy(Proxy("127.0.0.1", 8000))

  def loginUser: ChainBuilder={
    repeat(1){
      exec()
        .exec(http("Login User")
          .post("/api/v1/auth/login")
          .body(StringBody("""{"emailId":"jeyosah860@deusa7.com", "password":"Password@12"}"""))
          .check(status.is(200))
        )
    }
  }

  val scn = scenario("Login API Load Test")
    .exec(loginUser)


        setUp (
        scn.inject(
          constantUsersPerSec(50).during(5.seconds),
          rampUsersPerSec(1).to(5) during(10.seconds)
        ).protocols(httpConf)
        )
}
