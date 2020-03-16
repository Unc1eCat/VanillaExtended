package com.unclecat.vanillaextended.content.blocks.content;

import java.util.Random;

import com.unclecat.vanillaextended.content.blocks.BlockBase;
import com.unclecat.vanillaextended.main.Main;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WaffleBlock extends BlockBase
{
	public static final PropertyInteger BITES = createProp("bites", 0, 3, 0); // Waffle block has 4 bites to bite
	public static final AxisAlignedBB[] THIS_AABB = {
		FULL_BLOCK_AABB, 
		createAABB(0, 0, 0, 12, 16, 16), 
		createAABB(0, 0, 0, 8, 16, 16), 
		createAABB(0, 0, 0, 4, 16, 16) 
		};
	
	
	
	public WaffleBlock()
	{
		super("waffle_block", CreativeTabs.FOOD, SoundType.WOOD, Material.CAKE, MapColor.YELLOW_STAINED_HARDENED_CLAY, 0.2F, 0.0f, true);
		
		setDefaultValue(BITES, 0);
	}
	
	
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 30;
	}
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		return 18;
	}
	
	
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return THIS_AABB[state.getValue(BITES)];
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return state.getValue(BITES) <= 0;
	}
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return isOpaqueCube(state);
	}
	public boolean isNormalCube(IBlockState state)
    {
        return isOpaqueCube(state);
    }
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
	
	
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Main.WAFFLE;
	}
	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		return 3 - state.getValue(BITES);
	}
	
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(BITES);
	}
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(BITES, meta);
	}

	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack itemstack = playerIn.getHeldItem(hand);
		if (itemstack.getItem() instanceof ItemAxe)
		{
			if (state.getValue(BITES) >= 3)
			{
				worldIn.setBlockToAir(pos);
			} else
			{
				worldIn.setBlockState(pos, state.withProperty(BITES, state.getValue(BITES) + 1), 2);
			}
			if (Minecraft.getMinecraft().world.rand.nextInt(3) >= 1)
			{
				this.spawnAsEntity(worldIn, pos.add(0.5, 0, 0), new ItemStack(Main.WAFFLE, 1));
			}
			return true;
		} else
		{
			return eat(worldIn, pos, state, playerIn) || itemstack.isEmpty();
		}
	}
	
	public boolean eat(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
	{
		if (!player.canEat(false))
		{
			return false;
		}
		
		player.getFoodStats().addStats(4, 1.4f);
		
		if (state.getValue(BITES) >= 3)
		{
			worldIn.setBlockToAir(pos);
		} else
		{
			worldIn.setBlockState(pos, state.withProperty(BITES, state.getValue(BITES) + 1), 3);
		}
		return true;
	}
	
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {BITES});
	}
	
	
	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));	
	}
}
