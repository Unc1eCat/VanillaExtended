package com.unclecat.vanillaextended.content.enchantments.content;

import com.unclecat.vanillaextended.content.enchantments.WandSpellBase;
import com.unclecat.vanillaextended.utils.ModMath;

import it.unimi.dsi.fastutil.ints.Int2IntSortedMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class WandSpellQuantumPotion extends WandSpellBase
{

	public WandSpellQuantumPotion()
	{
		super("quantum_potion", 80, Rarity.VERY_RARE);
	}
	
	
	
	@Override
	public int getMaxLevel()
	{
		return 1;
	}
	@Override
	public int getMinLevel()
	{
		return 1;
	}
	
	@Override
	public int getUsingDuration(ItemStack stack, int lvl)
	{
		return 18;
	}
	
	
	@Override
	public ItemStack onItemUseFinish(int lvl, ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		ItemStack potionStack = entityLiving.getHeldItemOffhand();
		int totalExpToConsume = 0;
		
		if (potionStack.getItem() instanceof ItemPotion)
		{
			if (entityLiving instanceof EntityPlayer && !((EntityPlayer) entityLiving).capabilities.isCreativeMode)
			{
				for (PotionEffect i : PotionUtils.getEffectsFromStack(potionStack))
				{
					if (i.getPotion().isInstant()) totalExpToConsume += (i.getAmplifier() + 1) * 50 * (i.getPotion().isBadEffect() ? 1 : 2);
					else totalExpToConsume += (i.getAmplifier() + 1) * i.getDuration() / 20 * (i.getPotion().isBadEffect() ? 1 : 2);
					
					entityLiving.sendMessage(new TextComponentString(Integer.toString(totalExpToConsume)));
				}
				
				if (((EntityPlayer)entityLiving).experienceTotal >= totalExpToConsume) ModMath.decreaseExp((EntityPlayer) entityLiving, totalExpToConsume);
				else return stack;		 
			}
			
			
			for (PotionEffect i : PotionUtils.getEffectsFromStack(potionStack))
			{
				if (i.getPotion().isInstant()) i.getPotion().affectEntity(entityLiving, entityLiving, entityLiving, i.getAmplifier(), 1.0D);
				else entityLiving.addPotionEffect(new PotionEffect(i));
			}			
		}
				
		return stack;
	}
}