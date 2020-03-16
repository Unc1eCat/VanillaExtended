package com.unclecat.vanillaextended.content.blocks;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.unclecat.vanillaextended.main.Main;
import com.unclecat.vanillaextended.utils.IHasModel;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;

public class BlockBase extends Block implements IHasModel
{
	//public List<IProperty> properties = new ArrayList<IProperty>();
	
	
	public BlockBase(String name, Material material, @Nullable boolean hasItem) 
	{
		super(material);
		
		setRegistryName(name);
		setUnlocalizedName(name);

		Main.BLOCKS.add(this);
		if (hasItem == true)
		{
			Main.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
		}
	}
	public BlockBase(String name, CreativeTabs creativeTab, SoundType soundType, Material material, MapColor mapColor, float hardness, float resistance, @Nullable boolean hasItem) 
	{
		this(name, creativeTab, material, mapColor, hardness, resistance, hasItem);
		setSoundType(soundType);
	}
	public BlockBase(String name, CreativeTabs creativeTab, Material material, @Nullable MapColor mapColor, float hardness, float resistance, @Nullable boolean hasItem) 
	{
		super(material, mapColor);
		
		setRegistryName(name);
		setUnlocalizedName(name);
		
		Main.BLOCKS.add(this);
		if (hasItem == true)
		{
			Main.ITEMS.add(new ItemBlock(this).setRegistryName(getRegistryName()));
		}
		
		setTypicalBlock(creativeTab, hardness, resistance);
	}
	
	
	public void setTypicalBlock(CreativeTabs creativeTab, float hardness, float resistance)
	{
		setCreativeTab(creativeTab);
		setHardness(hardness);
		this.setResistance(resistance);
	} 
	
	
	public static AxisAlignedBB createAABB(int x1, int y1, int z1, int x2, int y2, int z2)
	{
		return new AxisAlignedBB(x1 * 0.0625, y1 * 0.0625, z1 * 0.0625, x2 * 0.0625, y2 * 0.0625, z2 * 0.0625);
	}
	
	// Creates property
	public static PropertyBool createProp(String name, boolean defaultValue)
	{
		PropertyBool property = PropertyBool.create(name);
		//properties.add(property);
		//setDefaultState(getDefaultState().withProperty(property, defaultValue));
		return property;
	}
	public static PropertyInteger createProp(String name, int min, int max, int defaultValue)
	{
		PropertyInteger property = PropertyInteger.create(name, min, max);
		//properties.add(property);
		//setDefaultState(getDefaultState().withProperty(property, defaultValue));
		return property;
	}
	public static <T extends Enum<T> & IStringSerializable> PropertyEnum createProp(String name, Class<T> enumClass, T defaultValue)
	{
		PropertyEnum<T> property = PropertyEnum.<T>create(name, enumClass);
		//properties.add(property);
		//setDefaultState(getDefaultState().withProperty(property, defaultValue));
		return property;
	}
	public static PropertyDirection createProp(String name, @Nullable Predicate<EnumFacing> filter, EnumFacing defaultValue)
	{
		PropertyDirection property;
		if (filter != null)
		{
			property = PropertyDirection.create(name, filter);
		} else
		{
			property = PropertyDirection.create(name);
		}
		//properties.add(property);
		//setDefaultState(getDefaultState().withProperty(property, defaultValue));
		return property;
	}
	
	
	public <T extends Comparable<T>> void setDefaultValues(IProperty[] props, T[] values)
	{
		if (props.length != values.length)
		{
			FMLLog.bigWarning("Amount of properties isn't equal to amount of values", new Object());
		}
		
		for (int i = props.length - 1; i >= 0; i--)
		{
			setDefaultState(getDefaultState().withProperty(props[i], values[i]));
		}
	}	
	
	public <T extends Comparable<T>> void setDefaultValue(IProperty property, T value)
	{
		setDefaultState(getDefaultState().withProperty(property, value));
	}
	
	
	/* Converts pixels of block to double */
	public static double fromPx(int n)
	{
		return (double)n * 0.0625;
	}


	public static int getRedstonePower(World worldIn, BlockPos thisPos, EnumFacing facing)
	{
		IBlockState iblockstate = worldIn.getBlockState(thisPos.offset(facing));
        return Math.max(iblockstate.getBlock() == Blocks.REDSTONE_WIRE ? iblockstate.getValue(BlockRedstoneWire.POWER) : 0, worldIn.getRedstonePower(thisPos.offset(facing), facing));
	}
	
	public static int getRedstoneAllSidesPower(World worldIn, BlockPos pos)
	{
		int ret = 0;
		
		for (EnumFacing enumfacing : EnumFacing.values())
        {
			int i = getRedstonePower(worldIn, pos, enumfacing);
            if (i > ret)
            {
                ret = i;
            }
        }
		return ret;
	}

	
	public boolean isDownBlockSolid(World worldIn, BlockPos pos)
    {
		IBlockState downState = worldIn.getBlockState(pos.down());
        return super.canPlaceBlockAt(worldIn, pos) || BlockFaceShape.SOLID == downState.getBlockFaceShape(worldIn, pos.down(), EnumFacing.UP);
    }
	
	
	
	@Override
	public void registerModels() 
	{
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));	
	}
}
