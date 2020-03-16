package com.unclecat.vanillaextended.content.enchantments.content;

import com.unclecat.vanillaextended.content.enchantments.WandSpellBase;
import com.unclecat.vanillaextended.main.Main;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WandSpellForceShield extends WandSpellBase
{

	public WandSpellForceShield()
	{
		super("force_shield", 30, Rarity.RARE);
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
		
		if (worldIn.isRemote) return stack;
		
		BlockPos center = entityLiving.getPosition().up();
		
		placeSide(center, worldIn, EnumFacing.NORTH);
		placeSide(center, worldIn, EnumFacing.SOUTH);
		placeSide(center, worldIn, EnumFacing.EAST);
		placeSide(center, worldIn, EnumFacing.WEST);
		placeSide(center, worldIn, EnumFacing.UP);
		placeSide(center, worldIn, EnumFacing.DOWN);
		
		return stack;
	}
	
	
	
	/** @param pos Center of shield bubble(position of player) */
	public static void placeSide(BlockPos center, World world, EnumFacing side)
	{	
		EnumFacing a;
		EnumFacing b;
		
		switch (side.getAxis())
		{
		case X:
			a = EnumFacing.DOWN;
			b = EnumFacing.NORTH;
			break;
		case Y:
			a = EnumFacing.WEST;
			b = EnumFacing.NORTH;
			break;
		case Z:
			a = EnumFacing.DOWN;
			b = EnumFacing.WEST;
			break;
		default:
			return;
		}
			
		BlockPos pos = center.offset(a).offset(b).offset(side, 2);
		
		a = a.getOpposite();
		b = b.getOpposite();
		
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				tryToPlaceShieldBlock(pos, world);
				
				pos = pos.offset(a);
			}
			pos = pos.offset(a.getOpposite(), 3);
			pos = pos.offset(b);
		}
	}
	
	public static boolean tryToPlaceShieldBlock(BlockPos pos, World world)
	{
		if (world.getBlockState(pos).getBlock().isReplaceable(world, pos))
		{
			world.setBlockState(pos, Main.FORCE_BLOCK.getDefaultState());
//			world.updateBlockTick(pos, Main.FORCE_BLOCK, 14*20 + world.rand.nextInt(80), -1);
			return true;
		}
		return false;
	}
}
