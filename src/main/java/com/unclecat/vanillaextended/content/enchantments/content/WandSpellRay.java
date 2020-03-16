package com.unclecat.vanillaextended.content.enchantments.content;

import com.unclecat.vanillaextended.content.enchantments.WandSpellBase;
import com.unclecat.vanillaextended.content.items.ItemBase;
import com.unclecat.vanillaextended.utils.ModMath;
import com.unclecat.vanillaextended.utils.ModNetWrapper;
import com.unclecat.vanillaextended.utils.messages.MessageMagicAttackEntity;

import ca.weblite.objc.Message;
import net.minecraft.block.SoundType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelMagmaCube;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class WandSpellRay extends WandSpellBase
{

	public WandSpellRay()
	{
		super("ray", 1, Rarity.UNCOMMON);
	}
	
	
	
	@Override
	public int getMaxLevel()
	{
		return 5;
	}
	@Override
	public int getMinLevel()
	{
		return 1;
	}
	
	
	@Override
	public void onUsingTick(int lvl, EntityLivingBase player, EnumHand mainHand, ItemStack stack)
	{	
		if (player instanceof EntityPlayer) // TODO: Check if EntityPlayer is the only player class
		{
			if (hasEnoughExpToCast((EntityPlayer)player, lvl))
			{
				consumeExpForCast((EntityPlayer)player, lvl);
			} else
			{
				return;
			}
		}
		
		if (!player.world.isRemote) return;
		
		Entity entity = ItemBase.getMouseOver(Minecraft.getMinecraft().getRenderPartialTicks(), lvl * 16);
		
		if (entity != null && entity instanceof EntityLivingBase)
		{
			ModNetWrapper.WRAPPER.sendToServer(new MessageMagicAttackEntity(entity.getEntityId(), 1.0f + 0.4f * (float)lvl));
			Vec3d lookAt = player.getLook(Minecraft.getMinecraft().getRenderPartialTicks());
			float distance = player.getDistance(entity);
			Vec3d ray;
			
			if (Minecraft.getMinecraft().player == player)
			{
				Vec3d lookAt2 = ModMath.getVectorForRotation(player.rotationPitch, player.rotationYawHead + 100);
				ray = player.getPositionVector().addVector(0 + lookAt2.x / 3.5, player.getEyeHeight(), 0 + lookAt2.z / 3.5);
			} else
			{
				ray = player.getPositionVector().addVector(0 + ModMath.getVectorForRotation(player.rotationPitch, player.rotationYawHead + 10).x, player.getEyeHeight(), 0);
			}
			
			for (float i = 0; i <= distance; i += 0.4)
			{
				ray = ray.add(new Vec3d( lookAt.x / 2.5, lookAt.y / 2.5, lookAt.z / 2.5 ));
				player.world.spawnParticle(EnumParticleTypes.END_ROD, true, ray.x, ray.y, ray.z, 0, 0, 0);
			}
		}
	}
}
