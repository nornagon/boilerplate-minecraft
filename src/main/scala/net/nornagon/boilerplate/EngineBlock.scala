package net.nornagon.boilerplate

import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.{IBlockAccess, World}

class EngineBlock(val pressure: Int) extends BlockContainer(Material.wood) {
  setCreativeTab(CreativeTabs.tabBlock)

  /**
   * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
   */
  //@SideOnly(Side.CLIENT) override def getRenderBlockPass = 1

  /**
   * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
   */
  override def renderAsNormalBlock = true

  override def isOpaqueCube = true

  /**
   * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
   * coordinates.  Args: blockAccess, x, y, z, side
   */
  @SideOnly(Side.CLIENT) override def shouldSideBeRendered(blockAccess: IBlockAccess, x: Int, y: Int, z: Int, side: Int): Boolean = {
    super.shouldSideBeRendered(blockAccess, x, y, z, side)
  }

  override def onBlockAdded(world: World, x: Int, y: Int, z: Int) {
    super.onBlockAdded(world, x, y, z)
  }

  def createNewTileEntity(world: World, arg: Int): TileEntity = new EngineTile
}