package com.unclecat.vanillaextended.content.items;

import java.util.Random;

import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

public class CustomUniversalTrade implements EntityVillager.ITradeList
{
	public ItemStack itemFromPlayer0;
	public int decreasementItemFromPlayer0;

	public ItemStack itemFromVillager;
	public int decreasementItemFromVillager;

	
	
	public CustomUniversalTrade(
			ItemStack itemFromPlayer0, int decreasementItemFromPlayer0,  
			ItemStack itemFromVillager, int decreasementItemFromVillager
			)
	{
		this.itemFromPlayer0 = itemFromPlayer0;
		this.decreasementItemFromPlayer0 = MathHelper.abs(decreasementItemFromPlayer0);
		
		this.itemFromVillager = itemFromVillager;
		this.decreasementItemFromVillager =  MathHelper.abs(decreasementItemFromVillager);
	}	
	
	
	
	public void register(String profession, int career, int level)
	{
		VillagerRegistry.VillagerProfession profInst = ForgeRegistries.VILLAGER_PROFESSIONS.getValue(new ResourceLocation(profession));
		register(profInst, career, level);
	}
	public void register(VillagerRegistry.VillagerProfession profession, int career, int level)
	{
		profession.getCareer(career).addTrade(level, this);
	}
	
	
	public ItemStack pickStack(ItemStack stackIn, int decreasement, Random random)
	{
		ItemStack stackOut = stackIn.copy();
		stackOut.shrink(random.nextInt(decreasement));
		return stackOut;
	}
	
	
	@Override
	public void addMerchantRecipe(IMerchant merchant, MerchantRecipeList recipeList, Random random)
	{
		recipeList.add(new MerchantRecipe( 
				pickStack(itemFromPlayer0, decreasementItemFromPlayer0, random),
				pickStack(itemFromVillager, decreasementItemFromVillager, random)
		));
	}

}

