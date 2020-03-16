package com.unclecat.vanillaextended.content.enchantments.content;

import java.util.List;

import com.unclecat.vanillaextended.content.enchantments.WandSpellBase;
import com.unclecat.vanillaextended.utils.ModMath;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class WandSpellPowerPush extends WandSpellBase
{

	public WandSpellPowerPush()
	{
		super("power_push", 4, Rarity.RARE);
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
		return lvl;
	}
	
	@Override
	public EnumActionResult onItemRightCick(int lvl, EntityPlayer player, World worldIn, EnumHand hand)
	{
		if (hasEnoughExpToCast(player, lvl))
		{
			consumeExpForCast(player, lvl);
		} else
		{
			return EnumActionResult.FAIL;
		}
		
		// Get vector from player's rotation
		Vec3d lookingTo = ModMath.getVectorForRotation(player.rotationPitch, player.rotationYawHead);
		
		if (worldIn.isRemote)
		{
			worldIn.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, player.posX + lookingTo.x * 1.6, player.posY + player.eyeHeight + lookingTo.y * 1.6, player.posZ + lookingTo.z * 1.6, 1.6 / lvl, 1, 1);
			return EnumActionResult.SUCCESS;
		}
		
		// A vertex of affected entities box
		double xA = player.posX + lookingTo.x * 10 + 8;
		double yA = player.posY + lookingTo.y * 10 + 8;
		double zA = player.posZ + lookingTo.z * 10 + 8;

		// B vertex of affected entities box
		double xB = player.posX + lookingTo.x * 4 - 8;
		double yB = player.posY + lookingTo.y * 4 - 8; 
		double zB = player.posZ + lookingTo.z * 4 - 8;
		
		// Get list of affected entities in the box
		List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(player, new AxisAlignedBB(xA, yA, zA, xB, yB, zB));
		
		// Set motion for each affected entity depending on distance, entity properties and level
		for (Entity entity : list)
		{
			if (!(entity instanceof EntityLivingBase)) continue;
						
			// Calculate motion
			double pushX = (entity.posX - player.posX);
			double pushY = (entity.posY - player.posY);
			double pushZ = (entity.posZ - player.posZ);
			
			double signX = pushX < 0 ? -1 : 1;
			double signZ = pushZ < 0 ? -1 : 1;
			
			pushX = Math.abs(pushX);
			pushZ = Math.abs(pushZ);
					
			double min = pushX < pushZ ? pushX : pushZ;
			
			pushX += 1 - min;
			pushZ += 1 - min;
			
			double pushPower = lvl / (player.getDistance(entity) + 1);
			
			// Add motion to entity
			entity.motionX += pushPower * pushX * signX;
			entity.motionY += pushPower * pushY + (lvl * 0.1);
			entity.motionZ += pushPower * pushZ * signZ;
		}
				
		return EnumActionResult.SUCCESS;
	}
}
