package com.unclecat.vanillaextended.utils;

import java.util.Collection;

import com.unclecat.vanillaextended.main.Main;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;

public class ItemColorHandler implements IItemColor
{
	public static ItemColorHandler INSTANCE = new ItemColorHandler();

	public ItemColorHandler()
	{
		
	}

	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex)
	{
		if (stack.getItem() == Main.SYRINGE && tintIndex == 0)
		{
			NBTTagCompound nbt = stack.getTagCompound();
			if (nbt == null) return 0;
			if (nbt.hasKey("PotionColor"))
			{
				return nbt.getInteger("PotionColor");
			}
			return 0;
		}
		return 0xFFFFFFFF;
	}
}
