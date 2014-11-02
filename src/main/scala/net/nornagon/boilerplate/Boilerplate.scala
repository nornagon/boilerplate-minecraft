package net.nornagon.boilerplate

import cpw.mods.fml.common.Mod.EventHandler
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLServerStartingEvent}
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.TickEvent
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.common.{FMLCommonHandler, Mod}
import cpw.mods.fml.relauncher.Side
import net.minecraft.block.Block

import scala.collection.mutable

@Mod(modid = "boilerplate", version = "1.0", modLanguage = "scala") object Boilerplate {
  val pipeBlock = new PipeBlock
  val posEngineBlock = new EngineBlock(1)
  val negEngineBlock = new EngineBlock(-1)
  val shuttleBlock = new ShuttleBlock
  def addBlock(b: Block) = {
    GameRegistry.registerBlock(b, b.getUnlocalizedName)
  }
  @EventHandler def init(event: FMLInitializationEvent) {
    System.out.println("Boilerplate loaded.")
    addBlock(pipeBlock.setBlockTextureName("boilerplate:glass").setHardness(0.5f))
    addBlock(posEngineBlock.setBlockTextureName("boilerplate:glass_green").setHardness(0.5f).setBlockName("pos_engine"))
    addBlock(negEngineBlock.setBlockTextureName("boilerplate:glass_red").setHardness(0.5f).setBlockName("neg_engine"))
    addBlock(shuttleBlock.setBlockTextureName("boilerplate:glass_magenta").setHardness(0.5f))
    GameRegistry.registerTileEntity(classOf[EngineTile], "bp_engine")
    GameRegistry.registerTileEntity(classOf[ShuttleTile], "bp_shuttle")
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

  private val activeShuttles = mutable.Set.empty[ShuttleTile]
  private val activeEngines = mutable.Set.empty[EngineTile]

  def unscheduleShuttleTick(t: ShuttleTile) = {
    activeShuttles -= t
  }
  def scheduleShuttleTick(t: ShuttleTile) = {
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