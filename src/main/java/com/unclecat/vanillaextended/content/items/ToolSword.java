package com.unclecat.vanillaextended.content.items;

import com.unclecat.vanillaextended.main.Main;
import com.unclecat.vanillaextended.utils.IHasModel;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSword;

public class ToolSword extends ItemSword implements IHasModel 
{

	public ToolSword(String name, ToolMaterial material)
	{
		super(material);
		
		setRegistryName(name);
		setUnlocalizedName(name);
		
		Main.ITEMS.add(this);
	}
	
	public ToolSword(String name, CreativeTabs creativeTab, ToolMaterial material)
	{
		this(name, material);
		setTypicalItem(creativeTab);
	}

	public void setTypicalItem(CreativeTabs creativeTab)
	{
		setCreativeTab(creativeTab);
	}
	
	@Override
	public void registerModels() 
	{
		Main.proxy.registerItemRenderer(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));	
	}

}
