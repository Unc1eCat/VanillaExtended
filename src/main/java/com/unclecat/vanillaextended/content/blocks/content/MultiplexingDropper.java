package com.unclecat.vanillaextended.content.blocks.content;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.unclecat.vanillaextended.content.blocks.BlockBase;
import com.unclecat.vanillaextended.content.containers.content.MultiplexDropperContainer;
import com.unclecat.vanillaextended.content.gui.GuiHandler.GUIS;
import com.unclecat.vanillaextended.main.Main;
import com.unclecat.vanillaextended.main.References;
import com.unclecat.vanillaextended.utils.IHasModel;
import com.unclecat.vanillaextended.utils.helpers.ItemsManagementHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.items.VanillaInventoryCodeHooks;

public class MultiplexingDropper extends BlockContainer implements IHasModel
{
	public static final PropertyBool TRIGGERED = PropertyBool.create("triggered");
	public static final List<AxisAlignedBB> THIS_AABB = Arrays.asList(
			BlockBase.createAABB(0, 2, 0, 16, 16, 2), // north
			BlockBase.createAABB(0, 2, 2, 2, 16, 16), // west
			BlockBase.createAABB(2, 2, 13, 16, 16, 16), // south
			BlockBase.createAABB(13, 2, 2, 16, 16, 13), // east
			BlockBase.createAABB(3, 2, 3, 13, 12, 13), // center
			BlockBase.createAABB(5, 0, 5, 10, 3, 10)); // bottommost


