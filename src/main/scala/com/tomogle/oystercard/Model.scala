package com.tomogle.oystercard

import scala.util.Failure
import scala.util.Success
import scala.util.Try

/**
  * The model for the application
  */

case class JourneyEvent(eventType: EventType, station: Station, transportType: TransportType)

class Station(val name: String, val zones: Set[Zone])
case object Holborn extends Station("Holborn", Set(Zone1))
case object EarlsCourt extends Station("Earl’s Court", Set(Zone1, Zone2))
case object Wimbledon extends Station("Wimbledon", Set(Zone3))
case object Hammersmith extends Station("Holborn", Set(Zone2))
case object Chelsea extends Station("Chelsea", Set())

abstract class Zone
case object Zone1 extends Zone
case object Zone2 extends Zone
case object Zone3 extends Zone

abstract class EventType
case object EnterStation extends EventType
case object ExitStation extends EventType

abstract class TransportType
case object Bus extends TransportType
case object Tube extends TransportType

case class OysterCard(balance: BigDecimal) {
  def applyCharge(charge: Charge): Try[OysterCard] = {
    val newBalance = balance - charge.amount
    if (newBalance < BigDecimal("0.00")) Failure(OysterCardBalanceTooLow)
    else Success(OysterCard(newBalance))
  }

  def load(amount: BigDecimal): OysterCard =
    OysterCard(amount + balance)
}

object OysterCardBalanceTooLow extends Exception("Balance is too low")

object OysterCard {
  def apply(): OysterCard = OysterCard(BigDecimal("0.00"))
  def apply(amount: BigDecimal): OysterCard = new OysterCard(amount)
}

case class Charge(amount: BigDecimal)
object ZeroCharge extends Charge(BigDecimal("0.00"))
