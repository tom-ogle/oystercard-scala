package com.tomogle.oystercard

import org.scalatest.FlatSpec
import org.scalatest.Matchers

/**
  *
  */
class FunctionalChargingFunctionTest extends FlatSpec with Matchers {

  trait AlwaysAppliesFareRule extends FareRule {
    override def applies(thisEvent: JourneyEvent, lastEvent: Option[JourneyEvent]): Boolean = true
  }

  trait NeverAppliesFareRule extends FareRule {
    override def applies(thisEvent: JourneyEvent, lastEvent: Option[JourneyEvent]): Boolean = false
  }

  behavior of "FunctionalChargingLogic"

  it should "sortFareRules" in {
    val rule1Applies: FareRule = new AlwaysAppliesFareRule {
      override def charge(): Seq[Charge] = Seq(
        Charge(BigDecimal("1.00")), Charge(BigDecimal("-1.00")), Charge(BigDecimal("5.00"))
      )
    }
    val rule2DoesNotApply: FareRule = new NeverAppliesFareRule {
      override def charge(): Seq[Charge] = Seq(
        Charge(BigDecimal("1.00")), Charge(BigDecimal("-1.00")), Charge(BigDecimal("2.00"))
      )
    }
    val rule3Applies: FareRule = new AlwaysAppliesFareRule {
      override def charge(): Seq[Charge] = Seq(
        Charge(BigDecimal("1.00")), Charge(BigDecimal("-1.00"))
      )
    }
    val rules = Seq(rule1Applies, rule2DoesNotApply, rule3Applies)
    val underTest = FunctionalChargingFunction(rules)
    val lastEvent = Some(JourneyEvent(EnterStation, Zone1Only, Tube))
    val thisEvent = JourneyEvent(ExitStation, Zone1Only, Tube)
    val expectedResult = Seq(Charge(BigDecimal("1.00")), Charge(BigDecimal("-1.00")))
    val actualResult: Seq[Charge] = underTest.apply(thisEvent, lastEvent)
    actualResult should be(expectedResult)
  }

  it should "applyChargingLogic" in {
    val expected1st: FareRule = new AlwaysAppliesFareRule {
      override def charge(): Seq[Charge] = Seq(
        Charge(BigDecimal("1.00")), Charge(BigDecimal("-1.00"))
      )
    }
    val expected2nd: FareRule = new AlwaysAppliesFareRule {
      override def charge(): Seq[Charge] = Seq(
        Charge(BigDecimal("1.00")), Charge(BigDecimal("-1.00")), Charge(BigDecimal("2.00"))
      )
    }
    val expected3rd: FareRule = new AlwaysAppliesFareRule {
      override def charge(): Seq[Charge] = Seq(
        Charge(BigDecimal("1.00")), Charge(BigDecimal("-1.00")), Charge(BigDecimal("5.00"))
      )
    }
    val rules = Seq(expected2nd, expected3rd, expected1st)
    val underTest = FunctionalChargingFunction(rules)
    val expectedResult = Seq(expected1st, expected2nd, expected3rd)
    val actualResult = underTest.sortFareRules(rules)
    actualResult should be(expectedResult)
  }



}
