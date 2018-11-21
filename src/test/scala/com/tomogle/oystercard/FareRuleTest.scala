package com.tomogle.oystercard

import org.scalatest.FlatSpec
import org.scalatest.Matchers

/**
  *
  */
class FareRuleTest extends FlatSpec with Matchers {

  // TODO: Further cases, especially for Any2ZonesExcludingZone1

  behavior of "ChargeMaxOnEnterStation"

  it should "apply for start of tube journey" in {
    val lastEvent = Some(JourneyEvent(ExitStation, Zone1Only, Tube))
    val thisEvent = JourneyEvent(EnterStation, Zone1Only, Tube)
    val result = ChargeMaxOnEnterStation.applies(thisEvent, lastEvent)
    result should be(true)
  }

  it should "not apply for end of tube journey" in {
    val lastEvent = Some(JourneyEvent(EnterStation, Zone1Only, Tube))
    val thisEvent = JourneyEvent(ExitStation, Zone1Only, Tube)
    val result = ChargeMaxOnEnterStation.applies(thisEvent, lastEvent)
    result should be(false)
  }

  it should "apply for start of bus journey" in {
    val lastEvent = Some(JourneyEvent(ExitStation, Zone1Only, Bus))
    val thisEvent = JourneyEvent(EnterStation, Zone1Only, Bus)
    val result = ChargeMaxOnEnterStation.applies(thisEvent, lastEvent)
    result should be(true)
  }

  it should "not apply for end of bus journey" in {
    val lastEvent = Some(JourneyEvent(EnterStation, Zone1Only, Bus))
    val thisEvent = JourneyEvent(ExitStation, Zone1Only, Bus)
    val result = ChargeMaxOnEnterStation.applies(thisEvent, lastEvent)
    result should be(false)
  }

  behavior of "AnywhereInZone1"

  it should "apply for end of tube journey only within zone 1" in {
    val lastEvent = Some(JourneyEvent(EnterStation, Zone1Only, Tube))
    val thisEvent = JourneyEvent(ExitStation, Zone1Only, Tube)
    val result = AnywhereInZone1.applies(thisEvent, lastEvent)
    result should be(true)
  }

  it should "not apply for tube journey that is just starting" in {
    val lastEvent: Option[JourneyEvent] = None
    val thisEvent = JourneyEvent(EnterStation, Zone1Only, Tube)
    val result = AnywhereInZone1.applies(thisEvent, lastEvent)
    result should be(false)
  }

  it should "not apply for tube journey exiting without entrance station" in {
    val lastEvent = Some(JourneyEvent(EnterStation, Zone1Only, Tube))
    val thisEvent = JourneyEvent(EnterStation, Zone1Only, Tube)
    val result = AnywhereInZone1.applies(thisEvent, lastEvent)
    result should be(false)
  }

  it should "not apply for tube journey that is entirely outside zone 1" in {
    val lastEvent = Some(JourneyEvent(EnterStation, Zone2Only, Tube))
    val thisEvent = JourneyEvent(ExitStation, Zone2Only, Tube)
    val result = AnywhereInZone1.applies(thisEvent, lastEvent)
    result should be(false)
  }

  it should "not apply for tube journey starting in zone 1 and finishing in another zone" in {
    val lastEvent = Some(JourneyEvent(EnterStation, Zone1Only, Tube))
    val thisEvent = JourneyEvent(ExitStation, Zone2Only, Tube)
    val result = AnywhereInZone1.applies(thisEvent, lastEvent)
    result should be(false)
  }

  it should "not apply for tube journey starting in another zone and finishing in zone 1" in {
    val lastEvent = Some(JourneyEvent(EnterStation, Zone2Only, Tube))
    val thisEvent = JourneyEvent(ExitStation, Zone1Only, Tube)
    val result = AnywhereInZone1.applies(thisEvent, lastEvent)
    result should be(false)
  }

  it should "not apply for end of bus journey" in {
    val lastEvent = Some(JourneyEvent(EnterStation, Zone1Only, Bus))
    val thisEvent = JourneyEvent(ExitStation, Zone1Only, Bus)
    val result = AnywhereInZone1.applies(thisEvent, lastEvent)
    result should be(false)
  }

  it should "not apply for start of bus journey" in {
    val lastEvent = Some(JourneyEvent(ExitStation, Zone1Only, Bus))
    val thisEvent = JourneyEvent(EnterStation, Zone1Only, Bus)
    val result = AnywhereInZone1.applies(thisEvent, lastEvent)
    result should be(false)
  }

