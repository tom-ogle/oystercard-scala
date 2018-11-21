package com.tomogle.oystercard

trait FareRule {
  def charge(): Seq[Charge]
  def applies(thisEvent: JourneyEvent, lastEvent: Option[JourneyEvent]): Boolean
}

object FareRule {
  val allRules = Seq(
    ChargeMaxOnEnterStation,
    AnywhereInZone1,
    AnyBusJourney,
    Any1ZoneOutsideZone1,
    Any2ZonesIncludingZone1,
    Any2ZonesExcludingZone1
  )

  def anyTwoZones(thisEvent: JourneyEvent, lastEvent: Option[JourneyEvent]): Boolean = {
    lastEvent.exists { previousEvent =>
        thisEvent.station.zones.union(previousEvent.station.zones).size > 1 &&
        thisEvent.station.zones != previousEvent.station.zones
    }
  }

  def includesZone1(thisEvent: JourneyEvent, lastEvent: Option[JourneyEvent]): Boolean =
    lastEvent.exists(thisEvent.station.zones.contains(Zone1) || _.station.zones.contains(Zone1))

  def isExitingStation(thisEvent: JourneyEvent): Boolean =
    thisEvent.eventType == ExitStation

  def isEnteringStation(thisEvent: JourneyEvent): Boolean =
    thisEvent.eventType == EnterStation
}

object MaxFareCharge extends Charge(BigDecimal("3.20"))
object ReverseMaxFareCharge extends Charge(MaxFareCharge.amount * BigDecimal("-1"))

object ChargeMaxOnEnterStation extends FareRule {
  override def charge(): Seq[Charge] = Seq(MaxFareCharge)
  override def applies(thisEvent: JourneyEvent, lastEvent: Option[JourneyEvent]): Boolean =
    FareRule.isEnteringStation(thisEvent)
}

object AnywhereInZone1 extends FareRule {
  override def charge(): Seq[Charge] = Seq(ReverseMaxFareCharge, Charge(BigDecimal("2.50")))
  override def applies(thisEvent: JourneyEvent, lastEvent: Option[JourneyEvent]): Boolean =
    FareRule.isExitingStation(thisEvent) &&
    lastEvent.exists { previousEvent =>
        thisEvent.station.zones.contains(Zone1) &&
        previousEvent.station.zones.contains(Zone1) &&
        thisEvent.transportType == Tube
    }
}

object AnyBusJourney extends FareRule {
  override def charge(): Seq[Charge] = Seq(ReverseMaxFareCharge, Charge(BigDecimal("1.80")))
  override def applies(thisEvent: JourneyEvent, lastEvent: Option[JourneyEvent]): Boolean =
    FareRule.isExitingStation(thisEvent) &&
      thisEvent.transportType == Bus
}

object Any1ZoneOutsideZone1 extends FareRule {
  override def charge(): Seq[Charge] = Seq(ReverseMaxFareCharge, Charge(BigDecimal("2.00")))
  override def applies(thisEvent: JourneyEvent, lastEvent: Option[JourneyEvent]): Boolean =
    FareRule.isExitingStation(thisEvent) &&
      !thisEvent.station.zones.contains(Zone1) &&
      thisEvent.transportType == Tube &&
      lastEvent.exists { previousEvent =>
      !previousEvent.station.zones.contains(Zone1)
    }
}

object Any2ZonesIncludingZone1 extends FareRule {
  override def charge(): Seq[Charge] = Seq(ReverseMaxFareCharge, Charge(BigDecimal("3.00")))
  override def applies(thisEvent: JourneyEvent, lastEvent: Option[JourneyEvent]): Boolean =
    FareRule.isExitingStation(thisEvent) &&
      FareRule.anyTwoZones(thisEvent, lastEvent) &&
      FareRule.includesZone1(thisEvent, lastEvent)

}

object Any2ZonesExcludingZone1 extends FareRule {
  override def charge(): Seq[Charge] = Seq(ReverseMaxFareCharge, Charge(BigDecimal("2.25")))
  override def applies(thisEvent: JourneyEvent, lastEvent: Option[JourneyEvent]): Boolean =
    FareRule.isExitingStation(thisEvent) &&
      FareRule.anyTwoZones(thisEvent, lastEvent) &&
      !FareRule.includesZone1(thisEvent, lastEvent)
}
