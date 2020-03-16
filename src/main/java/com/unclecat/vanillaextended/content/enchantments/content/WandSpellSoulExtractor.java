package com.unclecat.vanillaextended.content.enchantments.content;

import com.unclecat.vanillaextended.content.enchantments.WandSpellBase;
import com.unclecat.vanillaextended.content.items.ItemBase;
import com.unclecat.vanillaextended.utils.ModNetWrapper;
import com.unclecat.vanillaextended.utils.messages.MessageMagicAttackEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class WandSpellSoulExtractor extends WandSpellBase
{

	public WandSpellSoulExtractor()
	{
		super("soul_extractor", 25, Rarity.RARE);
	}
	
	
	
	@Override
	public int getMaxLevel()
	{
		return 3;
	}
	@Override
	public int getMinLevel()
	{
		return 1;
	}
	
	@Override
	public int getUsingDuration(ItemStack stack, int lvl)
	{
		return 20;
	}
	
	
	@Override
	public ItemStack onItemUseFinish(int lvl, ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{		
		if (entityLiving instanceof EntityPlayer) // TODO: Check if EntityPlayer is the only player class
		{
			if (hasEnoughExpToCast((EntityPlayer)entityLiving, lvl))
			{
				consumeExpForCast((EntityPlayer)entityLiving, lvl);
			} else
			{
				return stack;
			}
		}
		
		Entity entity = ItemBase.getMouseOver(0, -1);
		
		if (entity != null && entity instanceof EntityLiving)
		{
			if (worldIn.isRemote)
			{
				ModNetWrapper.WRAPPER.sendToServer(new MessageMagicAttackEntity(entity.getEntityId(), 2.0f));
				for (int i = 0; i < 40; i++) worldIn.spawnParticle(EnumParticleTypes.DAMAGE_INDICATOR, entity.posX - 0.6 + worldIn.rand.nextDouble() * 1.2, entity.posY + worldIn.rand.nextDouble() * 2.3, entity.posZ - 0.6 + worldIn.rand.nextDouble() * 1.2, 0, -1, 0);
			} else if (worldIn.rand.nextFloat() <= 0.15 * lvl)
			{
					ItemStack eggStack = new ItemStack(Items.SPAWN_EGG);
					ItemMonsterPlacer.applyEntityIdToItemStack(eggStack, EntityRegistry.getEntry(entity.getClass()).getRegistryName());
					EntityItem egg = new EntityItem(worldIn, entity.posX, entity.posY, entity.posZ, eggStack);
					worldIn.spawnEntity(egg);
			}
		}
		
		return stack;
	}
}
