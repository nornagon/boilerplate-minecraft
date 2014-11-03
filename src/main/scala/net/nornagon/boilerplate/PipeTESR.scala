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
    tessellator.setTranslation(
      (x.toFloat - e.xCoord.toFloat + 0).toDouble,
      (y.toFloat - e.yCoord.toFloat + 0.4f).toDouble,
      (z.toFloat - e.zCoord.toFloat + 0).toDouble
    )

    tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F)

    this.renderBlocks.overrideBlockBounds(0.1, 0.1, 0.1, 0.9, 0.9, 0.9)
    this.renderBlocks.renderBlockAllFaces(Blocks.dirt, e.xCoord, e.yCoord, e.zCoord)
    this.renderBlocks.unlockBlockBounds()

    tessellator.setTranslation(0.0D, 0.0D, 0.0D)
    tessellator.draw

    RenderHelper.enableStandardItemLighting()
  }

  override def func_147496_a(p_147496_1_ : World) {
    this.renderBlocks = new RenderBlocks(p_147496_1_)
  }
}
