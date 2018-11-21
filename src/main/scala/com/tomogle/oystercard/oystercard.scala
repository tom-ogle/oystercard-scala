package com.tomogle

import scala.annotation.tailrec
import scala.util.Failure
import scala.util.Success
import scala.util.Try

/**
  *
  */
package object oystercard {

  def simulateJourney(events: Seq[JourneyEvent], oysterCard: OysterCard)(chargingLogic: ChargingFunction): Try[OysterCard]  = {
    @tailrec
    def go(remainingEvents: Seq[JourneyEvent], lastEvent: Option[JourneyEvent], card: OysterCard): Try[OysterCard]  = {
      remainingEvents match {
        case Nil => Success(card)
        case h :: t =>
          val charge = reduceCharges(chargingLogic.apply(h, lastEvent))
          val maybeUpdatedCard = card.applyCharge(charge)
          maybeUpdatedCard match {
            case Success(updatedCard) => go(t, Some(h), updatedCard)
            case error: Failure[OysterCard] =>
              // Terminate the reduce early on error
              error
          }
      }
    }
    go(events, None, oysterCard)
  }

  def reduceCharges(charges: Seq[Charge]): Charge =
    charges.foldLeft[Charge](ZeroCharge)((c1, c2) => combineCharges(c1, c2))

  def combineCharges(charge1: Charge, charge2: Charge): Charge =
    Charge(charge1.amount + charge2.amount)

}
