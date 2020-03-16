package com.unclecat.vanillaextended.content.blocks.content;

import java.util.Random;

import javax.annotation.Nullable;

import com.unclecat.vanillaextended.content.blocks.BlockBase;
import com.unclecat.vanillaextended.main.Main;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockOfRepeaters extends BlockBase implements ITileEntityProvider // Unstable
{
	public static final PropertyDirection FACING = createProp("facing", EnumFacing.Plane.HORIZONTAL, EnumFacing.NORTH);
	public static final PropertyInteger A_OFFSET = createProp("a", 1, 5, 0);
	public static final PropertyInteger B_OFFSET = createProp("b", 1, 5, 0);
	public static final PropertyBool DISABLE_A = createProp("dis_a", false);
	
	protected static final AxisAlignedBB THIS_AABB = createAABB(0, 0, 0, 16, 14, 16);
    protected final boolean isPoweredType;
    
	
	
	public BlockOfRepeaters(String name, boolean isPoweredType) 
	{
		super(name, CreativeTabs.REDSTONE, SoundType.WOOD, Material.CIRCUITS, null, 0.5f, 0, !isPoweredType);
		this.isPoweredType = isPoweredType;
		setDefaultValue(FACING, EnumFacing.NORTH);
		setDefaultValue(A_OFFSET, 1);
		setDefaultValue(B_OFFSET, 1);
		setDefaultValue(DISABLE_A, false);
	}
	
	

	public boolean isPoweredType()
	{
		return isPoweredType;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new ThisTileEntity();
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		ThisTileEntity te = (ThisTileEntity) worldIn.getTileEntity(pos);
		return state.withProperty(B_OFFSET, te.delayB).withProperty(A_OFFSET, te.delayA);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, A_OFFSET, B_OFFSET, DISABLE_A});
    }
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).getHorizontalIndex() 
				+ (state.getValue(DISABLE_A) ? 4 : 0);
	}
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return blockState.getBaseState()
				.withProperty(FACING, EnumFacing.getHorizontal(meta & 0x00000011))
				.withProperty(DISABLE_A, (meta & 4) >= 4);
	}
	
	public boolean isOpaqueCube(IBlockState state)
	{
	    return false;
	}
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return THIS_AABB;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    public boolean isNormalCube(IBlockState state)
    {
        return false;
    }
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
	
	@Override
	public int quantityDroppedWithBonus(int fortune, Random random)
	{
		return 9;
	}
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Items.REPEATER;
	}
	
	
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, net.minecraft.util.EnumFacing facing, float hitX,
			float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		EnumFacing f = placer.getHorizontalFacing().getOpposite();
		IBlockState state = getDefaultState().withProperty(FACING, f);
