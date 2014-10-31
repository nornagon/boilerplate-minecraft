package net.nornagon.boilerplate

import net.minecraft.block.{Block, BlockContainer}
import net.minecraft.block.material.Material
import net.minecraft.world.{IBlockAccess, World}
import net.minecraft.tileentity.TileEntity
import net.minecraft.creativetab.CreativeTabs
import cpw.mods.fml.relauncher.{Side, SideOnly}

class ShuttleBlock extends BlockContainer(Material.wood) {
  setCreativeTab(CreativeTabs.tabBlock)
  setBlockName("shuttle")

  def createNewTileEntity(p_149915_1_ : World, p_149915_2_ : Int): TileEntity = {
    new ShuttleTile
  }

  /**
   * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
   */
  @SideOnly(Side.CLIENT) override def getRenderBlockPass = 1

  /**
   * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
   */
  override def renderAsNormalBlock = false

  override def isOpaqueCube = false

  /**
   * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
   * coordinates.  Args: blockAccess, x, y, z, side
   */
  @SideOnly(Side.CLIENT) override def shouldSideBeRendered(blockAccess: IBlockAccess, x: Int, y: Int, z: Int, side: Int): Boolean = {
    val block: Block = blockAccess.getBlock(x, y, z)
    if (block eq this) {
      return false
    }
    true
  }

  override def onBlockAdded(world: World, x: Int, y: Int, z: Int) {
    super.onBlockAdded(world, x, y, z)
  }

}
