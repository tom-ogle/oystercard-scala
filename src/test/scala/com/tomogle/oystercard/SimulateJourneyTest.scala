package com.tomogle.oystercard

import org.scalatest.FlatSpec
import org.scalatest.Matchers

import scala.util.Failure
import scala.util.Success

/**
  *
  */
class SimulateJourneyTest extends FlatSpec with Matchers {

  behavior of "SimulateJourneyTest"

  def testSimulateSuccessfulJourney(initialBalance: String, expectedBalance: String, events: Seq[JourneyEvent]): Unit = {
    val card = OysterCard().load(BigDecimal(initialBalance))
    val updatedCard = simulateJourney(events, card)(FunctionalChargingFunction())
    updatedCard should be(Success(OysterCard(BigDecimal(expectedBalance))))
  }

  def testSimulateFailureJourney(initialBalance: String, expectedFailure: Exception, events: Seq[JourneyEvent]): Unit = {
    val card = OysterCard().load(BigDecimal(initialBalance))
    val updatedCard = simulateJourney(events, card)(FunctionalChargingFunction())
    updatedCard should be(Failure(expectedFailure))
  }

  it should "simulate a single journey" in {
    val events = Seq(
        JourneyEvent(EnterStation, Holborn, Tube),
        JourneyEvent(ExitStation, EarlsCourt, Tube),
    )
    testSimulateSuccessfulJourney("10.00", "7.50", events)
  }

  it should "simulate being denied entry to a station" in {
    val events = Seq(
      JourneyEvent(EnterStation, Holborn, Tube),
      JourneyEvent(ExitStation, EarlsCourt, Tube),
    )
    testSimulateFailureJourney("3.19", OysterCardBalanceTooLow, events)
  }

  it should "simulate being denied entry in the second step of a journey station" in {
    val events = Seq(
      JourneyEvent(EnterStation, Holborn, Tube),
      JourneyEvent(ExitStation, EarlsCourt, Tube),
      JourneyEvent(EnterStation, Holborn, Tube),
      JourneyEvent(ExitStation, EarlsCourt, Tube),
    )
    testSimulateFailureJourney("4.19", OysterCardBalanceTooLow, events)
  }

  it should "simulate a user skipping the exit gates by double charging maximum fare" in {
    val events = Seq(
      JourneyEvent(EnterStation, Holborn, Tube),
      JourneyEvent(EnterStation, EarlsCourt, Tube),
    )
    testSimulateSuccessfulJourney("10.00", "3.60", events)
  }

  it should "simulate a user skipping the entry gates on their first journey charges max fare" in {
    val events = Seq(
      JourneyEvent(ExitStation, EarlsCourt, Tube),
    )
    testSimulateSuccessfulJourney("10.00", "6.80", events)
  }

  it should "simulate a user skipping the entry gates twice charges max fare twice" in {
    val events = Seq(
      JourneyEvent(ExitStation, Holborn, Tube),
      JourneyEvent(ExitStation, EarlsCourt, Tube),
    )
    testSimulateSuccessfulJourney("10.00", "3.60", events)
  }

}
