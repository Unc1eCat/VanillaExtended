package com.unclecat.vanillaextended.content.enchantments.content;

import com.unclecat.vanillaextended.content.blocks.content.ExperienceBlock;
import com.unclecat.vanillaextended.content.enchantments.WandSpellBase;
import com.unclecat.vanillaextended.main.Main;
import com.unclecat.vanillaextended.utils.ModMath;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class WandSpellExperiencePlacer extends WandSpellBase
{

	public WandSpellExperiencePlacer()
	{
		super("experience_placer", 0, Rarity.RARE);
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
		return 16;
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
		if (!world.mayPlace(Main.EXPERIENCE_BLOCK, pos, false, side, null)) return EnumActionResult.FAIL;

		if (!player.isCreative())
		{
			if (player.experienceTotal >= (player.isSneaking() ? 128 : 32))
			{
				ModMath.decreaseExp(player, player.isSneaking() ? 128 : 32);
			} else
			{
				return EnumActionResult.PASS;
			}
		}

		world.playSound(player, pos, SoundEvent.REGISTRY.getObject(new ResourceLocation("block.glass.place")), SoundCategory.BLOCKS, 16, 1.2f);
		if (player.isSneaking())
		{
			world.setBlockState(pos, Main.EXPERIENCE_BLOCK.getDefaultState().withProperty(ExperienceBlock.CONDENSED, true));
		} else
		{
			world.setBlockState(pos, Main.EXPERIENCE_BLOCK.getDefaultState().withProperty(ExperienceBlock.CONDENSED, false));
		}

		return EnumActionResult.SUCCESS;
	}
}





