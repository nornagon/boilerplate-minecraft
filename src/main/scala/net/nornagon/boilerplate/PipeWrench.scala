package net.nornagon.boilerplate

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.{ItemStack, Item}
import net.minecraft.world.World

class PipeWrench extends Item {
  setCreativeTab(CreativeTabs.tabTools)

  setFull3D()
  setMaxStackSize(1)
  setHarvestLevel("wrench", 0)

  override def onItemUseFirst(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
    /*val tile = world.getTileEntity(x, y, z)
    tile match {
      case pipe: PipeTile =>
        if (!world.isRemote) {
          pipe.hasShuttle = !pipe.hasShuttle
        }
        world.markBlockForUpdate(x, y, z)
        player.swingItem()
        return !world.isRemote
      case _ =>
    }*/
    false
  }
}