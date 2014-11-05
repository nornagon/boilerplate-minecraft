package net.nornagon.boilerplate

import net.minecraft.world.IBlockAccess

trait PhysicalProperties {
  def canAirPass: Boolean = false
  def canShuttlePass: Boolean = false
}

object PhysicalProperties {
  def findPhysicalProperties(world: IBlockAccess, x: Int, y: Int, z: Int) = {
    world.getTileEntity(x, y, z) match {
      case p: PhysicalProperties => Some(p)
      case _ =>
        world.getBlock(x, y, z) match {
          case p: PhysicalProperties => Some(p)
          case _ => None
        }
    }
  }

  def canAirPass(world: IBlockAccess, x: Int, y: Int, z: Int): Boolean = {
    findPhysicalProperties(world, x, y, z) exists (_.canAirPass)
  }
  
  def canShuttlePass(world: IBlockAccess, x: Int, y: Int, z: Int): Boolean = {
    findPhysicalProperties(world, x, y, z) exists (_.canShuttlePass)
  }
}
