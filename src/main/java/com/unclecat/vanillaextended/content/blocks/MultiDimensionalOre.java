package com.unclecat.vanillaextended.content.blocks;

import com.unclecat.vanillaextended.content.items.ItemBlockVariant;
import com.unclecat.vanillaextended.main.Main;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;

public class MultiDimensionalOre extends DamageBlock
{
	public static final PropertyEnum<Dimension> DIMENSION = createProp("dimension", Dimension.class, Dimension.OVERWORLD);

	
	
	public MultiDimensionalOre(String name, CreativeTabs creativeTab, Material material, MapColor mapColor,
			float hardness, float resistance, boolean hasItem) 
	{
		super(name, creativeTab, material, mapColor, hardness, resistance, false);
		setDefaultValue(DIMENSION, Dimension.OVERWORLD);
		
		if (hasItem == true)
		{
			Main.ITEMS.add(new ItemBlockVariant(this, getRegistryName()));
		}
	}
	
	
	
	@Override
	public int damageDropped(IBlockState state)
    {
        return ((Dimension)state.getValue(DIMENSION)).meta;
    }
	
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
		if (itemIn == this.getCreativeTabToDisplayOn())
		{
			items.add(new ItemStack(Item.getItemFromBlock(this), 1, 0));
			items.add(new ItemStack(Item.getItemFromBlock(this), 1, 1));
			items.add(new ItemStack(Item.getItemFromBlock(this), 1, 2));
		}
    }
	
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(DIMENSION).meta;
	}
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return blockState.getBaseState().withProperty(DIMENSION, Dimension.values()[meta]);
	}
	
	
	@Override
	public String getDamageName(ItemStack stack, String nameIn)
	{
		return ((Dimension)Dimension.values()[stack.getItemDamage()]).getDamageName(nameIn);
	}
	
		
	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, Dimension.OVERWORLD.getDamageName(this.getRegistryName().toString()), "inventory");						
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 1, Dimension.NETHER.getDamageName(this.getRegistryName().toString()), "inventory");						
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 2, Dimension.END.getDamageName(this.getRegistryName().toString()), "inventory");						
	}
	
	
	@Override
	protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {DIMENSION});
    }
	
	
	
	public enum Dimension implements IStringSerializable
	{
		OVERWORLD("overworld", 0),
		NETHER("nether", 1),
		END("end", 2);
		
		
		public String name;
		public int meta;
		
		
		Dimension(String name, int meta)
		{
			this.name = name;
			this.meta = meta;
		}
		
		
		public String getDamageName(String nameIn)
		{
			return nameIn + '_' + this.name;
		}
		
		@Override
		public String getName() 
		{
			return name;
		}
	}
}






