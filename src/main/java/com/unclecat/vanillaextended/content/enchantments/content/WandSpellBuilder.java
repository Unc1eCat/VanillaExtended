package com.unclecat.vanillaextended.content.enchantments.content;

import com.unclecat.vanillaextended.content.enchantments.WandSpellBase;
import com.unclecat.vanillaextended.main.Main;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WandSpellBuilder extends WandSpellBase
{

	public WandSpellBuilder()
	{
		super("builder", 2, Rarity.RARE);
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
	public int getUsingDuration(ItemStack stack, int lvl)
	{
		return lvl >= 4 ? 4 : 8;
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
		player.setActiveHand(EnumHand.MAIN_HAND);

		//// Validation and preparing variables ////
		// You can build only if you stand on ground
		if (!player.onGround) return EnumActionResult.PASS;	
	
		// Some variables
		BlockPos pos = player.getPosition();
		
		// Getting direction and specializing it
		EnumFacing direction = EnumFacing.getDirectionFromEntityLiving(pos, player).getOpposite();
		direction = player.rotationPitch > 60 ? EnumFacing.UP : (player.rotationPitch < -60 ? EnumFacing.DOWN : EnumFacing.getDirectionFromEntityLiving(pos, player)).getOpposite();
		if (direction == EnumFacing.DOWN) return EnumActionResult.PASS;
		else if (direction != EnumFacing.UP) pos = pos.offset(direction).down();
	
		// Some variables
		ItemStack offHandStack = player.getHeldItemOffhand();
		double axisDiff = 0;
		
		// You'll place only one block if you sneak
		if (player.isSneaking()) lvl = 1;
		
		
		//// Building ////
		for (int i = 0; i < lvl; i++)
		{
			// Checking if player can safely stand here
			if (worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos) && !worldIn.getBlockState(pos.up(2)).causesSuffocation())
			{
				// Placing block
				if (offHandStack.getItem() instanceof ItemBlock && (offHandStack.getCount() >= 1 || player.capabilities.isCreativeMode))
				{ // Item from off-hand
					// Don't consume items if player is in creative mode
					worldIn.playSound(player, pos, ((ItemBlock)offHandStack.getItem()).getBlock().getSoundType().getPlaceSound(), SoundCategory.BLOCKS, 16, 1);
					if (!worldIn.isRemote) ((ItemBlock)offHandStack.getItem()).placeBlockAt(offHandStack, player, player.world, pos, EnumFacing.UP, 0, 0, 0,((ItemBlock)offHandStack.getItem()).getBlock().getStateFromMeta(offHandStack.getMetadata()));
					if (!player.capabilities.isCreativeMode)
					{
						offHandStack.shrink(1);		
					}
				} else if (!worldIn.isRemote)
				{ // Force block
					worldIn.setBlockState(pos, Main.FORCE_BLOCK.getDefaultState());
				}
				
				pos = pos.offset(direction);
				axisDiff++;
			} else
			{
				break;
			}
		}
		player.setHeldItem(EnumHand.OFF_HAND, offHandStack);
		
		// Moving player
//		player.setPosition(player.posX + axisDiff * direction.getFrontOffsetX(), player.posY + axisDiff * direction.getFrontOffsetY() + (axisDiff > 0 ? 0.8 : 0), player.posZ + axisDiff * direction.getFrontOffsetZ());
		if (player instanceof EntityPlayerMP) ((EntityPlayerMP)player).connection.setPlayerLocation(player.posX + axisDiff * direction.getFrontOffsetX(), player.posY + axisDiff * direction.getFrontOffsetY() + (axisDiff > 0 ? 0.8 : 0), player.posZ + axisDiff * direction.getFrontOffsetZ(), player.rotationYaw, player.rotationPitch);

		return EnumActionResult.SUCCESS;
	}
}