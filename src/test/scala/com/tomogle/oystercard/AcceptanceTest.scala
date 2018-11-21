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
    // TODO: Really should rework this to run card.applyCharge after every event's charges are generated to
    // model preventing a user from entering if they don't have enough balance for max charge
    val events = Seq(
      JourneyEvent(EnterStation, Holborn, Tube),
      JourneyEvent(ExitStation, EarlsCourt, Tube),

      JourneyEvent(EnterStation, EarlsCourt, Bus),
      JourneyEvent(ExitStation, Chelsea, Bus),

      JourneyEvent(EnterStation, EarlsCourt, Tube),
      JourneyEvent(ExitStation, Hammersmith, Tube),
    )
    val accumulator = ChargeAccumulator()
    val aggregatedCharge = reduceCharges(accumulator.accumulateCharges(events))
    val updatedCard = card.applyCharge(aggregatedCharge)
    println(updatedCard)
    updatedCard should be(Success(OysterCard(BigDecimal("22.70"))))
  }
}
