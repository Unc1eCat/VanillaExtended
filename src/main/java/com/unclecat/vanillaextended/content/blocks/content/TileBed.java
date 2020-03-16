package com.unclecat.vanillaextended.content.blocks.content;

import javax.annotation.Nullable;

import com.unclecat.vanillaextended.content.blocks.BlockBase;

import net.minecraft.block.BlockBed;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;;

public class TileBed extends BlockBase
{
	public static final PropertyBool NORTH_BORDER = createProp("north_border", false);
	public static final PropertyBool SOUTH_BORDER = createProp("south_border", false);
	public static final PropertyBool WEST_BORDER = createProp("west_border", false);
	public static final PropertyBool EAST_BORDER = createProp("east_border", false);
	public static final PropertyDirection FACING = createProp("facing", EnumFacing.Plane.HORIZONTAL, EnumFacing.NORTH);
	public static final PropertyBool OCCUPIED = createProp("occupied", false);
	public static final PropertyBool PILLOW = createProp("pillow", false);
	
	public static final AxisAlignedBB THIS_AABB = createAABB(0, 0, 0, 16, 9, 16);
	
	
	
	public TileBed() 
	{
		super("tiling_bed", CreativeTabs.DECORATIONS, Material.CLOTH , MapColor.RED, 0.2f, 1, true);
		
		setSoundType(SoundType.WOOD);
		
		setDefaultValue(NORTH_BORDER, false);
		setDefaultValue(SOUTH_BORDER, false);
		setDefaultValue(WEST_BORDER, false);
		setDefaultValue(EAST_BORDER, false);
		setDefaultValue(FACING, EnumFacing.NORTH);
		setDefaultValue(OCCUPIED, false);
		setDefaultValue(PILLOW, false);
	}

	
	
	@Override
	public boolean isBed(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable Entity player)
	{
		if (world.getClass() != World.class)
		{
			return true;
		}
		return isTileBed((World)world, pos.offset(state.getValue(FACING)));
	}
	
	public void setBedOccupied(IBlockAccess world, BlockPos pos, EntityPlayer player, boolean occupied)
    {
        if (world instanceof World)
        {
            IBlockState state = world.getBlockState(pos);
            state = state.getBlock().getActualState(state, world, pos);
            state = state.withProperty(OCCUPIED, occupied);
            ((World)world).setBlockState(pos, state, 4);
        }
    }
	
	public EnumFacing getBedDirection(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return (EnumFacing)getActualState(state, world, pos).getValue(FACING);
    }
	
