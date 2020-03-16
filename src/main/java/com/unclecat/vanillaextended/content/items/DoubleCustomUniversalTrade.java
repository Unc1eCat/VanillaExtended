package com.unclecat.vanillaextended.content.items;

import java.util.Random;

import net.minecraft.entity.IMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class DoubleCustomUniversalTrade extends CustomUniversalTrade
{
	public ItemStack itemFromPlayer1;
	public int decreasementItemFromPlayer1;

	
	
	public DoubleCustomUniversalTrade(
			ItemStack itemFromPlayer0, int decreasementItemFromPlayer0, 
			ItemStack itemFromPlayer1, int decreasementItemFromPlayer1, 
			ItemStack itemFromVillager, int decreasementItemFromVillager
			)
	{
		super(itemFromPlayer0, decreasementItemFromPlayer0, itemFromVillager, decreasementItemFromVillager);
		
		this.itemFromPlayer1 = itemFromPlayer1;
		this.decreasementItemFromPlayer1 =  MathHelper.abs(decreasementItemFromPlayer1);
		int i = -0;
	}	
	
	
	
	@Override
	public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
	{
		recipeList.add(new MerchantRecipe( 
				pickStack(itemFromPlayer0, decreasementItemFromPlayer0, random),
				pickStack(itemFromPlayer1, decreasementItemFromPlayer1, random),
				pickStack(itemFromVillager, decreasementItemFromVillager, random)
		));
	}

}
