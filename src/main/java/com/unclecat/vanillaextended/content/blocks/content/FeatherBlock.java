package com.unclecat.vanillaextended.content.blocks.content;

import com.unclecat.vanillaextended.content.blocks.BlockBase;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class FeatherBlock extends BlockBase
{

	public FeatherBlock()
	{
		super("feather_block", CreativeTabs.BUILDING_BLOCKS, SoundType.CLOTH, Material.CLOTH, MapColor.CLOTH, 0.2f, 0.5f, true);
		
		
	}



}