//		neighborChanged(state, world, pos, null, pos.offset(f.getOpposite()));
		return state;
    }
	
	public int getTickDelay(IBlockAccess worldIn, IBlockState state, BlockPos pos)
	{
		return ( (ThisTileEntity)worldIn.getTileEntity(pos) ).getTickDelay(state.getValue(DISABLE_A));
	}
	
	protected int calculateInputStrength(World worldIn, BlockPos pos, IBlockState state)
    {
        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING).getOpposite();
        BlockPos blockpos = pos.offset(enumfacing.getOpposite());
        int i = worldIn.getRedstonePower(blockpos, enumfacing.getOpposite());

        if (i >= 15)
        {
            return i;
        }
        else
        {
            IBlockState iblockstate = worldIn.getBlockState(blockpos);
            return Math.max(i, iblockstate.getBlock() == Blocks.REDSTONE_WIRE ? ((Integer)iblockstate.getValue(BlockRedstoneWire.POWER)).intValue() : 0);
        }
    }
	
	
	public boolean canProvidePower(IBlockState state)
    {
        return true;
    }
	
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side)
    {
        return side == state.getValue(FACING) || side == state.getValue(FACING).getOpposite();
    }
	
	
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return blockState.getWeakPower(blockAccess, pos, side);
    }
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        if (this.isPoweredType)
        {
        	 return blockState.getValue(FACING) == side ? 15 : 0;        
        }
        else
        {
           return 0;
        }
    }
    
   
    @Deprecated
    public boolean isSidesPoweredWithRepeater(World worldIn, IBlockState state, BlockPos pos)
    {
    	EnumFacing facing = state.getValue(FACING);
    	
    	// return worldIn.getStrongPower( pos.offset( facing.rotateY() ) ) > 0 
    	// 		|| worldIn.getStrongPower( pos.offset( facing.rotateYCCW() ) ) > 0;
    	
    	return worldIn.getBlockState(pos.offset(facing.rotateY())).getBlock().getClass() == Blocks.POWERED_REPEATER.getClass() 			
    			|| worldIn.getBlockState(pos.offset(facing.rotateYCCW())).getBlock().getClass() == Blocks.POWERED_REPEATER.getClass(); 			
    }
    	
    protected IBlockState getPoweredState(IBlockState unpoweredState)
    {
        EnumFacing enumfacing = (EnumFacing)unpoweredState.getValue(FACING);
        return Main.POWERED_BLOCK_OF_REPEATERS.getDefaultState().withProperty(FACING, enumfacing).withProperty(DISABLE_A, unpoweredState.getValue(DISABLE_A));
    }

    protected IBlockState getUnpoweredState(IBlockState poweredState)
    {
        EnumFacing enumfacing = (EnumFacing)poweredState.getValue(FACING);
        return Main.UNPOWERED_BLOCK_OF_REPEATERS.getDefaultState().withProperty(FACING, enumfacing).withProperty(DISABLE_A, poweredState.getValue(DISABLE_A));
    }
    
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
    	if (calculateInputStrength(worldIn, pos, state) <= 0 && isPoweredType)
    	{
    		worldIn.updateBlockTick(pos, this, getTickDelay(worldIn, state, pos), -1);
    	} 
    	else if (calculateInputStrength(worldIn, pos, state) > 0 && !isPoweredType)
    	{
    		worldIn.updateBlockTick(pos, this, getTickDelay(worldIn, state, pos), -1);
    	}
    	
    	boolean dis_a = false;
    	if (fromPos.equals(pos.offset(state.getValue(FACING).rotateY())))
    	{
    		dis_a = (worldIn.getBlockState(fromPos).getBlock() == Blocks.POWERED_REPEATER && worldIn.getBlockState(fromPos).getValue(Blocks.POWERED_REPEATER.FACING) == state.getValue(FACING).rotateY());
    		worldIn.setBlockState(pos, state.withProperty(DISABLE_A, dis_a));
    	} 
    	if (fromPos.equals(pos.offset(state.getValue(FACING).rotateYCCW())))
    	{
    		dis_a = dis_a || (worldIn.getBlockState(fromPos).getBlock() == Blocks.POWERED_REPEATER && worldIn.getBlockState(fromPos).getValue(Blocks.POWERED_REPEATER.FACING) == state.getValue(FACING).rotateYCCW());
    		worldIn.setBlockState(pos, state.withProperty(DISABLE_A, dis_a));
    	}
    }
	
    
    
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
    	int a = ((ThisTileEntity)worldIn.getTileEntity(pos)).delayA;
    	int b = ((ThisTileEntity)worldIn.getTileEntity(pos)).delayB;
        if (this.isPoweredType)
        {
        	worldIn.setBlockState(pos, getUnpoweredState(state));
        } else
        {
        	worldIn.setBlockState(pos, getPoweredState(state));
        }
        ThisTileEntity newTe = (ThisTileEntity)worldIn.getTileEntity(pos);
        newTe.delayA = a;
        newTe.delayB = b;
        newTe.markDirty();
        worldIn.notifyNeighborsOfStateChange(pos, this, true);
    }
    
	
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (facing == EnumFacing.UP && worldIn.getTileEntity(pos).getClass() == BlockOfRepeaters.ThisTileEntity.class)
        {
        	switch ((EnumFacing)state.getValue(FACING))
        	{
        	
        	case NORTH:
        		if (hitX > 0.5)
        		{
        			if (!state.getValue(DISABLE_A)) ((BlockOfRepeaters.ThisTileEntity)worldIn.getTileEntity(pos)).clickDelayA();
        		} else 
        		{
        			((BlockOfRepeaters.ThisTileEntity)worldIn.getTileEntity(pos)).clickDelayB();
        		}
        		break;
        		
        	case SOUTH:
        		if (hitX <= 0.5)
        		{
        			if (!state.getValue(DISABLE_A)) ((BlockOfRepeaters.ThisTileEntity)worldIn.getTileEntity(pos)).clickDelayA();
        		} else 
        		{
        			((BlockOfRepeaters.ThisTileEntity)worldIn.getTileEntity(pos)).clickDelayB();
        		}
        		break;
        		
        	case WEST:
        		if (hitZ < 0.5)
        		{
        			if (!state.getValue(DISABLE_A)) ((BlockOfRepeaters.ThisTileEntity)worldIn.getTileEntity(pos)).clickDelayA();
        		} else 
        		{
        			((BlockOfRepeaters.ThisTileEntity)worldIn.getTileEntity(pos)).clickDelayB();
        		}
        		break;
        		
        	case EAST:
        		if (hitZ >= 0.5)
        		{
        			if (!state.getValue(DISABLE_A)) ((BlockOfRepeaters.ThisTileEntity)worldIn.getTileEntity(pos)).clickDelayA();
        		} else 
        		{
        			((BlockOfRepeaters.ThisTileEntity)worldIn.getTileEntity(pos)).clickDelayB();
        		}
        		break;
        		
			default:
				break;
        	}
        	worldIn.markBlockRangeForRenderUpdate(pos, pos);
        }
        return true;
    }
	
	
	
	
	//======== TILE ENTITY ========//
	public static class ThisTileEntity extends TileEntity
	{
		public int delayA = 1;
		public int delayB = 1;
		
			
		
		public int getTickDelay(boolean propDisableA)
		{
			return propDisableA  ?  delayB  :  delayA * delayB * 4;
		}
		
		public void clickDelayA()
		{
			if (delayA >= 5) 
			{
				delayA = 1;
			}
			else
			{
				delayA++;
			}
		}
		public void clickDelayB()
		{
			if (delayB >= 5)
			{
				delayB = 1;
			}
			else
			{
				delayB++;
			}
		}
		
		@Override
		public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState)
		{
			return (newState.getBlock().getClass() != oldState.getBlock().getClass()); 
		} // Dammit they made a typo in superimpl parameter 
		
		@Override
		public SPacketUpdateTileEntity getUpdatePacket()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			writeToNBT(nbt);
			return new SPacketUpdateTileEntity(pos, getBlockMetadata(), nbt);
		}
		
		@Override
		public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
		{
			readFromNBT(pkt.getNbtCompound());
		}
		
		@Override
		public NBTTagCompound getUpdateTag()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			writeToNBT(nbt);
			return nbt;
		}
		
		@Override
		public NBTTagCompound getTileData()
		{
			NBTTagCompound nbt = new NBTTagCompound();
			writeToNBT(nbt);
			return nbt;
		}
		
		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound compound)
		{
			compound.setInteger("DelayA", delayA);
			compound.setInteger("DelayB", delayB);
			super.writeToNBT(compound);
			return compound;
		}
		@Override
		public void readFromNBT(NBTTagCompound compound)
		{
			delayA = compound.getInteger("DelayA");
			delayB = compound.getInteger("DelayB");
			super.readFromNBT(compound);
//			markDirty();
		}
	}
}







