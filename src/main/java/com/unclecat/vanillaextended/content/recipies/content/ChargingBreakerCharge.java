package com.unclecat.vanillaextended.content.recipies.content;

import java.awt.GradientPaint;

import com.sun.pisces.GradientColorMap;
import com.unclecat.vanillaextended.content.items.content.ChargingBreaker;
import com.unclecat.vanillaextended.main.Main;
import com.unclecat.vanillaextended.utils.ModMath;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class ChargingBreakerCharge extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe
{
	public ChargingBreakerCharge()
	{
		setRegistryName("charging_breaker_charge");
	}
	
	

	@Override
	public boolean matches(InventoryCrafting inv, World worldIn)
	{
		ItemStack breaker = null;
		Block block = null;
		int blockCount = 0;
		int blockDamage = 0;
		
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack cur = inv.getStackInSlot(i);
			
			if (cur.getItem() == Main.CHARGING_BREAKER)
			{
				if (breaker == null) breaker = cur.copy();
				else return false;
			} 
			else if (cur.getItem() instanceof ItemBlock)
			{
				Block block2 = ((ItemBlock) cur.getItem()).getBlock();
				int blockDamage2 = cur.getItemDamage();
				
				if (block == null) 
				{
					block = block2;
					blockDamage = blockDamage2;
				}
				else if (block != block2 || blockDamage != blockDamage2) return false;
				blockCount++;
			} 
			else if (!cur.isEmpty()) return false;
		}
		
		if (breaker == null || block == null) return false;
		
		NBTTagCompound nbt = breaker.getTagCompound();
		System.out.println((nbt == null ? true : (nbt.getString("ChargeBlock").contentEquals(block.getRegistryName().toString()) && nbt.getInteger("ChargeBlockDamage") == blockDamage)));
		return (nbt == null ? true : (nbt.getInteger("ChargePower") + blockCount <= ChargingBreaker.MAX_CAHRGE && nbt.getString("ChargeBlock").contentEquals(block.getRegistryName().toString()) && nbt.getInteger("ChargeBlockDamage") == blockDamage));
	}
	

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		ItemStack breaker = null;
		ItemStack block = null;
		
		for (int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack cur = inv.getStackInSlot(i);
			
			if (cur.getItem() == Main.CHARGING_BREAKER)
			{
				breaker = cur.copy();
			}
			else if (cur.getItem() instanceof ItemBlock)
			{
				if (block == null)
				{
					block = cur.copy();
					block.setCount(1);
				}
				else block.grow(1);
			}
		}
		
		NBTTagCompound nbt = breaker.getTagCompound();
		if (nbt == null) // Write the block
		{
			nbt = new NBTTagCompound();
			nbt.setString("ChargeBlock", ((ItemBlock) block.getItem()).getBlock().getRegistryName().toString());
			nbt.setInteger("ChargeBlockDamage", block.getItemDamage());
			nbt.setInteger("ChargePower", block.getCount());
		} else // Override the block
		{
			nbt.setInteger("ChargePower", ModMath.clip(nbt.getInteger("ChargePower") + block.getCount(), 0, ChargingBreaker.MAX_CAHRGE)); // Maximum charge is 64 stacks of the item
		}
		
		breaker.setTagCompound(nbt);
		breaker.setCount(1);
		return breaker;
	}
	
	@Override
	public boolean isDynamic()
	{
		return true;
	}
	@Override
	public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv)
	{
        return net.minecraftforge.common.ForgeHooks.defaultRecipeGetRemainingItems(inv);
	}
	@Override
	public boolean canFit(int width, int height)
	{
		return width * height >= 2;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return ItemStack.EMPTY;
	}
	
}
