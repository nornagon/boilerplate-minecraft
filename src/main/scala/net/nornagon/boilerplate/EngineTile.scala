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
    bfsFrom((xCoord, yCoord, zCoord)) { case (x, y, z) =>
      if ((x == xCoord && y == yCoord && z == zCoord) || PhysicalProperties.canAirPass(worldObj, x, y, z)) {
        for ((dx, dy, dz) <- cardinals) {
          val tile = worldObj.getTileEntity(x + dx, y + dy, z + dz)
          tile match {
            case pipe: PipeTile if pipe.hasShuttle =>
              pipe.addPressure(dx * pressure, dy * pressure, dz * pressure)
            case _ =>
          }
        }
        true
      } else
        false
    }
  }
}