package com.unclecat.vanillaextended.content.items.content;

import java.util.ArrayList;
import java.util.List;

import com.unclecat.vanillaextended.content.items.ItemBase;
import com.unclecat.vanillaextended.utils.ModMath;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class OreFinder extends ItemBase
{
	public OreFinder()
	{
		super("ore_finder", CreativeTabs.TOOLS);
		
		setMaxStackSize(1);
		
		addPropertyOverride(new ResourceLocation("angle"), new IItemPropertyGetter()
		{
			
			@Override
			public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn)
			{
				NBTTagCompound nbt = stack.getTagCompound();
				if (nbt == null) return 1.0F; // Void
				else if (!nbt.hasKey("RecentOreX")) return 0.999999F;
				else if (entityIn == null) return 0.8F;
				else
				{
					float rotation = ModMath.getRotationForVector(nbt.getInteger("RecentOreX") - (int)entityIn.posX, nbt.getInteger("RecentOreZ") - (int)entityIn.posZ) - MathHelper.positiveModulo(entityIn.rotationYawHead, 360.0F);
					//if (entityIn != null) entityIn.sendMessage(new TextComponentString(Float.toString(rotation)));
					//if (entityIn != null) entityIn.sendMessage(new TextComponentString(Float.toString(nbt.getFloat("HandRotation"))));
					return MathHelper.positiveModulo(rotation, 360.0F) / 360.0F; // ((hand - (yaw % 360 + 360) % 360) % 360 + 360) % 360 how to simplify it? TODO: Simplify the modulo's				
				}
			}
		});
		
		setMaxDamage(69);
	}
	
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt != null)
		{
			int oreDamage = nbt.getInteger("OreDamage");
			ItemStack oreStack = new ItemStack(Block.getBlockFromName(nbt.getString("Ore")));
			oreStack.setItemDamage(oreDamage);
			tooltip.add(I18n.translateToLocal(oreStack.getUnlocalizedName() + ".name"));
		}
		else tooltip.add(TextFormatting.RED + "" + TextFormatting.ITALIC + I18n.translateToLocal("item.ore_finder.noOreBound"));		
	}
	
	
	@Override
	public String getHighlightTip(ItemStack item, String displayName)
	{
		NBTTagCompound nbt = item.getTagCompound();
		if (nbt != null)
		{
			int oreDamage = nbt.getInteger("OreDamage");
			ItemStack oreStack = new ItemStack(Block.getBlockFromName(nbt.getString("Ore")));
			oreStack.setItemDamage(oreDamage);
			displayName += ":  " + TextFormatting.GRAY + I18n.translateToLocal(oreStack.getUnlocalizedName() + ".name"); //oreStack.getUnlocalizedName());
		}
		return displayName;
	}
	

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
//		if (!worldIn.isRemote)
//			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		
		// Prepare
		NBTTagCompound nbt = playerIn.getHeldItem(handIn).getTagCompound();
		if (nbt == null)
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
		BlockPos playerPos = playerIn.getPosition();
		Block ore = Block.getBlockFromName(nbt.getString("Ore"));
		int oreDamage = nbt.getInteger("OreDamage");
		BlockPos pos = playerPos.subtract(new Vec3i(12, 12, 12));
		BlockPos posEnd = pos.add(new Vec3i(24, 24, 24));
		List<BlockPos> matchingBlocks = new ArrayList<BlockPos>();
		BlockPos closestPos = null;
		double closestDist = 9999;

		while (pos.getX() < posEnd.getX()) // Finds ores in the area
		{
			while (pos.getY() < posEnd.getY())
			{
				while (pos.getZ() < posEnd.getZ())
				{
					IBlockState iblockstate = worldIn.getBlockState(pos);
					try
					{
						if (iblockstate.getBlock() == ore && iblockstate.getBlock().getPickBlock(iblockstate, null, worldIn, pos, playerIn).getItemDamage() == oreDamage)
						{
							matchingBlocks.add(pos);
						}
					} catch (Exception e)
					{
						if (iblockstate.getBlock() == ore)
						{
							matchingBlocks.add(pos);
						}
					}
					pos = pos.add(0, 0, 1);
				}
				pos = pos.add(0, 1, 0);
				pos = new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 32);
			}
			pos = pos.add(1, 0, 0);
			pos = new BlockPos(pos.getX(), pos.getY() - 32, pos.getZ());
		}

		for (BlockPos i : matchingBlocks) // Finds closest ore in the list of found ores. It's not quicksort but insertsort ok
		{
			BlockPos j = i.subtract(playerPos);
			double distance = Math.sqrt(j.getX() * j.getX() + j.getY() * j.getY() + j.getZ() * j.getZ());
			if (distance <= closestDist) 
			{
				closestPos = i;
				closestDist = distance;
			}
		}

		if (closestDist == 9999) // Calculates rotation
		{
			nbt.removeTag("RecentOreX");
			nbt.removeTag("RecentOreZ");
		} else
		{
			nbt.setInteger("RecentOreX", closestPos.getX());
			nbt.setInteger("RecentOreZ", closestPos.getZ());
			//playerIn.sendMessage(new TextComponentString(Float.toString(rotation))); // TODO: Debug remove
			//playerIn.sendMessage(new TextComponentString(closestRelatedPos.toString())); // TODO: Debug remove
			//playerIn.sendMessage(new TextComponentString(closestPos.add(playerPos).toString())); // TODO: Debug remove

			for (int i = 15; i >= 0; i--)
			{
				worldIn.spawnParticle(EnumParticleTypes.DRAGON_BREATH, closestPos.getX() + worldIn.rand.nextDouble(), closestPos.getY() + worldIn.rand.nextDouble(), closestPos.getZ() + worldIn.rand.nextDouble(), 0.2D * worldIn.rand.nextDouble() - 0.1D, 0.4D * worldIn.rand.nextDouble() - 0.2D, 0.2D * worldIn.rand.nextDouble() - 0.1D);
				worldIn.spawnParticle(EnumParticleTypes.CRIT, closestPos.getX() + worldIn.rand.nextDouble(), closestPos.getY() + worldIn.rand.nextDouble(), closestPos.getZ() + worldIn.rand.nextDouble(), worldIn.rand.nextDouble() - 0.5D, worldIn.rand.nextDouble() - 0.5D, worldIn.rand.nextDouble() - 0.5D);
			}
		}
		
		playerIn.getHeldItem(handIn).setTagCompound(nbt);
		playerIn.getHeldItem(handIn).damageItem(1, playerIn);
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
	
	
	@Override
	public void registerModels()
	{
		super.registerModels();
	}	
}
