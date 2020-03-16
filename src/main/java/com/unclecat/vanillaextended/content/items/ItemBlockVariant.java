package com.unclecat.vanillaextended.content.items;

import javax.annotation.Nullable;

import com.unclecat.vanillaextended.content.blocks.DamageBlock;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class ItemBlockVariant extends ItemBlock
{

	public ItemBlockVariant(Block block, ResourceLocation name) 
	{
		super(block);
		setHasSubtypes(true);
		setMaxDamage(0);
		setRegistryName(name);
	}
	public ItemBlockVariant(Block block) 
	{
		super(block);
		setHasSubtypes(true);
		setMaxDamage(0);
		setRegistryName(block.getRegistryName());
	}
	
	
	@Override
	public int getMetadata(int damage)
	{
		return damage;
	}
	

	public String getUnlocalizedName(ItemStack stack)
	{
		return ((DamageBlock)this.block).getDamageName(stack, super.getUnlocalizedName());
	}
	

	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
		this.block.getSubBlocks(tab, items);
    }	
}
