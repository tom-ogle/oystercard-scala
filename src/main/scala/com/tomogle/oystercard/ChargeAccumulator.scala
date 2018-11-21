package com.tomogle.oystercard

/**
  *
  */
trait ChargeAccumulator {
  this: ChargingLogic =>

  def accumulateCharges(events: Seq[JourneyEvent]):  Seq[Charge] = {
    val (_, charges) = events.foldLeft[(Option[JourneyEvent], Seq[Charge])]((None, Seq()))((accumulator, thisEvent) => {
      val (lastEvent, chargesSoFar) = accumulator
      accumulateCharge(thisEvent, lastEvent, chargesSoFar)
    })
    charges
  }

  private def accumulateCharge(thisEvent: JourneyEvent, lastEvent: Option[JourneyEvent], chargesSoFar: Seq[Charge]): (Some[JourneyEvent], Seq[Charge]) = {
    val newCharges = this.applyChargingLogic(thisEvent, lastEvent)
    val allCharges = chargesSoFar ++ newCharges
    val newlyAccumulatedCharges = (Some(thisEvent), allCharges)
    newlyAccumulatedCharges
  }

}

object ChargeAccumulator {
  def apply(): ChargeAccumulator = new ChargeAccumulator with FunctionalChargingLogic {
    override val fareRules: Seq[FareRule] = FareRule.allRules
  }
}