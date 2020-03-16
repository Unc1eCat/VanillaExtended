package com.unclecat.vanillaextended.content.blocks.content;

import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.unclecat.vanillaextended.content.blocks.BlockBase;
import com.unclecat.vanillaextended.main.Main;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.network.ForgeMessage;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class BlockRedstoneSensor extends BlockBase
{
	// Side of block on which this sensor was placed
	public static final PropertyDirection FACING = createProp("facing", (Predicate<EnumFacing>)null, EnumFacing.NORTH);
	public static final PropertyBool POWERED = createProp("powered", false);
	
	protected static final AxisAlignedBB THIS_AABB[] = 
	{
		createAABB(16, 16, 16, 0, 14, 0), 	// Up
		createAABB(0, 0, 0, 16, 2, 16), 	// Down
		createAABB(16, 16, 16, 0, 0, 14), 	// South
		createAABB(0, 0, 0, 16, 16, 2), 	// North
		createAABB(16, 16, 16, 14, 0, 0), 	// East
		createAABB(0, 0, 0, 2, 16, 16), 	// West
	};
    
	
	
	public BlockRedstoneSensor() 
	{
		super("redstone_sensor", CreativeTabs.REDSTONE, SoundType.STONE, Material.CIRCUITS, MapColor.GRAY, 0, 0, true);
		
		setDefaultValue(FACING, EnumFacing.NORTH);
		setDefaultValue(POWERED, false);
	}
	
	
	
	@Override
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, POWERED});
    }
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).ordinal() + (state.getValue(POWERED) ? 8 : 0);
	}
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(FACING, EnumFacing.VALUES[meta & 7]).withProperty(POWERED, meta >> 3 >= 1);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		return getDefaultState().withProperty(FACING, facing); // Facing inverted
	}	
	
	public boolean isOpaqueCube(IBlockState state)
	{
	    return false;
	}
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return THIS_AABB[state.getValue(FACING).ordinal()];
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
	
	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state)
	{
		return EnumPushReaction.DESTROY;
	}
	
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
		neighborChanged(state, worldIn, pos, null, pos);
    }
	
	public boolean canProvidePower(IBlockState state)
    {
        return true;
    }
	
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return getWeakPower(blockState, blockAccess, pos, side);
    }
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
    	return blockState.getValue(POWERED) ? 8 : 0;
    }
    
    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
    	worldIn.setBlockState(pos, state.withProperty(POWERED, true));
    }
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote && facing == state.getValue(FACING))
        {
        	worldIn.setBlockState(pos, state.withProperty(POWERED, !state.getValue(POWERED)));
        }
        return true;
    }
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		if (getRedstoneAllSidesPower(worldIn, pos) > 8)
		{
			worldIn.setBlockState(pos, state.withProperty(POWERED, false));
		}
	}
}
