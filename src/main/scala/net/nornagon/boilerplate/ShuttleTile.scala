package net.nornagon.boilerplate

import net.minecraft.tileentity.TileEntity

class ShuttleTile extends TileEntity {
  var pressure = (0,0,0)
  def addPressure(dx: Int, dy: Int, dz: Int) = {
    pressure = (pressure._1 + dx, pressure._2 + dy, pressure._3 + dz)
    Boilerplate.scheduleShuttleTick(this)
  }

  def tick(): Unit = {
    println(s"pressure $pressure")
    var moved = false
    if (pressure._1 != 0) {
      moved = tryMove(math.signum(pressure._1), 0, 0)
    }
    if (!moved && pressure._2 != 0) {
      moved = tryMove(0, math.signum(pressure._2), 0)
    }
    if (!moved && pressure._3 != 0) {
      tryMove(0, 0, math.signum(pressure._3))
    }
    pressure = (0,0,0)
  }

  def tryMove(dx: Int, dy: Int, dz: Int) = {
    val b = worldObj.getBlock(xCoord + dx, yCoord + dy, zCoord + dz)
    b match {
      case _: PipeBlock =>
        worldObj.setBlock(xCoord+dx, yCoord+dy, zCoord+dz, Boilerplate.shuttleBlock)
        worldObj.setBlock(xCoord, yCoord, zCoord, Boilerplate.pipeBlock)
        println(s"moved $dx, $dy, $dz")
        true
      case _ => false
    }
  }
}
