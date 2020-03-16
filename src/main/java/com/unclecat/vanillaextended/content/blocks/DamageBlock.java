package com.unclecat.vanillaextended.content.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public abstract class DamageBlock extends BlockBase
{
	public DamageBlock(String name, CreativeTabs creativeTab, Material material, MapColor mapColor, float hardness,
			float resistance, boolean hasItem) 
	{
		super(name, creativeTab, material, mapColor, hardness, resistance, hasItem);
	}

	
	public abstract String getDamageName(ItemStack stack, String nameIn);
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        return new ItemStack(Item.getItemFromBlock(this), 1, damageDropped(state));
    }
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return getMetaFromState(state);
	}
}
