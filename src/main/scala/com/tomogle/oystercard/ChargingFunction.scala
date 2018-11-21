package com.tomogle.oystercard

/**
  *
  */
trait ChargingFunction {
  def apply(thisEvent: JourneyEvent, lastEvent: Option[JourneyEvent]): Seq[Charge]
}

object FunctionalChargingFunction {
  def apply(rules: Seq[FareRule]): FunctionalChargingFunction = new FunctionalChargingFunction {
    override val fareRules: Seq[FareRule] = rules
  }

  def apply(): FunctionalChargingFunction = FunctionalChargingFunction(FareRule.allRules)
}

trait FunctionalChargingFunction extends ChargingFunction {
  val fareRules: Seq[FareRule]

  private lazy val sortedFareRules = sortFareRules(fareRules)

  def apply(thisEvent: JourneyEvent, lastEvent: Option[JourneyEvent]): Seq[Charge] = {
    if (ChargeMaxFareForEntryGateSkippers.applies(thisEvent, lastEvent))
      ChargeMaxFareForEntryGateSkippers.charge()
    else {
      val ruleToApply: Option[FareRule] = sortedFareRules.find(rule => {
        rule.applies(thisEvent, lastEvent)
      })
      ruleToApply.map(rule => rule.charge()).getOrElse(Seq())
    }
  }

  def sortFareRules(rules: Seq[FareRule]): Seq[FareRule] = rules.sortBy(f => reduceCharges(f.charge()).amount)
}
