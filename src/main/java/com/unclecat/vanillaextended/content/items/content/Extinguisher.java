package com.unclecat.vanillaextended.content.items.content;

import java.util.Random;

import com.unclecat.vanillaextended.content.items.ItemBase;

import net.minecraft.block.BlockFire;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Extinguisher extends ItemBase
{

	public Extinguisher()
	{
		super("extinguisher", CreativeTabs.TOOLS);
		
		setMaxDamage(30);  
		setMaxStackSize(maxStackSize);
		setMaxStackSize(1);
	}
	
	
	@Override
	public boolean isDamageable()
	{
		return true;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		Random rand = worldIn.rand;
		
		playerIn.extinguish();
		
		for (int i = 8; i >= -8; i--)
		{
			for (int j = 8; j >= -8; j--)
			{
				for (int k = 8; k >= -8; k--)
				{
					if (worldIn.getBlockState(new BlockPos(playerIn.posX + i, playerIn.posY+ j, playerIn.posZ + k)).getBlock() instanceof BlockFire)
					{
						worldIn.setBlockToAir(new BlockPos(playerIn.posX + i, playerIn.posY+ j, playerIn.posZ + k));
					}
					if (worldIn.isRemote && Minecraft.getMinecraft().gameSettings.particleSetting < 2) worldIn.spawnParticle(EnumParticleTypes.SPIT, playerIn.posX + i + rand.nextFloat(), playerIn.posY + j + rand.nextFloat(), playerIn.posZ + k + rand.nextFloat(), 0, 0, 0);
				}
			}
			
//			for (int l = 20; l > 0; l--)
//			{
//				worldIn.spawnParticle(EnumParticleTypes.SPIT, playerIn.posX + rand.nextFloat() * 10 - 5, playerIn.posY + rand.nextFloat() * 10 - 5, playerIn.posZ + rand.nextFloat() * 10 - 5, 0, 0, 0);
//			}
		}
		
		playerIn.getHeldItem(handIn).damageItem(1, playerIn);
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
	
}