	 @Nullable
    private EntityPlayer getPlayerInBed(World worldIn, BlockPos pos)
    {
        for (EntityPlayer entityplayer : worldIn.playerEntities)
        {
            if (entityplayer.isPlayerSleeping() && entityplayer.bedLocation.equals(pos))
            {
                return entityplayer;
            }
        }

        return null;
    }
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) 
	{
		
	}
	
	
	public IBlockState calculateBorders(IBlockAccess worldIn , IBlockState state, BlockPos pos)
	{
		if (worldIn.getBlockState(pos.offset(EnumFacing.NORTH)).getBlock().getClass() == TileBed.class)
		{
			state = state.withProperty(NORTH_BORDER, false);
		} else
		{
			state = state.withProperty(NORTH_BORDER, true);
		}
		if (worldIn.getBlockState(pos.offset(EnumFacing.SOUTH)).getBlock().getClass() == TileBed.class)
		{
			state = state.withProperty(SOUTH_BORDER, false);
		} else
		{
			state = state.withProperty(SOUTH_BORDER, true);
		}
		if (worldIn.getBlockState(pos.offset(EnumFacing.WEST)).getBlock().getClass() == TileBed.class)
		{
			state = state.withProperty(WEST_BORDER, false);
		} else
		{
			state = state.withProperty(WEST_BORDER, true);
		}
		if (worldIn.getBlockState(pos.offset(EnumFacing.EAST)).getBlock().getClass() == TileBed.class)
		{
			state = state.withProperty(EAST_BORDER, false);
		} else
		{
			state = state.withProperty(EAST_BORDER, true);
		}
		return state;
	}
	
	public IBlockState calculatePillow(World worldIn , IBlockState state, BlockPos pos)
	{
		if ( isTileBed(worldIn, pos.offset( state.getValue(FACING) )) )
		{
			state = state.withProperty(PILLOW, false);
		} 
		else if (isTileBed(worldIn, pos.offset(state.getValue(FACING).getOpposite())))
		{
			worldIn.setBlockState( pos.offset(state.getValue(FACING).getOpposite()) , 
					worldIn.getBlockState(pos.offset(state.getValue(FACING).getOpposite())).withProperty(PILLOW, false));
			state = state.withProperty(PILLOW, true);
		} else
		{
			state = state.withProperty(PILLOW, false);
		}
		return state;
	}
	
	public void calculateBed(World worldIn, BlockPos pos, IBlockState state)
	{
		worldIn.setBlockState(pos, calculatePillow(worldIn, calculateBorders(worldIn, state, pos), pos));
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if (worldIn.isRemote)
        {
            return;
        }
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		state = state
			.withProperty(FACING, placer.getHorizontalFacing());
		for (EnumFacing f : EnumFacing.HORIZONTALS)
        {
        	if (isTileBed(worldIn, pos.offset(f)))
        	{
        		calculateBed(worldIn, pos.offset(f), worldIn.getBlockState(pos.offset(f)));
        	}
        }
		calculateBed(worldIn, pos, state);
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
		if (worldIn.isRemote)
        {
            return;
        }
        super.breakBlock(worldIn, pos, state);
        for (EnumFacing f : EnumFacing.HORIZONTALS)
        {
        	if (isTileBed(worldIn, pos.offset(f)))
        	{
        		calculateBed(worldIn, pos.offset(f), worldIn.getBlockState(pos.offset(f)));
        	}
        }
    }
	
	
	
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            net.minecraft.world.WorldProvider.WorldSleepResult sleepResult = worldIn.provider.canSleepAt(playerIn, pos);
            if (sleepResult != net.minecraft.world.WorldProvider.WorldSleepResult.BED_EXPLODES)
            {
                if (sleepResult == net.minecraft.world.WorldProvider.WorldSleepResult.DENY) return true;
                if (((Boolean)state.getValue(OCCUPIED)).booleanValue())
                {
                    EntityPlayer entityplayer = this.getPlayerInBed(worldIn, pos);

                    if (entityplayer != null && entityplayer != playerIn)
                    {
                        playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.occupied", new Object[0]), true);
                        return true;
                    }

                    state = state.withProperty(OCCUPIED, Boolean.valueOf(false));
                    worldIn.setBlockState(pos, state, 4);
                }

                EntityPlayer.SleepResult entityplayer$sleepresult = playerIn.trySleep(pos);

                if (entityplayer$sleepresult == EntityPlayer.SleepResult.OK && isBed(state, worldIn, pos, playerIn))
                {
                    state = state.withProperty(OCCUPIED, Boolean.valueOf(true));
                    worldIn.setBlockState(pos, state, 4);
                    return true;
                }
                else
                {
                    if (entityplayer$sleepresult == EntityPlayer.SleepResult.NOT_POSSIBLE_NOW)
                    {
                        playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.noSleep", new Object[0]), true);
                    } 
                    else if (!isBed(state, worldIn, pos, playerIn))
                    {
                        playerIn.sendStatusMessage(new TextComponentTranslation("tile.tiling_bed.need_more_tiles", new Object[0]), true);
                    }
                    else if (entityplayer$sleepresult == EntityPlayer.SleepResult.NOT_SAFE)
                    {
                        playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.notSafe", new Object[0]), true);
                    }
                    else if (entityplayer$sleepresult == EntityPlayer.SleepResult.TOO_FAR_AWAY)
                    {
                        playerIn.sendStatusMessage(new TextComponentTranslation("tile.bed.tooFarAway", new Object[0]), true);
                    }

                    return true;
                }
            }
            else
            {
                worldIn.setBlockToAir(pos);
                BlockPos blockpos = pos.offset(((EnumFacing)state.getValue(FACING)).getOpposite());

                if (worldIn.getBlockState(blockpos).getBlock() == this)
                {
                    worldIn.setBlockToAir(blockpos);
                }

                worldIn.newExplosion((Entity)null, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, 5.0F, true, true);
                return true;
            }
        }
    }
	
	
	
	@Override
	 public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	 {
		return calculateBorders(worldIn, state, pos);
	 }
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return blockState.getBaseState()
			.withProperty(FACING, EnumFacing.getHorizontal(meta & 0b00000011))
			.withProperty(PILLOW, (meta & 0b00000100) > 0);
	}
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(PILLOW) ? 0b00000100 : 0b00000000) | state.getValue(FACING).getHorizontalIndex();
	}
	
	
	@Override
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {NORTH_BORDER, SOUTH_BORDER, WEST_BORDER, EAST_BORDER, PILLOW, FACING, OCCUPIED});
    } 
	
	
	public boolean isTileBed(World worldIn ,BlockPos pos)
	{
		return worldIn.getBlockState(pos).getBlock().getClass() == TileBed.class;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return THIS_AABB;
	}
}