  it should "not apply for start of tube journey" in {
    val lastEvent = Some(JourneyEvent(ExitStation, Zone1Only, Tube))
    val thisEvent = JourneyEvent(EnterStation, Zone1Only, Tube)
    val result = AnywhereInZone1.applies(thisEvent, lastEvent)
    result should be(false)
  }

  it should "apply for end of tube journey within zone 1 where start station is also in another zone" in {
    val lastEvent = Some(JourneyEvent(EnterStation, Zones1And2, Tube))
    val thisEvent = JourneyEvent(ExitStation, Zone1Only, Tube)
    val result = AnywhereInZone1.applies(thisEvent, lastEvent)
    result should be(true)
  }

  it should "apply for end of tube journey within zone 1 where end station is also in another zone" in {
    val lastEvent = Some(JourneyEvent(EnterStation, Zone1Only, Tube))
    val thisEvent = JourneyEvent(ExitStation, Zones1And2, Tube)
    val result = AnywhereInZone1.applies(thisEvent, lastEvent)
    result should be(true)
  }

  it should "apply for end of tube journey within zone 1 where start and end stations are also in another zone" in {
    val lastEvent = Some(JourneyEvent(EnterStation, Zones1And2And3, Tube))
    val thisEvent = JourneyEvent(ExitStation, Zones1And2, Tube)
    val result = AnywhereInZone1.applies(thisEvent, lastEvent)
    result should be(true)
  }

  it should "not apply for tube journey that ends with no previous event" in {
    val lastEvent: Option[JourneyEvent] = None
    val thisEvent = JourneyEvent(EnterStation, Zone1Only, Tube)
    val result = AnywhereInZone1.applies(thisEvent, lastEvent)
    result should be(false)
  }

  behavior of "AnyBusJourney"

  it should "not apply for start of bus journey" in {
    val lastEvent = Some(JourneyEvent(ExitStation, Zone1Only, Tube))
    val thisEvent = JourneyEvent(EnterStation, Zone1Only, Bus)
    val result = AnyBusJourney.applies(thisEvent, lastEvent)
    result should be(false)
  }

  it should "apply for end of bus journey" in {
    val lastEvent = Some(JourneyEvent(EnterStation, Zone1Only, Bus))
    val thisEvent = JourneyEvent(ExitStation, Zone1Only, Bus)
    val result = AnyBusJourney.applies(thisEvent, lastEvent)
    result should be(true)
  }

  behavior of "Any1ZoneOutsideZone1"

  it should "not apply for start of bus journey" in {
    val lastEvent = Some(JourneyEvent(ExitStation, Zone2Only, Tube))
    val thisEvent = JourneyEvent(EnterStation, Zone2Only, Bus)
    val result = Any1ZoneOutsideZone1.applies(thisEvent, lastEvent)
    result should be(false)
  }

  it should "not apply for end of bus journey" in {
    val lastEvent = Some(JourneyEvent(EnterStation, Zone2Only, Tube))
    val thisEvent = JourneyEvent(ExitStation, Zone2Only, Bus)
    val result = Any1ZoneOutsideZone1.applies(thisEvent, lastEvent)
    result should be(false)
  }

  it should "not apply for start of a tube journey" in {
    val lastEvent = Some(JourneyEvent(ExitStation, Zone2Only, Tube))
    val thisEvent = JourneyEvent(EnterStation, Zone2Only, Tube)
    val result = Any1ZoneOutsideZone1.applies(thisEvent, lastEvent)
    result should be(false)
  }

  it should "not apply for start of a tube journey where there is no last event" in {
    val lastEvent = Some(JourneyEvent(ExitStation, Zone2Only, Tube))
    val thisEvent = JourneyEvent(EnterStation, Zone2Only, Tube)
    val result = Any1ZoneOutsideZone1.applies(thisEvent, lastEvent)
    result should be(false)
  }

  it should "apply for a tube journey in a single zone that isn't zone1" in {
    val lastEvent = Some(JourneyEvent(EnterStation, Zone2Only, Tube))
    val thisEvent = JourneyEvent(ExitStation, Zone2Only, Tube)
    val result = Any1ZoneOutsideZone1.applies(thisEvent, lastEvent)
    result should be(true)
  }

  it should "not apply for a tube journey starting in zone 1" in {
    val lastEvent = Some(JourneyEvent(EnterStation, Zone1Only, Tube))
    val thisEvent = JourneyEvent(ExitStation, Zone2Only, Tube)
    val result = Any1ZoneOutsideZone1.applies(thisEvent, lastEvent)
    result should be(false)
  }

  it should "not apply for a tube journey ending in zone 1" in {
    val lastEvent = Some(JourneyEvent(EnterStation, Zone2Only, Tube))
    val thisEvent = JourneyEvent(ExitStation, Zone1Only, Tube)
    val result = Any1ZoneOutsideZone1.applies(thisEvent, lastEvent)
    result should be(false)
  }

