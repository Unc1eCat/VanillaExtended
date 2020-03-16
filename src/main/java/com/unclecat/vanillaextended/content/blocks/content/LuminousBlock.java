package com.unclecat.vanillaextended.content.blocks.content;

import java.util.Random;

import com.unclecat.vanillaextended.content.blocks.BlockBase;
import com.unclecat.vanillaextended.main.Main;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class LuminousBlock extends BlockBase
{

	public LuminousBlock()
	{
		super("luminous_block", CreativeTabs.DECORATIONS, SoundType.SNOW, Material.GLASS, null, 0.0f, 0.0f, true);
		
		setLightLevel(1);
	}

	

	@Override
	public int getLightValue(IBlockState state)
	{
		return 15;
	}
	public boolean isOpaqueCube(IBlockState state)
	{
	    return false;
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
    	return super.shouldSideBeRendered(blockState, blockAccess, pos, side) && Minecraft.getMinecraft().world.getBlockState(pos.offset(side)).getBlock() != Main.LUMINOUS_BLOCK;
    }
    
    @Override
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
    	if (Minecraft.getMinecraft().gameSettings.particleSetting >= 1) return;
		for (int i = 0; i < 4; i++)
		{
			worldIn.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, pos.getX() + rand.nextFloat(), pos.getY() + 0.2 + rand.nextFloat() * 0.8f, pos.getZ() + rand.nextFloat(), 0, 0, 0);
		}
    }
    
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
	{
		return NULL_AABB;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return FULL_BLOCK_AABB;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		worldIn.destroyBlock(pos, false);
		entityIn.motionX /= 3;
		entityIn.motionY /= 3;
		entityIn.motionZ /= 3;
	}
	
	@Override
	public boolean causesSuffocation(IBlockState state)
	{
		return false;
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
}
