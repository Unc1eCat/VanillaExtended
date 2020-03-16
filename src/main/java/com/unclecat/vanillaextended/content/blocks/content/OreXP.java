package com.unclecat.vanillaextended.content.blocks.content;

import java.util.Random;

import com.unclecat.vanillaextended.content.blocks.MultiDimensionalOre;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class OreXP extends MultiDimensionalOre
{
	public OreXP() 
	{
		super("ore_xp", CreativeTabs.BUILDING_BLOCKS, Material.IRON, MapColor.STONE, 10, 20, true);
		setLightLevel(0.3f);
	}


	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos)
    {
        return (float) (Math.pow(128 - pos.getY(), 1.5) / 64);
    }
	
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune)
    {
        //return (int) ((1 + fortune) * Math.pow(70 - pos.getY(), 1.2) / 2);
		return (int) (  Math.pow(1 + fortune, 0.4)  *  ( -0.04320881 + 325.05152 * Math.pow(Math.E, -0.03793265 * pos.getY()) )  );	
    }
	
	public int quantityDropped(IBlockState state, int fortune, Random random)
    {
        return 0;
    } 
	protected ItemStack getSilkTouchDrop(IBlockState state)
   {
		switch (state.getValue(DIMENSION))
		{
		case END:
			return new ItemStack(Blocks.END_STONE, 1);
			
		case NETHER:
			return new ItemStack(Blocks.NETHERRACK, 1);
			
		case OVERWORLD:
			return new ItemStack(Blocks.COBBLESTONE, 1);
			
		default:
			return new ItemStack(Blocks.COBBLESTONE, 1);
		}
        
   } 
}
