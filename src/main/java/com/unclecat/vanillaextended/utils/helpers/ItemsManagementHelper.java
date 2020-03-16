package com.unclecat.vanillaextended.utils.helpers;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/* 
 * Helps with item stacks, inventory and containers 
 * */
public class ItemsManagementHelper
{
	public static boolean areStackItemsEqual(ItemStack stack1, ItemStack stack2)
	{
		return stack1.isItemEqual(stack2) && ItemStack.areItemStackTagsEqual(stack1, stack2);
	}
}