	public MultiplexingDropper()
	{
		super(Material.ROCK, MapColor.STONE);
		
		setRegistryName(References.MOD_ID + ":multiplexing_dropper");
		setUnlocalizedName("multiplexing_dropper");
		setSoundType(SoundType.STONE);
		setDefaultState(getDefaultState().withProperty(TRIGGERED, false).withProperty(BlockDispenser.FACING, EnumFacing.NORTH));
		setHarvestLevel("pickaxe", 0);
		setHardness(3.5F);
		setCreativeTab(CreativeTabs.REDSTONE);
		
		Main.BLOCKS.add(this);
		Main.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	public boolean isFullCube(IBlockState state)
	{
		setHardness(3.5F);
		setHarvestLevel("pickaxe", 0);

		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, // TODO: Complete
			List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean isActualState)
	{
		super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, entityIn, isActualState);
//		collidingBoxes.addAll(Arrays.asList(
//			BlockBase.createAABB(0, 2, 0, 16, 16, 2), // north
//			BlockBase.createAABB(0, 2, 2, 2, 16, 16), // west
//			BlockBase.createAABB(2, 2, 13, 16, 16, 16), // south
//			BlockBase.createAABB(13, 2, 2, 16, 16, 13), // east
//			BlockBase.createAABB(3, 2, 3, 13, 12, 13), // center
//			BlockBase.createAABB(5, 0, 5, 10, 3, 10))); // bottommost
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new ThisTileEntity();
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer, EnumHand hand)
	{
		return getDefaultState().withProperty(BlockDispenser.FACING,placer.getHorizontalFacing().getOpposite());
	}
 
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		playerIn.openGui(Main.instance, GUIS.MULTIPLEXING_DROPPER.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileEntity te = worldIn.getTileEntity(pos);
		
		if (te instanceof ThisTileEntity)
		{
			InventoryHelper.dropInventoryItems(worldIn, pos, (ThisTileEntity) te);
			worldIn.updateComparatorOutputLevel(pos, this);
		}
		
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		if (worldIn.isRemote) return;
		if (worldIn.isBlockPowered(pos))
		{
			state = state.withProperty(TRIGGERED, true);
			
			((ThisTileEntity)worldIn.getTileEntity(pos)).tryDrop(worldIn, pos);
		} 
		else
		{
			state = state.withProperty(TRIGGERED, false);
		}
		
		worldIn.setBlockState(pos, state);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(BlockDispenser.FACING).ordinal() + ((state.getValue(TRIGGERED) ? 1 : 0) << 3);
	}
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState()
				.withProperty(BlockDispenser.FACING, EnumFacing.values()[meta & 0b00000111])
				.withProperty(TRIGGERED, (meta >> 3 >= 1 ? true : false));
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, BlockDispenser.FACING, TRIGGERED);
	}

	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, getRegistryName().toString(), "inventory");
	}
	
	
	
	
	
	
	public static class ThisTileEntity extends TileEntityLockable implements ISidedInventory, ITickable, IHopper // TODO: Make it save data
	{
		public NonNullList<ItemStack> slots = NonNullList.<ItemStack>withSize(10, ItemStack.EMPTY);
		private String customName = "";
		public Item predicateItem = null; // Item, damage and NBT

		public static final int[] HOPPER_SLOTS = { 0, 1, 2, 3, 4 };
		public static final int[] DROPPER_SLOTS = { 5, 6, 7, 8, 9 };
		public static final IBehaviorDispenseItem dropBehavior = new BehaviorDefaultDispenseItem();
		
		
		public ThisTileEntity()
		{
		}
		
		
		
		// Returns true if slots belongs to hopper part
		public static boolean doesSlotBelongToHopper(int index)
		{
			return index < 5;
		}
		// Returns true if slots belongs to hopper part
		public static boolean doesSlotBelongToDropper(int index)
		{
			return index >= 5 && index < 10;
		}
		
		@Override
		public void update()
		{	
			if (world.isRemote || world.getTotalWorldTime() % 4 != 0) return;
			
				sortOne();
				updateHopper();
		}
		
		private void updateHopper() // TODO: Add capability support
		{
			if (world.getTotalWorldTime() % 8 != 0) return;
			
			if (!isEmpty()) // Pushes out the bottom
			{
				IInventory pushInto = TileEntityHopper.getInventoryAtPosition(world, pos.getX(), pos.getY() - 1, pos.getZ());
				
				if (pushInto != null)
				{
					for (int i = 0; i < 5; i++) // Finds mergeable slots
					{
						ItemStack put = slots.get(i).copy();
						if (put.isEmpty()) continue; // The hopper-dropper doesn't have anything to merge
						put.setCount(1);
						
						ItemStack remainder = TileEntityHopper.putStackInInventoryAllSlots(this, pushInto, put, EnumFacing.UP);
						
						if (remainder.isEmpty())
						{
							slots.get(i).shrink(1);
							markDirty();
							break;
						}
					}
				}
			}
			
			if (!ItemsManagementHelper.isSidedInventoryFull(this, EnumFacing.UP)) // Pulls in from top
			{
				IInventory pullFrom = TileEntityHopper.getInventoryAtPosition(world, pos.getX(), pos.getY() + 1, pos.getZ());
				
				if (pullFrom == null) // Try pick items from world
				{
					for (EntityItem picked : TileEntityHopper.getCaptureItems(world, pos.getX(), pos.getY(), pos.getZ()))
					{
						ItemStack remainder = TileEntityHopper.putStackInInventoryAllSlots(null, this, picked.getItem(), EnumFacing.UP);
						
						if (remainder.isEmpty()) picked.setDead();
						else picked.setItem(remainder);
					}
				}
				else // Try pull items from the the inventory
				{
					if (pullFrom instanceof ISidedInventory)
					{
						int[] pullFromSlots = ((ISidedInventory)pullFrom).getSlotsForFace(EnumFacing.DOWN);
						
						for (int i : pullFromSlots)
						{
							ItemStack extracted = pullFrom.getStackInSlot(i).copy();
							if (extracted.isEmpty()) continue;
							extracted.setCount(1);
							
							if (((ISidedInventory) pullFrom).canExtractItem(i, extracted, EnumFacing.DOWN))
							{
								if (TileEntityHopper.putStackInInventoryAllSlots(null, this, extracted, EnumFacing.UP).isEmpty())
								{
									pullFrom.getStackInSlot(i).shrink(1);
									markDirty();
									break;
								}
							}	
						}
					}
					else
					{
						for (int i = 0; i < pullFrom.getSizeInventory(); i++)
						{
							ItemStack extracted = pullFrom.getStackInSlot(i).copy();
							if (extracted.isEmpty()) continue;
							extracted.setCount(1);
							
							if (TileEntityHopper.putStackInInventoryAllSlots(null, this, extracted, EnumFacing.UP).isEmpty())
							{
								pullFrom.getStackInSlot(i).shrink(1);
								markDirty();
								break;
							}
						}
					}
				}
			}
		}

		public void sortOne()
		{
			ItemStack stack = ItemStack.EMPTY;
			
			for (int i = 0; i < 5; i++)
			{
				stack = slots.get(i);
				if (!stack.isEmpty() && stack.getItem() == predicateItem) // If stack matches
				{
					break;
				}
			}
			
			if (stack.isEmpty()) return;
			
			for (int i = 5; i < 10; i++)
			{
				ItemStack tryPutIn = slots.get(i);
				
				if (tryPutIn.isEmpty()) // If trying to put in empty stack
				{
					slots.set(i, stack.splitStack(1));
					markDirty();
					break;
				} 
				else if (ItemsManagementHelper.areStackItemsEqual(tryPutIn, stack) && tryPutIn.getCount() < tryPutIn.getMaxStackSize()) // If trying to put in equal stack and it has space
				{
					tryPutIn.grow(1);
					stack.shrink(1);
					markDirty();
					break;
				}
				// Else continue searching another stack to put in
			}
		}
		
		// Returns true if dropped successfully
		public boolean tryDrop(World world, BlockPos pos)
		{	
			if (world.isRemote) return false;
			
			ItemStack toDrop = ItemStack.EMPTY;
			EnumFacing facing = world.getBlockState(pos).getValue(BlockDispenser.FACING);
			BlockPos insertInPos = pos.offset(facing);
			IInventory insertIn = TileEntityHopper.getInventoryAtPosition(world, insertInPos.getX(), insertInPos.getY(), insertInPos.getZ());
			ItemStack remainder = ItemStack.EMPTY; // TODO: Do something with it its unused
			
			int i = 5;
			for (; i < slots.size(); i++)
			{
				if (!slots.get(i).isEmpty())
				{
					toDrop = slots.get(i);
					break;
				}
			}
			
			if (toDrop == ItemStack.EMPTY) return false;
			
			if (insertIn == null)
			{
				BlockSourceImpl blocksourceimpl = new BlockSourceImpl(world, pos);
				remainder = dropBehavior.dispense(blocksourceimpl, toDrop); // Probably null pointer will be on facing	
			}
			else
			{
				remainder = TileEntityHopper.putStackInInventoryAllSlots(this, insertIn, toDrop.splitStack(1), facing);
				
				if (!remainder.isEmpty())
				{
					toDrop.grow(1);
					slots.set(i, toDrop);
				}
			}
			
			return true;
		}

		
		
		@Override
		public SPacketUpdateTileEntity getUpdatePacket()
		{
			return new SPacketUpdateTileEntity(pos, 1, writeToNBT(new NBTTagCompound()));
		}
		
		@Override
		public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
		{
			readFromNBT(pkt.getNbtCompound());
		}

		@Override
		public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
		{
			return oldState.getBlock() != newSate.getBlock();
		}
		
		@Override
		public NBTTagCompound getTileData()
		{
			return writeToNBT(super.getTileData());
		}
		
		
		
		@Override
		public int getSizeInventory()
		{
			return slots.size();
		}


		@Override
		public boolean isEmpty()
		{
			for (ItemStack s : slots) if (!s.isEmpty()) return false;
			return true;
		}


		@Override
		public ItemStack getStackInSlot(int index)
		{
			return slots.get(index);
		}


		@Override
		public ItemStack decrStackSize(int index, int count)
		{
			return slots.get(index).splitStack(count);
		}


		@Override
		public ItemStack removeStackFromSlot(int index)
		{
			return slots.set(index, ItemStack.EMPTY);
		}


		@Override
		public void setInventorySlotContents(int index, ItemStack stack)
		{
			slots.set(index, stack);
		}


		@Override
		public int getInventoryStackLimit()
		{
			return 64;
		}


		@Override
		public boolean isUsableByPlayer(EntityPlayer player)
		{
			return true;
		}


		@Override
		public void openInventory(EntityPlayer player)
		{		
		}


		@Override
		public void closeInventory(EntityPlayer player)
		{			
		}


		@Override
		public boolean isItemValidForSlot(int index, ItemStack stack)
		{
			return doesSlotBelongToHopper(index);
		}


		@Override
		public int getField(int id)
		{
			return 0;
		}


		@Override
		public void setField(int id, int value)
		{
			
		}


		@Override
		public int getFieldCount()
		{
			return 0;
		}


		@Override
		public void clear()
		{
			for (ItemStack i : slots) i = ItemStack.EMPTY;
		}


		@Override
		public String getName()
		{
			return hasCustomName() ? customName : "container.multiplexing_dropper";
		}


		@Override
		public boolean hasCustomName()
		{
			return customName.length() > 0;
		}


		@Override
		public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
		{
			return new MultiplexDropperContainer(playerIn, this);
		}

		@Override
		public NBTTagCompound writeToNBT(NBTTagCompound compound) // TODO: Make it store damage of the predicate data
		{
			compound = super.writeToNBT(compound);
			if (this.hasCustomName())
	        {
	            compound.setString("CustomName", customName);
	        }
			if (predicateItem != null) compound.setString("PredicateItem", predicateItem.getRegistryName().toString());
			return ItemStackHelper.saveAllItems(compound, slots);
		}
		
		@Override
		public void readFromNBT(NBTTagCompound compound)
		{
			super.readFromNBT(compound);
			ItemStackHelper.loadAllItems(compound, slots);
			if (compound.hasKey("CustomName", 8))
	        {
	            customName = compound.getString("CustomName");
	        }
			if (compound.hasKey("PredicateItem")) predicateItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(compound.getString("PredicateItem")));
		}
		
		@Override
		public String getGuiID()
		{
			return References.MOD_ID + ":multiplexingDropper";
		}


		@Override
		public int[] getSlotsForFace(EnumFacing side)
		{
			return side == world.getBlockState(pos).getValue(BlockDispenser.FACING) ? DROPPER_SLOTS : HOPPER_SLOTS;
		}


		@Override
		public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
		{
			return doesSlotBelongToHopper(index);
		}


		@Override
		public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
		{
			return (direction == EnumFacing.DOWN && doesSlotBelongToHopper(index)) || (direction != EnumFacing.DOWN && doesSlotBelongToDropper(index));
		}

		@Override
		public double getXPos()
		{
			return pos.getX();
		}

		@Override
		public double getYPos()
		{
			return pos.getY();
		}

		@Override
		public double getZPos()
		{
			return pos.getZ();
		}
	}
}
