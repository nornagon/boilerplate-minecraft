package net.nornagon.boilerplate

import net.minecraft.nbt.NBTTagCompound

object Shuttle {
  def fromNBT(t: NBTTagCompound) = {
    Shuttle(t.getBoolean("thin"))
  }
}

case class Shuttle(thin: Boolean) {
  def asNBT: NBTTagCompound = {
    val t = new NBTTagCompound
    t.setBoolean("thin", thin)
    t
  }
}
