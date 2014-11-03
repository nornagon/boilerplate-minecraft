package net.nornagon.boilerplate

import net.minecraft.tileentity.TileEntity

class EngineTile extends TileEntity {
  override def updateEntity() = {
    super.updateEntity()
    if (!worldObj.isRemote) {
      Boilerplate.scheduleEngineTick(this)
    }
  }

  def tick(): Unit = {
    val block = worldObj.getBlock(xCoord, yCoord, zCoord)
    if (!block.isInstanceOf[EngineBlock]) return
    val pressure = block.asInstanceOf[EngineBlock].pressure
    bfsFrom((xCoord, yCoord, zCoord)) { here =>
      val block = worldObj.getBlock(here._1, here._2, here._3)
      if (PhysicalProperties.canAirPass(block) || (here._1 == xCoord && here._2 == yCoord && here._3 == zCoord)) {
        for ((dx, dy, dz) <- cardinals) {
          val tile = worldObj.getTileEntity(here._1 + dx, here._2 + dy, here._3 + dz)
          tile match {
            case shuttle: ShuttleTile =>
              shuttle.addPressure(dx * pressure, dy * pressure, dz * pressure)
            case _ =>
          }
        }
        true
      } else
        false
    }
  }
}