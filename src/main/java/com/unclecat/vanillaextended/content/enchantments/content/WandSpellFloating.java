package com.unclecat.vanillaextended.content.enchantments.content;

import java.util.Random;

import com.unclecat.vanillaextended.content.enchantments.WandSpellBase;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WandSpellFloating extends WandSpellBase
{

	public WandSpellFloating()
	{
		super("floating", 1, Rarity.UNCOMMON);
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
		return 60;
	}
	
	
	@Override
	public void onUsingTick(int lvl, EntityLivingBase player, EnumHand mainHand, ItemStack stack)
	{
		if (player instanceof EntityPlayer)
		{
			if (hasEnoughExpToCast((EntityPlayer)player, lvl))
			{
				consumeExpForCast((EntityPlayer)player, lvl);
			} else
			{
				return;
			}
		}
		
//		if (Minecraft.getMinecraft().world.isRemote) return;
		if (player.isSneaking())
		{
			player.motionY = -0.04;
		} else
		{
			player.motionY += 0.1;
		}
		player.fallDistance = (float) ((player.motionY < 0 ? -player.motionY : 0) * 10);
			
		Random rand = player.world.rand;
		
		player.world.spawnParticle(EnumParticleTypes.CLOUD, player.posX + rand.nextFloat() - 0.5, player.posY + rand.nextFloat() * 0.5 - 0.3, player.posZ + rand.nextFloat() - 0.5, 0, -0.1, 0);
		player.world.spawnParticle(EnumParticleTypes.CLOUD, player.posX + rand.nextFloat() - 0.5, player.posY + rand.nextFloat() * 0.5 - 0.3, player.posZ + rand.nextFloat() - 0.5, 0, -0.3, 0);
	}
}
