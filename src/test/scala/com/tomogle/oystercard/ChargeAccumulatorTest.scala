package com.tomogle.oystercard

import org.scalatest.FlatSpec
import org.scalatest.Matchers

/**
  *
  */
class ChargeAccumulatorTest extends FlatSpec with Matchers {

  trait OnePoundCharge extends ChargingLogic {
    override def applyChargingLogic(thisEvent: JourneyEvent, lastEvent: Option[JourneyEvent]): Seq[Charge] = Seq(onePound)
  }

  val onePound = Charge(BigDecimal("1.00"))

  behavior of "ChargeAccumulator"

  it should "accumulate charges" in {
    val underTest = new ChargeAccumulator with OnePoundCharge {}
    val events = Seq(
      JourneyEvent(EnterStation, Zone1Only, Tube),
      JourneyEvent(EnterStation, Zone1Only, Tube),
      JourneyEvent(EnterStation, Zone1Only, Tube),
    )
    val expectedCharges = Seq(onePound, onePound, onePound)
    val resultingCharges = underTest.accumulateCharges(events)
    expectedCharges should be(resultingCharges)
  }


}
