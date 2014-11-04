package net.nornagon.boilerplate

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity

class PipeTile extends TileEntity {
  var hasShuttle = false

  override def writeToNBT(tag : NBTTagCompound): Unit = {
    super.writeToNBT(tag)
    tag.setBoolean("hasShuttle", hasShuttle)
  }

  override def readFromNBT(tag : NBTTagCompound): Unit = {
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
}
