package com.unclecat.vanillaextended.content.blocks.content;

import java.util.Random;

import com.unclecat.vanillaextended.content.blocks.BlockBase;
import com.unclecat.vanillaextended.main.Main;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ForceBlock extends BlockBase
{

	public ForceBlock()
	{
		super("force_block", null, SoundType.STONE, Material.GLASS, null, Float.MAX_VALUE, Float.MAX_VALUE, true);
		
		setLightLevel(8);
		setBlockUnbreakable();
	}

	
	
	public boolean isOpaqueCube(IBlockState state)
	{
	    return false;
	}
    public boolean isFullCube(IBlockState state)
    {
        return true;
    }
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.TRANSLUCENT;
    }
	
    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
    	return super.shouldSideBeRendered(blockState, blockAccess, pos, side) && Minecraft.getMinecraft().world.getBlockState(pos.offset(side)).getBlock() != Main.LUMINOUS_BLOCK;
    }
	
	
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
    		EntityPlayer player) 
    {
    	return new ItemStack(Main.FORCE_BLOCK, 0);
    }
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		worldIn.updateBlockTick(pos, this, 14*20 + worldIn.rand.nextInt(80), -1);
		
		Random rand = worldIn.rand;
		for (int i = 0; i < 10; i++) worldIn.spawnParticle(EnumParticleTypes.SPELL_WITCH, (double)pos.getX() + rand.nextDouble() * 1.5, (double)pos.getY() + rand.nextDouble() * 1.5, (double)pos.getZ() + rand.nextDouble() * 1.5, 0, 0, 0);
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		worldIn.setBlockToAir(pos);

		for (int i = 0; i < 10; i++) worldIn.spawnParticle(EnumParticleTypes.CLOUD, (double)pos.getX() + rand.nextDouble(), (double)pos.getY() + rand.nextDouble()  +1, (double)pos.getZ() + rand.nextDouble(), 1, 1, 1);
	}
	
	@Override
	public void registerModels() 
	{

	}
}
