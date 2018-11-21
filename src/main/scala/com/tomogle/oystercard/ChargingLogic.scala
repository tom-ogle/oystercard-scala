package com.tomogle.oystercard

/**
  *
  */
trait ChargingLogic {
  def applyChargingLogic(thisEvent: JourneyEvent, lastEvent: Option[JourneyEvent]): Seq[Charge]
}

object FunctionalChargingLogic {
  def apply(rules: Seq[FareRule]): FunctionalChargingLogic = new FunctionalChargingLogic {
    override val fareRules: Seq[FareRule] = rules
  }
}

trait FunctionalChargingLogic extends ChargingLogic {
  val fareRules: Seq[FareRule]

  private lazy val sortedFareRules = sortFareRules(fareRules)

  def applyChargingLogic(thisEvent: JourneyEvent, lastEvent: Option[JourneyEvent]): Seq[Charge] = {
    val ruleToApply: Option[FareRule] = sortedFareRules.find(rule => {
      rule.applies(thisEvent, lastEvent)
    })
    ruleToApply.map(rule => rule.charge()).getOrElse(Seq())
  }

  def sortFareRules(rules: Seq[FareRule]): Seq[FareRule] = rules.sortBy(f => reduceCharges(f.charge()).amount)
}
