package com.tomogle.oystercard

import org.scalatest.FlatSpec
import org.scalatest.Matchers

/**
  *
  */
class PackageTest extends FlatSpec with Matchers {

  behavior of "reduceCharges"

  it should "reduce charges" in {
    val expectedResult = Charge(BigDecimal("3.00"))
    val result = reduceCharges(Seq(Charge(BigDecimal("1.00")), Charge(BigDecimal("1.00")), Charge(BigDecimal("1.00"))))
    result should be(expectedResult)
  }

  it should "reduce negative and positive charges" in {
    val expectedResult = Charge(BigDecimal("-1.00"))
    val result = reduceCharges(Seq(Charge(BigDecimal("-1.00")), Charge(BigDecimal("1.00")), Charge(BigDecimal("-1.00"))))
    result should be(expectedResult)
  }

  behavior of "combineCharges"

  it should "combine charges" in {
    val expectedResult = Charge(BigDecimal("4.00"))
    val result = combineCharges(Charge(BigDecimal("1.99")), Charge(BigDecimal("2.01")))
      result should be(expectedResult)
  }

  it should "combine negative charges" in {
    val expectedResult = Charge(BigDecimal("-4.00"))
    val result = combineCharges(Charge(BigDecimal("-1.99")), Charge(BigDecimal("-2.01")))
    result should be(expectedResult)
  }

}