  behavior of "Any2ZonesIncludingZone1"

  it should "apply for a tube journey across 2 zones, including zone1 at start" in {
    val lastEvent = Some(JourneyEvent(EnterStation, Zone1Only, Tube))
    val thisEvent = JourneyEvent(ExitStation, Zone2Only, Tube)
    val result = Any2ZonesIncludingZone1.applies(thisEvent, lastEvent)
    result should be(true)
  }

  it should "apply for a tube journey across 2 zones, including zone1 at end" in {
    val lastEvent = Some(JourneyEvent(EnterStation, Zone3Only, Tube))
    val thisEvent = JourneyEvent(ExitStation, Zone1Only, Tube)
    val result = Any2ZonesIncludingZone1.applies(thisEvent, lastEvent)
    result should be(true)
  }

  it should "not apply for a tube journey entering a station" in {
    val lastEvent = Some(JourneyEvent(ExitStation, Zone1Only, Tube))
    val thisEvent = JourneyEvent(EnterStation, Zone1Only, Tube)
    val result = Any2ZonesIncludingZone1.applies(thisEvent, lastEvent)
    result should be(false)
  }

  it should "not apply for a tube journey only inside 1 zone" in {
    val lastEvent = Some(JourneyEvent(EnterStation, Zone1Only, Tube))
    val thisEvent = JourneyEvent(ExitStation, Zone1Only, Tube)
    val result = Any2ZonesIncludingZone1.applies(thisEvent, lastEvent)
    result should be(false)
  }

  it should "not apply for a tube journey with 2 zones but no zone 1" in {
    val lastEvent = Some(JourneyEvent(EnterStation, Zone2Only, Tube))
    val thisEvent = JourneyEvent(ExitStation, Zone3Only, Tube)
    val result = Any2ZonesIncludingZone1.applies(thisEvent, lastEvent)
    result should be(false)
  }

  it should "not apply for a tube journey where there are the same multiple zones for entry and exit stations, including zone1" in {
    val lastEvent = Some(JourneyEvent(EnterStation, Zones1And2, Tube))
    val thisEvent = JourneyEvent(ExitStation, Zones1And2, Tube)
    val result = Any2ZonesIncludingZone1.applies(thisEvent, lastEvent)
    result should be(false)
  }

  it should "apply for a tube journey where there are multiple zones for entry and exit stations that don't fully overlap, including zone1" in {
    val lastEvent = Some(JourneyEvent(EnterStation, Zones1And2, Tube))
    val thisEvent = JourneyEvent(ExitStation, Zones2And3, Tube)
    val result = Any2ZonesIncludingZone1.applies(thisEvent, lastEvent)
    result should be(true)
  }

  behavior of "Any2ZonesExcludingZone1"

  it should "not apply for start of bus journey" in {
    val lastEvent = Some(JourneyEvent(ExitStation, Zone2Only, Tube))
    val thisEvent = JourneyEvent(EnterStation, Zone2Only, Bus)
    val result = Any2ZonesExcludingZone1.applies(thisEvent, lastEvent)
    result should be(false)
  }

  it should "not apply for end of bus journey" in {
    val lastEvent = Some(JourneyEvent(EnterStation, Zone2Only, Tube))
    val thisEvent = JourneyEvent(ExitStation, Zone2Only, Bus)
    val result = Any2ZonesExcludingZone1.applies(thisEvent, lastEvent)
    result should be(false)
  }

  it should "not apply for start of a tube journey" in {
    val lastEvent = Some(JourneyEvent(ExitStation, Zone2Only, Tube))
    val thisEvent = JourneyEvent(EnterStation, Zone2Only, Tube)
    val result = Any2ZonesExcludingZone1.applies(thisEvent, lastEvent)
    result should be(false)
  }

  it should "not apply for tube journey with 2 zones, including zone1 at start" in {
    val lastEvent = Some(JourneyEvent(EnterStation, Zone1Only, Tube))
    val thisEvent = JourneyEvent(ExitStation, Zone2Only, Tube)
    val result = Any2ZonesExcludingZone1.applies(thisEvent, lastEvent)
    result should be(false)
  }

  it should "not apply for tube journey with 2 zones, including zone1 at end" in {
    val lastEvent = Some(JourneyEvent(ExitStation, Zone2Only, Tube))
    val thisEvent = JourneyEvent(EnterStation, Zone1Only, Tube)
    val result = Any2ZonesExcludingZone1.applies(thisEvent, lastEvent)
    result should be(false)
  }

}
