package com.unclecat.vanillaextended.content.blocks.content;

import javax.annotation.Nullable;

import com.unclecat.vanillaextended.content.blocks.BlockBase;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRedstoneEnhancer extends BlockBase
{
	public static PropertyDirection FACING = createProp("facing", EnumFacing.Plane.HORIZONTAL, EnumFacing.NORTH);
	public static PropertyInteger POWER = createProp("power", 0, 15, 0);
	public static PropertyBool SUBTRACT_MODE = createProp("subtract_mode", false);
	public static AxisAlignedBB THIS_AABB[] = 
	{
		createAABB(3, 0, 0, 13, 2, 16),
		createAABB(0, 0, 3, 16, 2, 13)
	};
	
	
	public BlockRedstoneEnhancer() 
	{
		super("redstone_enhancer", CreativeTabs.REDSTONE, SoundType.STONE, Material.CIRCUITS, null, 0.0f, 0.0f, true);
		
		setDefaultValue(FACING, EnumFacing.NORTH);
		setDefaultValue(SUBTRACT_MODE, false);
	}


	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
    {
		IBlockState state = getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
		state.withProperty(POWER, world.getStrongPower(pos, placer.getHorizontalFacing().getOpposite()));
		return state;
    }
	
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).getHorizontalIndex() + (state.getValue(SUBTRACT_MODE) ? 4 : 0);
	}
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta)).withProperty(SUBTRACT_MODE, (meta >> 2) >= 1);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, POWER, SUBTRACT_MODE});
    }
	
	@Override
	public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return side != EnumFacing.UP;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack)
	{
		if (canPlaceBlockAt(worldIn, pos))
		{
			neighborChanged(state, worldIn, pos, null, pos.offset(state.getValue(FACING)));
		}
	}

	
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
		IBlockState downState = worldIn.getBlockState(pos.down());
        return BlockFaceShape.SOLID == downState.getBlockFaceShape(worldIn, pos.down(), EnumFacing.UP);
    }
	
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
//        this.neighborChanged(state, worldIn, pos, null, pos.offset(state.getValue(FACING)));
    }
	
	
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
		if (!canPlaceBlockAt(worldIn, pos))
		{
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
		}
		
		EnumFacing f = state.getValue(FACING);
		
		if (getRedstonePower(worldIn, pos, f.rotateY()) > 0 || getRedstonePower(worldIn, pos, f.rotateYCCW()) > 0)
		{	
			state = state.withProperty(SUBTRACT_MODE, true);
		} else
		{
			state = state.withProperty(SUBTRACT_MODE, false);
		}
		worldIn.setBlockState(pos, state);
			
		int power = getRedstonePower(worldIn, pos, f);
		power = power <= 0 ? 0 : MathHelper.clamp(power + (state.getValue(SUBTRACT_MODE) ? -4 : 4), 0, 15);
		worldIn.setBlockState(pos, state.withProperty(POWER, power));
    }
	
	
	@Override
	public boolean canProvidePower(IBlockState state)
    {
        return true;
    }
	
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side)
    {
        return side != null ? side.getAxis() != EnumFacing.Axis.Y : false;
    }
	
	
	public boolean isOpaqueCube(IBlockState state)
	{
	    return false;
	}
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return state != null ? THIS_AABB[state.getValue(FACING).getHorizontalIndex() % 2] : THIS_AABB[0];
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
	
	public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        return EnumPushReaction.DESTROY;
    }
	
	@Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
		//if (Minecraft.getMinecraft().world.isRemote) return 0;
        if (side == blockState.getValue(FACING))
        {
        	return blockState.getValue(POWER);
        }
        return 0;
    }
}
