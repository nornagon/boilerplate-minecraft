package net.nornagon.boilerplate

import net.minecraft.tileentity.TileEntity
import scala.collection.mutable

class ShuttleTile extends TileEntity {
  var pressure = (0,0,0)
  def addPressure(dx: Int, dy: Int, dz: Int) = {
    pressure = (pressure._1 + dx, pressure._2 + dy, pressure._3 + dz)
    Boilerplate.scheduleShuttleTick(this)
  }

  def tick(): Unit = {
    var thisTickPressure = pressure
    val adjacentShuttles = mutable.Set((xCoord,yCoord,zCoord))
    bfsFrom(xCoord, yCoord, zCoord) { case c@(x,y,z) =>
      worldObj.getTileEntity(x, y, z) match {
        case s: ShuttleTile =>
          Boilerplate.unscheduleShuttleTick(s)
          thisTickPressure = (thisTickPressure._1 + s.pressure._1, thisTickPressure._2 + s.pressure._2, thisTickPressure._3 + s.pressure._3)
          s.pressure = (0, 0, 0)
          adjacentShuttles += c
          true
        case _ =>
          false
      }
    }
    var moved = false
    if (thisTickPressure._1 != 0) {
      moved = tryMove(adjacentShuttles, math.signum(thisTickPressure._1), 0, 0)
    }
    if (!moved && thisTickPressure._2 != 0) {
      moved = tryMove(adjacentShuttles, 0, math.signum(thisTickPressure._2), 0)
    }
    if (!moved && thisTickPressure._3 != 0) {
      tryMove(adjacentShuttles, 0, 0, math.signum(thisTickPressure._3))
    }
    pressure = (0,0,0)
  }

  def tryMove(ss: mutable.Set[Coord], dx: Int, dy: Int, dz: Int): Boolean = {
    for ((x,y,z) <- ss) {
      val b = worldObj.getBlock(x + dx, y + dy, z + dz)
      if (!ss.contains((x + dx, y + dy, z + dz)) && !b.isInstanceOf[PipeBlock])
        return false
    }
    for ((x,y,z) <- ss) {
      worldObj.setBlock(x,y,z, Boilerplate.pipeBlock)
    }
    for ((x,y,z) <- ss) {
      worldObj.setBlock(x+dx, y+dy, z+dz, Boilerplate.shuttleBlock)
    }
    true
  }
}
