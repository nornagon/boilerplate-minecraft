package net.nornagon.boilerplate

import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
import net.minecraft.client.renderer.{RenderBlocks, RenderHelper, Tessellator}
import net.minecraft.init.Blocks
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import org.lwjgl.opengl.GL11

class PipeTESR extends TileEntitySpecialRenderer {
  var renderBlocks: RenderBlocks = null

  override def renderTileEntityAt(e: TileEntity, x: Double, y: Double, z: Double, g: Float): Unit = {
    e match {
      case pipeEntity: PipeTile =>
        if (!pipeEntity.shuttle.isDefined)
          return
      case _ =>
        return
    }
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
    //renderBlocks.setRenderBounds(0.1, 0.1, 0.1, 0.9, 0.9, 0.9)

    val shuttle = e.asInstanceOf[PipeTile].shuttle.get
    val (r, g, b) = if (shuttle.thin) {
      (216/255.0f, 135/255.0f, 248/255.0f)
    } else {
      (147/255.0f, 40/255.0f, 189/255.0f)
    }

    val world = e.getWorldObj
    val adjacent = (cardinals filter { case (dx, dy, dz) =>
      world.getTileEntity(e.xCoord + dx, e.yCoord + dy, e.zCoord + dz) match {
        case p: PipeTile => p.shuttle.isDefined
        case _ => false
      }
    }).toSet

    def lightForD(dx: Int, dy: Int, dz: Int) =
      if (dy == -1) { lightBottom }
      else if (dy == 1) { lightTop }
      else if (dz != 0) { lightEastWest }
      else { lightNorthSouth }

    for ((dx,dy,dz) <- cardinals) {
      if (!adjacent((dx,dy,dz))) {
        val light = lightForD(dx, dy, dz)
        tessellator.setColorOpaque_F(light * r, light * g, light * b)
        renderFace(x, y, z, dx, dy, dz, ((0.1,0.9), (0.1,0.9), (0.1,0.9)))
      } else {
        // adjacent shuttle, draw an extension
        for ((ex, ey, ez) <- cardinals; if (dx == 0) == (ex != 0) || (dy == 0) == (ey != 0) || (dz == 0) == (ey != 0)) {
          val light = lightForD(ex, ey, ez)
          tessellator.setColorOpaque_F(light * r, light * g, light * b)
          val renderBounds = (
            (if (dx == -1) 0 else if (dx == 1) 0.9 else 0.1, if (dx == -1) 0.1 else if (dx == 1) 1.0 else 0.9),
            (if (dy == -1) 0 else if (dy == 1) 0.9 else 0.1, if (dy == -1) 0.1 else if (dy == 1) 1.0 else 0.9),
            (if (dz == -1) 0 else if (dz == 1) 0.9 else 0.1, if (dz == -1) 0.1 else if (dz == 1) 1.0 else 0.9)
          )
          renderFace(x, y, z, ex, ey, ez, renderBounds)
        }
      }
    }

    /*
    tessellator.setColorOpaque_F(lightTop, lightTop, lightTop)
    renderBlocks.renderFaceYPos(block, x, y, z, block.getBlockTextureFromSide(1))

    tessellator.setColorOpaque_F(lightEastWest, lightEastWest, lightEastWest)
    renderBlocks.renderFaceZNeg(block, x, y, z, block.getBlockTextureFromSide(2))
    renderBlocks.renderFaceZPos(block, x, y, z, block.getBlockTextureFromSide(3))

    tessellator.setColorOpaque_F(lightNorthSouth, lightNorthSouth, lightNorthSouth)
    renderBlocks.renderFaceXNeg(block, x, y, z, block.getBlockTextureFromSide(4))
    renderBlocks.renderFaceXPos(block, x, y, z, block.getBlockTextureFromSide(5))
    */

    GL11.glDisable(GL11.GL_TEXTURE_2D)
    tessellator.draw
    GL11.glEnable(GL11.GL_TEXTURE_2D)

    RenderHelper.enableStandardItemLighting()
  }

  def renderFace(x: Double, y: Double, z: Double, dx: Int, dy: Int, dz: Int, renderBounds: ((Double, Double), (Double, Double), (Double, Double))) = {
    // Began life as RenderBlocks.renderFaceYNeg

    val ((renderMinX, renderMaxX), (renderMinY, renderMaxY), (renderMinZ, renderMaxZ)) = renderBounds

    val tessellator = Tessellator.instance

    /*
    val icon = Blocks.grass.getBlockTextureFromSide(0)
    var minu: Double = icon.getInterpolatedU(renderMinX * 16.0D).toDouble
    var maxu: Double = icon.getInterpolatedU(renderMaxX * 16.0D).toDouble
    var minv: Double = icon.getInterpolatedV(renderMinZ * 16.0D).toDouble
    var maxv: Double = icon.getInterpolatedV(renderMaxZ * 16.0D).toDouble

    if (renderMinX < 0.0D || renderMaxX > 1.0D) {
      minu = icon.getMinU.asInstanceOf[Double]
      maxu = icon.getMaxU.asInstanceOf[Double]
    }

    if (renderMinZ < 0.0D || renderMaxZ > 1.0D) {
      minv = icon.getMinV.asInstanceOf[Double]
      maxv = icon.getMaxV.asInstanceOf[Double]
    }
    */

    val minX = x + renderMinX
    val maxX = x + renderMaxX
    val minY = y + renderMinY
    val maxY = y + renderMaxY
    val minZ = z + renderMinZ
    val maxZ = z + renderMaxZ

    /*
    tessellator.addVertexWithUV(minx, miny, maxz, minu, maxv)
    tessellator.addVertexWithUV(minx, miny, minz, minu, minv)
    tessellator.addVertexWithUV(maxx, miny, minz, maxu, minv)
    tessellator.addVertexWithUV(maxx, miny, maxz, maxu, maxv)
    */

    if (dy == -1) {
      tessellator.addVertex(minX, minY, maxZ)
      tessellator.addVertex(minX, minY, minZ)
      tessellator.addVertex(maxX, minY, minZ)
      tessellator.addVertex(maxX, minY, maxZ)
    } else if (dy == 1) {
      tessellator.addVertex(maxX, maxY, maxZ)
      tessellator.addVertex(maxX, maxY, minZ)
      tessellator.addVertex(minX, maxY, minZ)
      tessellator.addVertex(minX, maxY, maxZ)
    } else if (dz == -1) {
      tessellator.addVertex(minX, maxY, minZ)
      tessellator.addVertex(maxX, maxY, minZ)
      tessellator.addVertex(maxX, minY, minZ)
      tessellator.addVertex(minX, minY, minZ)
    } else if (dz == 1) {
      tessellator.addVertex(minX, minY, maxZ)
      tessellator.addVertex(maxX, minY, maxZ)
      tessellator.addVertex(maxX, maxY, maxZ)
      tessellator.addVertex(minX, maxY, maxZ)
    } else if (dx == -1) {
      tessellator.addVertex(minX, maxY, maxZ)
      tessellator.addVertex(minX, maxY, minZ)
      tessellator.addVertex(minX, minY, minZ)
      tessellator.addVertex(minX, minY, maxZ)
    } else if (dx == 1) {
      tessellator.addVertex(maxX, minY, maxZ)
      tessellator.addVertex(maxX, minY, minZ)
      tessellator.addVertex(maxX, maxY, minZ)
      tessellator.addVertex(maxX, maxY, maxZ)
    }
  }

  override def func_147496_a(p_147496_1_ : World) {
    this.renderBlocks = new RenderBlocks(p_147496_1_)
  }
}
