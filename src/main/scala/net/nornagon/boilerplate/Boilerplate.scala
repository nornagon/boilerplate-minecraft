package net.nornagon.boilerplate

import cpw.mods.fml.client.registry.ClientRegistry
import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLServerStartingEvent}
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.TickEvent
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.common.{FMLCommonHandler, Mod}
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.minecraft.block.Block

import scala.collection.mutable

@Mod(modid = "boilerplate", version = "1.0", modLanguage = "scala") object Boilerplate {
  val nothingBlock = new PipeBlock(true)
  val thinSolidBlock = new PipeBlock(false)
  val posEngineBlock = new EngineBlock(1)
  val negEngineBlock = new EngineBlock(-1)
  val pipeWrench = new PipeWrench
  val shuttle = new ShuttleItem(thin = false)
  val thinShuttle = new ShuttleItem(thin = true)
  pipeWrench.setUnlocalizedName("bp_wrench")
  shuttle.setUnlocalizedName("bp_shuttle")
  thinShuttle.setUnlocalizedName("bp_thinshuttle")
  def addBlock(b: Block) = {
    GameRegistry.registerBlock(b, b.getUnlocalizedName)
  }
  @EventHandler def init(event: FMLInitializationEvent) {
    System.out.println("Boilerplate loaded.")
    addBlock(nothingBlock.setBlockTextureName("boilerplate:glass").setHardness(0.5f).setBlockName("nothing"))
    addBlock(thinSolidBlock.setBlockTextureName("boilerplate:glass_gray").setHardness(0.5f).setBlockName("thinsolid"))
    addBlock(posEngineBlock.setBlockTextureName("boilerplate:engine_pos").setHardness(0.5f).setBlockName("pos_engine"))
    addBlock(negEngineBlock.setBlockTextureName("boilerplate:engine_neg").setHardness(0.5f).setBlockName("neg_engine"))
    GameRegistry.registerTileEntity(classOf[EngineTile], "bp_engine")
    GameRegistry.registerTileEntity(classOf[PipeTile], "bp_pipe")
    GameRegistry.registerItem(shuttle.setTextureName("boilerplate:glass_magenta"), "bp_shuttle")
    GameRegistry.registerItem(thinShuttle.setTextureName("boilerplate:glass_magenta"), "bp_thinshuttle")
    GameRegistry.registerItem(pipeWrench.setTextureName("boilerplate:bp_wrench"), "bp_wrench")
  }
  @EventHandler def postInit(event: FMLPostInitializationEvent) {
    System.out.println("Boilerplate post init.")
    FMLCommonHandler.instance.bus.register(new TickHandler)
  }

  @EventHandler def onServerStart(event: FMLServerStartingEvent) = {
    println("Clearing boilerplate state")
    activeShuttles.clear()
    activeEngines.clear()
  }

  @EventHandler
  @SideOnly(Side.CLIENT)
  def registerClientThings(event: FMLInitializationEvent) = {
    val tesr = new PipeTESR
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[PipeTile], tesr)
  }

  private val activeShuttles = mutable.Set.empty[PipeTile]
  private val activeEngines = mutable.Set.empty[EngineTile]

  def unscheduleShuttleTick(t: PipeTile) = {
    activeShuttles -= t
  }
  def scheduleShuttleTick(t: PipeTile) = {
    activeShuttles += t
  }
  def scheduleEngineTick(e: EngineTile) = {
    activeEngines += e
  }

  def tickBoilerplate() = {
    for (e <- activeEngines) {
      e.tick()
    }
    for (s <- activeShuttles) {
      s.tick()
    }
    activeEngines.clear()
    activeShuttles.clear()
  }

  class TickHandler {
    var tickCounter = 0
    @SubscribeEvent
    def worldTick(evt: TickEvent.WorldTickEvent) {
      if (evt.phase == TickEvent.Phase.END && evt.side == Side.SERVER) {
        tickCounter += 1
        if (tickCounter > 20) {
          tickBoilerplate()
          tickCounter = 0
        }
      }
    }
  }
}