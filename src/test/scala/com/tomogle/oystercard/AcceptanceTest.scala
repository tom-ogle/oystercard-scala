package com.tomogle.oystercard

import org.scalatest.FlatSpec
import org.scalatest.Matchers

import scala.util.Success

/**
  *
  */
class AcceptanceTest extends FlatSpec with Matchers {
  behavior of "OysterCard system"

  it should "run the scenario correctly" in {
    val thirtyPounds = BigDecimal("30.00")
    val card = OysterCard().load(thirtyPounds)
    val events = Seq(
      JourneyEvent(EnterStation, Holborn, Tube),
      JourneyEvent(ExitStation, EarlsCourt, Tube),

      JourneyEvent(EnterStation, EarlsCourt, Bus),
      JourneyEvent(ExitStation, Chelsea, Bus),

      JourneyEvent(EnterStation, EarlsCourt, Tube),
      JourneyEvent(ExitStation, Hammersmith, Tube),
    )
    val updatedCard = simulateJourney(events, card)(FunctionalChargingFunction())
    updatedCard should be(Success(OysterCard(BigDecimal("22.70"))))
  }
}
