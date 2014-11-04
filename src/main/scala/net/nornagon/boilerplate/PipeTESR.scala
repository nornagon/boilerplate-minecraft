package net.nornagon.boilerplate

import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.client.renderer.{RenderHelper, RenderBlocks, Tessellator}
import net.minecraft.init.Blocks
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

class PipeTESR extends TileEntitySpecialRenderer {
  var renderBlocks: RenderBlocks = null

  override def renderTileEntityAt(e: TileEntity, x: Double, y: Double, z: Double, g: Float): Unit = {
    val tessellator: Tessellator = Tessellator.instance

    this.bindTexture(TextureMap.locationBlocksTexture)
    RenderHelper.disableStandardItemLighting()

    tessellator.startDrawingQuads()

    tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F)

    val block = Blocks.dirt

    // Cribbed from RenderBlocks.renderStandardBlockWithColorMultiplier
    val lightBottom = 0.5F
    val lightTop = 1.0F
    val lightEastWest = 0.8F
    val lightNorthSouth = 0.6F

    val brightness = block.getMixedBrightnessForBlock(e.getWorldObj, e.xCoord, e.yCoord, e.zCoord)

    tessellator.setBrightness(brightness)
    renderBlocks.setRenderBounds(0.1, 0.1, 0.1, 0.9, 0.9, 0.9)

    tessellator.setColorOpaque_F(lightBottom, lightBottom, lightBottom)
    renderBlocks.renderFaceYNeg(block, x, y, z, block.getBlockTextureFromSide(0))

    tessellator.setColorOpaque_F(lightTop, lightTop, lightTop)
    renderBlocks.renderFaceYPos(block, x, y, z, block.getBlockTextureFromSide(1))

    tessellator.setColorOpaque_F(lightEastWest, lightEastWest, lightEastWest)
    renderBlocks.renderFaceZNeg(block, x, y, z, block.getBlockTextureFromSide(2))
    renderBlocks.renderFaceZPos(block, x, y, z, block.getBlockTextureFromSide(3))

    tessellator.setColorOpaque_F(lightNorthSouth, lightNorthSouth, lightNorthSouth)
    renderBlocks.renderFaceXNeg(block, x, y, z, block.getBlockTextureFromSide(4))
    renderBlocks.renderFaceXPos(block, x, y, z, block.getBlockTextureFromSide(5))

    tessellator.draw

    RenderHelper.enableStandardItemLighting()
  }

  override def func_147496_a(p_147496_1_ : World) {
    this.renderBlocks = new RenderBlocks(p_147496_1_)
  }
}
