package com.unclecat.vanillaextended.content.enchantments.content;

import com.unclecat.vanillaextended.content.enchantments.WandSpellBase;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WandSpellFireball extends WandSpellBase
{

	public WandSpellFireball()
	{
		super("fireball", 12, Rarity.COMMON);
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
		return 4 + lvl * 3;
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
		
		if (worldIn.isRemote) return stack;
		Vec3d to = entityLiving.getLook(Minecraft.getMinecraft().getRenderPartialTicks());
		Vec3d from = entityLiving.getPositionEyes(Minecraft.getMinecraft().getRenderPartialTicks()); 
		
		EntityLargeFireball fireball = new EntityLargeFireball(worldIn, from.x, from.y, from.z, to.x, to.y, to.z);
		fireball.explosionPower = lvl - 1;
		fireball.shootingEntity = entityLiving;
		worldIn.spawnEntity(fireball);

		return stack;
	}
}





