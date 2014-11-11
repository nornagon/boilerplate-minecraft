package net.nornagon.boilerplate

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity

import scala.collection.mutable

class PipeTile extends TileEntity with PhysicalProperties {
  var hasShuttle = false

  override def canAirPass: Boolean = !hasShuttle
  override def canShuttlePass: Boolean = {
    val canShuttlePass = worldObj.getBlock(xCoord, yCoord, zCoord).asInstanceOf[PipeBlock].canShuttlePass
    canShuttlePass && !hasShuttle
  }

  override def writeToNBT(tag: NBTTagCompound): Unit = {
    super.writeToNBT(tag)
    tag.setBoolean("hasShuttle", hasShuttle)
  }

  override def readFromNBT(tag: NBTTagCompound): Unit = {
    super.readFromNBT(tag)
    hasShuttle = tag.getBoolean("hasShuttle")
  }

  override def getDescriptionPacket = {
    val nbttagcompound = new NBTTagCompound()
    this.writeToNBT(nbttagcompound)
    new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 3, nbttagcompound)
  }

  override def onDataPacket(net: NetworkManager, pkt: S35PacketUpdateTileEntity): Unit = {
    readFromNBT(pkt.func_148857_g())
  }

  /// shuttle stuff
  var pressure = (0,0,0)
  def addPressure(dx: Int, dy: Int, dz: Int) = {
    assert(hasShuttle)
    pressure = (pressure._1 + dx, pressure._2 + dy, pressure._3 + dz)
    Boilerplate.scheduleShuttleTick(this)
  }

  def tick(): Unit = {
    var thisTickPressure = pressure
    val adjacentShuttles = mutable.Set((xCoord,yCoord,zCoord))
    bfsFrom(xCoord, yCoord, zCoord) { case c@(x,y,z) =>
      worldObj.getTileEntity(x, y, z) match {
        case s: PipeTile if s.hasShuttle =>
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
      if (!ss.contains((x + dx, y + dy, z + dz)) && !PhysicalProperties.canShuttlePass(worldObj, x+dx, y+dy, z+dz))
        return false
    }
    for ((x,y,z) <- ss) {
      worldObj.getTileEntity(x,y,z).asInstanceOf[PipeTile].hasShuttle = false
      worldObj.markBlockForUpdate(x,y,z)
    }
    for ((x,y,z) <- ss) {
      worldObj.getTileEntity(x + dx, y + dy, z + dz).asInstanceOf[PipeTile].hasShuttle = true
      worldObj.markBlockForUpdate(x + dx, y + dy, z + dz)
    }
    true
  }
}
