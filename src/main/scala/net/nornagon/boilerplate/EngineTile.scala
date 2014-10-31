package net.nornagon.boilerplate

import net.minecraft.tileentity.TileEntity

import scala.collection.mutable

class EngineTile extends TileEntity {
  type Coord = (Int, Int, Int)

  val cardinals = Seq(
    (1,0,0),
    (-1,0,0),
    (0,1,0),
    (0,-1,0),
    (0,0,1),
    (0,0,-1)
  )

  def bfsFrom(c: Coord)(cb: (Coord) => Boolean): Unit = {
    val visited = mutable.Set.empty[Coord]
    val toExplore = mutable.Queue[Coord](c)

    def hmm(to: Coord) = {
      if (!visited(to)) {
        visited += to
        toExplore.enqueue(to)
      }
    }

    while (toExplore.nonEmpty) {
      val to = toExplore.dequeue()
      if (cb(to)) {
        val (x, y, z) = to
        for ((dx, dy, dz) <- cardinals) {
          hmm((x+dx, y+dy, z+dz))
        }
      }
    }
  }

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
      if (block.isInstanceOf[PipeBlock] || (here._1 == xCoord && here._2 == yCoord && here._3 == zCoord)) {
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