package com.unclecat.vanillaextended.utils.helpers;

import java.util.Iterator;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

/* 
 * Helps with item stacks, inventory and containers 
 * */
public class ItemsManagementHelper
{
	public static interface ISpecificSlots extends Iterator<Integer>
	{
		 // TODO: Test em all
	}
	public static class SpecificSlotsRange implements ISpecificSlots
	{
		public int first, last;
		protected int pos;
		
		public SpecificSlotsRange(int first, int last)
		{
			this.first = first;
			this.last = last;
			pos = first;
		}

		@Override
		public boolean hasNext()
		{
			return pos <= last;
		}

		@Override
		public Integer next()
		{
			return pos++;
		}
		
		@Override
		public void forEachRemaining(Consumer<? super Integer> action)
		{
			for (int i = first; i <= last; i++)
			{
				action.accept(i);
			}
		}
	}
	public static class SpecificSlotsArray implements ISpecificSlots
	{
		public int[] array;
		protected int pos = 0;
		
		public SpecificSlotsArray(int[] array)
		{
			this.array = array;
		}

		@Override
		public boolean hasNext()
		{
			return pos < array.length;
		}

		@Override
		public Integer next()
		{
			return array[pos++];
		}
		
		@Override
		public void forEachRemaining(Consumer<? super Integer> action)
		{
			for (int i = 0; i < array.length; i++)
			{
				action.accept(array[i]);
			}
		}
	}
	
//	public static class SpecificSlotsInventory<T extends ISpecificSlots> extends T // TODO: Complete
//	{
//		protected IInventory inventory;
//		
//		public SpecificSlotsInventory(IInventory inventory)
//		{
//			this.inventory = inventory;
//		}
//	}
	
	
	public static boolean areStackItemsEqual(ItemStack stack1, ItemStack stack2)
	{
		return stack1.isItemEqual(stack2) && ItemStack.areItemStackTagsEqual(stack1, stack2);
	}
	
//	/*
//	 * Finds stacks that can be transfered from one inventory into another
//	 */
//	public static findFirstTransferable(IInventory a, IInventory b, )
//	{
//		
//	}
	
	/*
	 * -1 if not found
	 */
	public static int findFirstMatching(IInventory inventoryIn, int first, int last, Predicate<ItemStack> predicate)
	{
		for (int i = first; i <= last; i++)
		{
			if (predicate.apply(inventoryIn.getStackInSlot(i)))
			{
				return i;
			}
		}
		
		return -1;
	}
	
	/*
	 * -1 if not found
	 */
	public static int findFirstNotEmpty(IInventory inventoryIn, int first, int last)
	{
		for (int i = first; i <= last; i++)
		{
			if (!inventoryIn.getStackInSlot(i).isEmpty())
			{
				return i;
			}
		}
		
		return -1;
	}
	
	/*
	 * -1 if not found
	 */
	public static int findFirstToPut(IInventory inventoryIn, int first, int last, ItemStack whatToPut)
	{
		for (int i = first; i <= last; i++)
		{
			if (areStackItemsEqual(inventoryIn.getStackInSlot(i), whatToPut) || inventoryIn.getStackInSlot(i).isEmpty())
			{
				return i;
			}
		}
		
		return -1;
	}
	
	/**
     * Returns false if the inventory has any room to place items in
     * 
     * Copied from TileEntityHopper
     */
    public static boolean isSidedInventoryFull(ISidedInventory inventoryIn, EnumFacing side)
    {
        if (inventoryIn instanceof ISidedInventory)
        {
            ISidedInventory isidedinventory = inventoryIn;
            int[] aint = isidedinventory.getSlotsForFace(side);

            for (int k : aint)
            {
                ItemStack itemstack1 = isidedinventory.getStackInSlot(k);

                if (itemstack1.isEmpty() || itemstack1.getCount() != itemstack1.getMaxStackSize())
                {
                    return false;
                }
            }
        }

        return true;
    }
    
    public static boolean isNotInventoryFull(IInventory inventoryIn)
    {
    	int i = inventoryIn.getSizeInventory();

        for (int j = 0; j < i; ++j)
        {
            ItemStack itemstack = inventoryIn.getStackInSlot(j);

            if (itemstack.isEmpty() || itemstack.getCount() != itemstack.getMaxStackSize())
            {
                return false;
            }
        }
        
        return true;
    }
    
    public static boolean isInventoryFull(IInventory inventoryIn, @Nullable EnumFacing side)
    {
    	if (inventoryIn instanceof ISidedInventory) return isSidedInventoryFull((ISidedInventory) inventoryIn, side);
    	else return isNotInventoryFull(inventoryIn);
    }
}
