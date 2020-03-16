package com.unclecat.vanillaextended.content.blocks.content;

import java.util.Random;

import javax.annotation.Nullable;

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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
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

public class BlockRedstoneGate extends BlockBase implements ITileEntityProvider {
	protected final boolean isPoweredType;
	/* Says if port on following side is inverted */
	public static final PropertyBool NORTH_INVERTED = createProp("north_inverted", false);
	public static final PropertyBool WEST_INVERTED = createProp("west_inverted", false);
	public static final PropertyBool SOUTH_INVERTED = createProp("south_inverted", false);
	public static final PropertyBool EAST_INVERTED = createProp("east_inverted", false);
	/* Will be saved in NBT but not in meta when unloaded on disk */ public static final PropertyDirection OUTPUT_SIDE = createProp(
			"output_side", EnumFacing.Plane.HORIZONTAL, EnumFacing.NORTH);
	protected static final AxisAlignedBB THIS_AABB = createAABB(0, 0, 0, 16, 2, 16);

	public BlockRedstoneGate(String name, boolean isPoweredType) {
		super(name, CreativeTabs.REDSTONE, SoundType.WOOD, Material.CIRCUITS, MapColor.GRAY, 0, 0, !isPoweredType);

		this.isPoweredType = isPoweredType;

		setDefaultValue(NORTH_INVERTED, false);
		setDefaultValue(WEST_INVERTED, false);
		setDefaultValue(SOUTH_INVERTED, false);
		setDefaultValue(EAST_INVERTED, false);
		setDefaultValue(OUTPUT_SIDE, EnumFacing.NORTH);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new ThisTileEntity();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this,
				new IProperty[] { NORTH_INVERTED, WEST_INVERTED, SOUTH_INVERTED, EAST_INVERTED, OUTPUT_SIDE });
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return (state.getValue(NORTH_INVERTED) ? 1 : 0) + (state.getValue(WEST_INVERTED) ? 2 : 0)
				+ (state.getValue(SOUTH_INVERTED) ? 4 : 0) + (state.getValue(EAST_INVERTED) ? 8 : 0);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return blockState.getBaseState().withProperty(NORTH_INVERTED, (meta & 1) > 0)
				.withProperty(WEST_INVERTED, (meta & 2) > 0).withProperty(SOUTH_INVERTED, (meta & 4) > 0)
				.withProperty(EAST_INVERTED, (meta & 8) > 0);
	}

	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {

	}

	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(OUTPUT_SIDE, ((ThisTileEntity) worldIn.getTileEntity(pos)).getOutputSide());
	}

	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return THIS_AABB;
	}

	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.DESTROY;
	}

	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		EnumFacing facing = placer.getHorizontalFacing();
		ThisTileEntity te = ((BlockRedstoneGate.ThisTileEntity) worldIn.getTileEntity(pos));

		te.setOutputSide(facing);
		worldIn.notifyBlockUpdate(pos, state, state, 2);
		neighborChanged(state, worldIn, pos, null, pos);
	}

	protected int calculateInputStrength(World worldIn, EnumFacing input, BlockPos pos, IBlockState state) {
		pos = pos.offset(input);
		int i = worldIn.getRedstonePower(pos, input);

		if (i >= 15) {
			return i;
		} else {
			IBlockState iblockstate = worldIn.getBlockState(pos);
			return Math.max(i,
					iblockstate.getBlock() == Blocks.REDSTONE_WIRE
							? ((Integer) iblockstate.getValue(BlockRedstoneWire.POWER)).intValue()
							: 0);
		}
	}

	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return getWeakPower(blockState, blockAccess, pos, side);
	}

	public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		System.out.println("GGGGG");
		if (this.isPoweredType) {
			return ((ThisTileEntity) blockAccess.getTileEntity(pos)).getOutputSide().getOpposite() == side ? 15 : 0;
		} else {
			return 0;
		}
	}

	protected IBlockState getPoweredState(IBlockState unpoweredState) {
		IBlockState poweredState = Main.POWERED_REDSTONE_GATE.getDefaultState();
		poweredState = poweredState.withProperty(NORTH_INVERTED, unpoweredState.getValue(NORTH_INVERTED));
		poweredState = poweredState.withProperty(WEST_INVERTED, unpoweredState.getValue(WEST_INVERTED));
		poweredState = poweredState.withProperty(SOUTH_INVERTED, unpoweredState.getValue(SOUTH_INVERTED));
		poweredState = poweredState.withProperty(EAST_INVERTED, unpoweredState.getValue(EAST_INVERTED));
//        poweredState = poweredState.withProperty(OUTPUT_SIDE, unpoweredState.getValue(OUTPUT_SIDE));
		return poweredState;
	}

	protected IBlockState getUnpoweredState(IBlockState poweredState) {
		IBlockState unpoweredState = Main.UNPOWERED_REDSTONE_GATE.getDefaultState();
		unpoweredState = unpoweredState.withProperty(NORTH_INVERTED, poweredState.getValue(NORTH_INVERTED));
		unpoweredState = unpoweredState.withProperty(WEST_INVERTED, poweredState.getValue(WEST_INVERTED));
		unpoweredState = unpoweredState.withProperty(SOUTH_INVERTED, poweredState.getValue(SOUTH_INVERTED));
		unpoweredState = unpoweredState.withProperty(EAST_INVERTED, poweredState.getValue(EAST_INVERTED));
//    	unpoweredState = unpoweredState.withProperty(OUTPUT_SIDE, poweredState.getValue(OUTPUT_SIDE));
		return unpoweredState;
	}

	public boolean isOutputInverted(IBlockState state, EnumFacing outputSide) {
		switch (outputSide) {
		case NORTH:
			return state.getValue(NORTH_INVERTED);

		case WEST:
			return state.getValue(WEST_INVERTED);

		case SOUTH:
			return state.getValue(SOUTH_INVERTED);

		case EAST:
			return state.getValue(EAST_INVERTED);

		default:
			return false;

		}
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		IBlockState thatState = worldIn.getBlockState(pos.offset(EnumFacing.DOWN));
		return thatState.getBlock().isTopSolid(thatState);
	}

	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (worldIn.isRemote)
			return;
		if (!canPlaceBlockAt(worldIn, pos)) {
			this.dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
		}

		boolean preOutput = false;
		ThisTileEntity te = (ThisTileEntity) worldIn.getTileEntity(pos);
		EnumFacing outputSide = te != null ? te.getOutputSide() : EnumFacing.UP;

		if (outputSide == EnumFacing.UP)
			return;

		if (outputSide != EnumFacing.NORTH && state
				.getValue(NORTH_INVERTED) != calculateInputStrength(worldIn, EnumFacing.NORTH, pos, state) > 0) {
			preOutput = true;
		} else if (outputSide != EnumFacing.WEST
				&& state.getValue(WEST_INVERTED) != calculateInputStrength(worldIn, EnumFacing.WEST, pos, state) > 0) {
			preOutput = true;
		} else if (outputSide != EnumFacing.SOUTH && state
				.getValue(SOUTH_INVERTED) != calculateInputStrength(worldIn, EnumFacing.SOUTH, pos, state) > 0) {
			preOutput = true;
		} else if (outputSide != EnumFacing.EAST
				&& state.getValue(EAST_INVERTED) != calculateInputStrength(worldIn, EnumFacing.EAST, pos, state) > 0) {
			preOutput = true;
		}

		if (isPoweredType != (preOutput != isOutputInverted(state, outputSide))) {
			worldIn.updateBlockTick(pos, this, 2, -1);
		}
	}

	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		EnumFacing outputSide = ((ThisTileEntity) worldIn.getTileEntity(pos)).getOutputSide();
		if (this.isPoweredType) {
			worldIn.setBlockState(pos, getUnpoweredState(state));// .withProperty(OUTPUT_SIDE, te.getOutputSide()));
		} else {
			worldIn.setBlockState(pos, getPoweredState(state));// .withProperty(OUTPUT_SIDE, te.getOutputSide()));
		}
		((ThisTileEntity) worldIn.getTileEntity(pos)).setOutputSide(outputSide);
		worldIn.notifyNeighborsOfStateChange(pos, this, true);
	}

	public @Nullable PropertyBool getClickedInverter(float hitX, float hitZ) {
//		System.out.println(hitX + "  " + hitZ);

		if (fromPx(3) < hitX && hitX < fromPx(9) && 0.0625 < hitZ && hitZ < fromPx(6)) {
//			System.out.println("NORTH_INVERTED");
			return NORTH_INVERTED;
		} else if (0.0625 < hitX && hitX < fromPx(6) && fromPx(3) < hitZ && hitZ < fromPx(9)) {
//			System.out.println("WEST_INVERTED");
			return WEST_INVERTED;
		} else if (fromPx(3) < hitX && hitX < fromPx(9) && 0.625 < hitZ && hitZ < fromPx(16)) {
//			System.out.println("SOUTH_INVERTED");
			return SOUTH_INVERTED;
		} else if (0.625 < hitX && hitX < fromPx(16) && fromPx(3) < hitZ && hitZ < fromPx(9)) {
//			System.out.println("EAST_INVERTED");
			return EAST_INVERTED;
		} else {
			return null;
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote && facing == EnumFacing.UP) {
			PropertyBool switched = getClickedInverter(hitX, hitZ);
			if (switched == null) {
				return true;
			} else {
				worldIn.playSound(null, pos, SoundEvent.REGISTRY.getObject(new ResourceLocation("block.lever.click")),
						SoundCategory.BLOCKS, 0.3f, state.getValue(switched) ? 0.5f : 0.6f);
				state = state.withProperty(switched, !state.getValue(switched));
				worldIn.setBlockState(pos, state);
				neighborChanged(state, worldIn, pos, null,
						pos.offset(((ThisTileEntity) worldIn.getTileEntity(pos)).getOutputSide()));
			}
		}
		return true;
	}

	@Override
	public void registerModels() {
		if (!isPoweredType) {
			super.registerModels();
		}
	}

	public static class ThisTileEntity extends TileEntity {
		public EnumFacing outputSide = EnumFacing.UP;

		public void setOutputSide(EnumFacing value) {
			this.outputSide = value;
			markDirty();
		}

		public EnumFacing getOutputSide() {
			return this.outputSide;
		}

		@Override
		public NBTTagCompound getUpdateTag() {
			return writeToNBT(new NBTTagCompound());
		}

		@Override
		public SPacketUpdateTileEntity getUpdatePacket() {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt = writeToNBT(nbt);
			return new SPacketUpdateTileEntity(pos, 1, nbt);
		}

		@Override
		public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
			readFromNBT(pkt.getNbtCompound());
		}

		@Override
		public NBTTagCompound getTileData() {
			NBTTagCompound nbt = new NBTTagCompound();
			nbt = writeToNBT(nbt);
			return nbt;
		}

		public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
			return (oldState.getBlock().getClass() != newState.getBlock().getClass());
		}

		public void readFromNBT(NBTTagCompound compound) {
			super.readFromNBT(compound);
			outputSide = EnumFacing.getHorizontal(compound.getInteger("outputSide"));
		}

		public NBTTagCompound writeToNBT(NBTTagCompound compound) {
			compound = super.writeToNBT(compound);
			compound.setInteger("outputSide", outputSide.getHorizontalIndex());
			return compound;
		}
	}
}
