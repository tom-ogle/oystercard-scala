package com.tomogle

/**
  *
  */
package object oystercard {

  def reduceCharges(charges: Seq[Charge]): Charge =
    charges.foldLeft[Charge](ZeroCharge)((c1, c2) => combineCharges(c1, c2))

  def combineCharges(charge1: Charge, charge2: Charge): Charge =
    Charge(charge1.amount + charge2.amount)

}
