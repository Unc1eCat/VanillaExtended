package com.unclecat.vanillaextended.content.enchantments.content;

import com.unclecat.vanillaextended.content.enchantments.WandSpellBase;
import com.unclecat.vanillaextended.main.Main;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class WandSpellLumin extends WandSpellBase
{

	public WandSpellLumin()
	{
		super("luminosity", 2, Rarity.COMMON);
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
		return 12;
	}
	
	
	
	@Override
	public EnumActionResult onItemUseFirst(int lvl, EntityPlayer player, World world, BlockPos pos, EnumFacing side,
			float hitX, float hitY, float hitZ, EnumHand hand)
	{
		if (hasEnoughExpToCast(player, lvl))
		{
			consumeExpForCast(player, lvl);
		} else
		{
			return EnumActionResult.FAIL;
		}
		
		pos = pos.offset(side);
		if (world.mayPlace(Main.LUMINOUS_BLOCK, pos, false, side, null))
		{
			world.setBlockState(pos, Main.LUMINOUS_BLOCK.getDefaultState());
		}
		
		return EnumActionResult.SUCCESS;
	}
}





