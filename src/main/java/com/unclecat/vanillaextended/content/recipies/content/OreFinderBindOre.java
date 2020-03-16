package com.unclecat.vanillaextended.content.recipies.content;

import com.unclecat.vanillaextended.main.Main;
import com.unclecat.vanillaextended.main.References;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class OreFinderBindOre extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{	
	public OreFinderBindOre()
	{
		setRegistryName(References.MOD_ID + ":ore_finder_bind_ore");	
	}
	

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn)
	{
		ItemStack oreFinder = null;
		ItemStack ore = null;
		
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack cur = inv.getStackInSlot(i);
			
			if (cur.getItem() == Main.ORE_FINDER)
			{
				if (oreFinder != null) return false;
				oreFinder = cur;
			} else if (cur.getItem() instanceof ItemBlock)
			{
				if (ore == null) ore = new ItemStack(cur.getItem(), 1, cur.getItemDamage());
				else if (ore.isItemEqual(cur)) ore.grow(1);
				else return false;
			} else 
			{
				return false;
			}
		}
		
		return ore.getCount() == 8 && oreFinder != null && oreFinder.getTagCompound() == null; 
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) // TODO: Make it work
	{
		ItemStack oreFinder = null;
		ItemBlock ore = null;	
		int oreDmage = 0;
		
		Main.LOGGER.info("Executed the line");
		
		for (int i = 0; i < inv.getSizeInventory(); i++) // Fetches ingredients
		{
			ItemStack cur = inv.getStackInSlot(i);
			
			if (cur.getItem() == Main.ORE_FINDER)
			{
				if (oreFinder != null) return ItemStack.EMPTY;
				oreFinder = cur.copy();
			} else if (cur.getItem() instanceof ItemBlock)
			{
				ore = (ItemBlock)cur.getItem();
				oreDmage = ore.getDamage(cur);
			}
		}
		
		NBTTagCompound nbt = oreFinder.getTagCompound();
		
		if (nbt == null || !nbt.hasKey("Ore"))
		{
			nbt = new NBTTagCompound();
			nbt.setString("Ore", ore.getBlock().getRegistryName().toString()); // Set the ore in the ore finder
			nbt.setInteger("OreDamage", oreDmage);
		}
		oreFinder.setTagCompound(nbt);
		
		oreFinder.setCount(1);
		
		return oreFinder;
	}

	@Override
	public boolean canFit(int width, int height)
	{	
		return width * height >= 9;
	}

	public boolean isDynamic()
    {
        return false;
    }
	
	@Override
	public ItemStack getRecipeOutput()
	{
		return ItemStack.EMPTY;
	}
	
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
    {
		return net.minecraftforge.common.ForgeHooks.defaultRecipeGetRemainingItems(inv);
	}
}
