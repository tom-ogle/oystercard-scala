package com.tomogle.oystercard

case object Zone1Only extends Station("zone 1 only", Set(Zone1))
case object Zone2Only extends Station("zone 2 only", Set(Zone2))
case object Zone3Only extends Station("zone 3 only", Set(Zone3))
case object Zones1And2 extends Station("zones 1 and 2", Set(Zone1, Zone2))
case object Zones2And3 extends Station("zones 2 and 3", Set(Zone2, Zone3))
case object Zones1And2And3 extends Station("zones 1, 2 and 3", Set(Zone1, Zone2, Zone3))
