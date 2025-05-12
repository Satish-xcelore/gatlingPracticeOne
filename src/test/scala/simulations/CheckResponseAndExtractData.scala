package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class CheckResponseAndExtractData extends Simulation{

  val httpConf=http.baseUrl("https://gorest.co.in/")
    .header("Authorization","Bearer 14d493af1714a677c58c599a9a279e3b48bf2cbeeea079af9b611244cbad6ce8")


  val scn=scenario("Correlation and extract data")
    .exec(http("get all users")
    .get("public/v2/users")
      .check(jsonPath("$[1].id").saveAs("userId"))
    )

  exec(http("Get Specific User")
    .get("public/v2/users/${userId}")
    .check(jsonPath("$[1].id").is("7866775"))
    .check(jsonPath("$[1].name").is("Chandrabhaga Varrier"))
    .check(jsonPath("$[1].email").is("chandrabhaga_varrier@casper-nader.example"))
  )

  setUp(scn.inject(atOnceUsers(1))).protocols(httpConf)



}
