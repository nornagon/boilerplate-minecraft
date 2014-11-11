package net.nornagon.boilerplate

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item

class ShuttleItem(val thin: Boolean) extends Item {
  setCreativeTab(CreativeTabs.tabMaterials)

  setFull3D()
}
