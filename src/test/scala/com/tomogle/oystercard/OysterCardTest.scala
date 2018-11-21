package com.tomogle.oystercard

import org.scalatest.FlatSpec
import org.scalatest.Matchers

import scala.util.Failure
import scala.util.Success
import scala.util.Try

/**
  *
  */
class OysterCardTest extends FlatSpec with Matchers {

  behavior of "OysterCardTest"

  it should "apply a charge to a balance" in {
    val resultCard = OysterCard(BigDecimal("3.00")).applyCharge(Charge(BigDecimal("2.00")))
    val expectedCard = Success(OysterCard(BigDecimal("1.00")))
    resultCard should be(expectedCard)
  }

  it should "apply a charge to take a balance to zero" in {
    val resultCard = OysterCard(BigDecimal("2.00")).applyCharge(Charge(BigDecimal("2.00")))
    val expectedCard = Success(OysterCard(BigDecimal("0.00")))
    resultCard should be(expectedCard)
  }

  it should "apply a negative charge to increase a balance" in {
    val resultCard = OysterCard(BigDecimal("3.00")).applyCharge(Charge(BigDecimal("-2.00")))
    val expectedCard = Success(OysterCard(BigDecimal("5.00")))
    resultCard should be(expectedCard)
  }

  it should "prevent entry by giving an error if applying charge would give a negative balance" in {
    val resultCard = OysterCard(BigDecimal("3.00")).applyCharge(Charge(BigDecimal("3.01")))
    val expectedCard: Try[OysterCard] = Failure(OysterCardBalanceTooLow)
    resultCard should be(expectedCard)
  }

}
