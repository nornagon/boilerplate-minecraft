package net.nornagon.boilerplate

import net.minecraft.block.Block

trait PhysicalProperties {
  def canAirPass: Boolean = false
  def canShuttlePass: Boolean = false
}

object PhysicalProperties {
  def canAirPass(b: Block): Boolean = {
    b match {
      case p: PhysicalProperties => p.canAirPass
      case _ => false
    }
  }
  
  def canShuttlePass(b: Block): Boolean = {
    b match {
      case p: PhysicalProperties => p.canShuttlePass
      case _ => false
    }
  }
}
