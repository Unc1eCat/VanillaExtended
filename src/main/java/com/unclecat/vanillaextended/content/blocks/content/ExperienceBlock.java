package com.unclecat.vanillaextended.content.blocks.content;

import java.util.Random;

import com.unclecat.vanillaextended.content.blocks.BlockBase;
import com.unclecat.vanillaextended.content.blocks.DamageBlock;
import com.unclecat.vanillaextended.content.items.ItemBlockVariant;
import com.unclecat.vanillaextended.main.Main;
import com.unclecat.vanillaextended.main.References;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ExperienceBlock extends DamageBlock
{
	public static PropertyBool CONDENSED = createProp("condensed", false);
	

	public ExperienceBlock()
	{
		super("experience_block", CreativeTabs.BUILDING_BLOCKS, Material.GLASS, null, 0.1f, 0.0f, false);
		setSoundType(SoundType.GLASS);
		
		Main.ITEMS.add(new ItemBlockVariant(this, getRegistryName()));
		
		setDefaultValue(CONDENSED, false);
	}

	

	@Override
	public int getLightValue(IBlockState state)
	{
		return state.getValue(CONDENSED) ? 6 : 4;
	}
    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
    }
	
    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
    	if (itemIn == CreativeTabs.BUILDING_BLOCKS)
    	{
    		items.add(new ItemStack(this, 1, 0));
    		items.add(new ItemStack(this, 1, 1));
    	}
    		
    }
    
	@Override
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune)
	{
		return state.getValue(CONDENSED) ? 128 : 32;
	}
	
	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		return 0;
	}
	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		return false;
	}
	
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(CONDENSED) ? 1 : 0;
	}
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(CONDENSED, meta >= 1);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {CONDENSED});
	}
	
	@Override
	public void registerModels() 
	{
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 1, new ModelResourceLocation(getRegistryName().toString() + "_condensed", "inventory"));
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	@Override
	public String getDamageName(ItemStack stack, String nameIn)
	{
		return stack.getMetadata() >= 1 ? nameIn + "_condensed" : nameIn;
	}
}
