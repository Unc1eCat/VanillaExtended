package com.unclecat.vanillaextended.content.items.content;

import com.unclecat.vanillaextended.main.Main;
import com.unclecat.vanillaextended.utils.IHasModel;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

public class Waffle extends ItemFood implements IHasModel
{
	public Waffle()
	{
		super(3, 1.2f, false);
		
		this.setUnlocalizedName("waffle");
		this.setRegistryName("waffle");
		
		Main.ITEMS.add(this);
	}

	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 18;
	}
	
	@Override
	public void registerModels() 
	{
		Main.proxy.registerItemRenderer(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));	
	}
}
