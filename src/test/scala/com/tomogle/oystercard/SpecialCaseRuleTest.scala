package com.tomogle.oystercard

import org.scalatest.FlatSpec
import org.scalatest.Matchers

/**
  *
  */
class SpecialCaseRuleTest extends FlatSpec with Matchers {

  behavior of "ChargeMaxFareForEntryGateSkippers"

  it should "apply for first ever exit" in {
    val lastEvent = None
    val thisEvent = JourneyEvent(ExitStation, Zone1Only, Tube)
    val result = ChargeMaxFareForEntryGateSkippers.applies(thisEvent, lastEvent)
    result should be(true)
  }

  it should "apply for exit followed by exit" in {
    val lastEvent = Some(JourneyEvent(ExitStation, Zone1Only, Tube))
    val thisEvent = JourneyEvent(ExitStation, Zone1Only, Tube)
    val result = ChargeMaxFareForEntryGateSkippers.applies(thisEvent, lastEvent)
    result should be(true)
  }

  it should "not apply for first exit following enter" in {
    val lastEvent = Some(JourneyEvent(EnterStation, Zone1Only, Tube))
    val thisEvent = JourneyEvent(ExitStation, Zone1Only, Tube)
    val result = ChargeMaxFareForEntryGateSkippers.applies(thisEvent, lastEvent)
    result should be(false)
  }

}
