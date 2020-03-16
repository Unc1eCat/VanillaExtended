package com.unclecat.vanillaextended.content.containers.content;

import com.unclecat.vanillaextended.content.blocks.content.MultiplexingDropper;
import com.unclecat.vanillaextended.utils.ModNetWrapper;
import com.unclecat.vanillaextended.utils.messages.MessageMultiplexingDropperPredicateChange;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MultiplexDropperContainer extends Container
{
	private final IInventory multiplexingDropper;
	private Item predicateItem;
	

	
	public MultiplexDropperContainer(EntityPlayer player, IInventory multiplexingDropper)
	{
		this.multiplexingDropper = multiplexingDropper;
		predicateItem = ((MultiplexingDropper.ThisTileEntity)multiplexingDropper).predicateItem;
		
		for (int i = 0; i < 5; i++)
		{
			addSlotToContainer(new Slot(multiplexingDropper, i, 7 + i * 18, 13));
		}
		
		for (int i = 5; i < 10; i++)
		{
			addSlotToContainer(new Slot(multiplexingDropper, i, i * 18 - 9, 59));
		}
		
		for (int i = 0; i < 3; ++i)
	    {
	        for (int j = 0; j < 9; ++j)
	        {
	        	addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
	        }
	    }
	
	    for (int k = 0; k < 9; ++k)
	    {
	    	addSlotToContainer(new Slot(player.inventory, k, 8 + k * 18, 142));
	    }
	}
	
	@Override
	public void addListener(IContainerListener listener)
	{
		super.addListener(listener);
		listener.sendAllWindowProperties(this, multiplexingDropper);
	}
	
	
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		
		for (IContainerListener i : listeners)
		{
			if (predicateItem != ((MultiplexingDropper.ThisTileEntity)multiplexingDropper).predicateItem)
			{
				i.sendAllContents(this, ((MultiplexingDropper.ThisTileEntity)multiplexingDropper).slots);
				ModNetWrapper.WRAPPER.sendToServer(new MessageMultiplexingDropperPredicateChange(((MultiplexingDropper.ThisTileEntity)multiplexingDropper).predicateItem, ((MultiplexingDropper.ThisTileEntity)multiplexingDropper).getPos()));
			}
		}
		
		predicateItem = ((MultiplexingDropper.ThisTileEntity)multiplexingDropper).predicateItem;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		Slot slot = inventorySlots.get(index);
		ItemStack oldStack = ItemStack.EMPTY;
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack slotStack = slot.getStack();
			oldStack = slotStack.copy();
			
			if (index < 10) // From multiplexing dropper
			{
				if (!mergeItemStack(slotStack, 10, 46, true))
				{
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(slotStack, oldStack);
			}
			else if (index >= 10) // From player inventory
			{
				if (((MultiplexingDropper.ThisTileEntity)multiplexingDropper).predicateItem != null && ((MultiplexingDropper.ThisTileEntity)multiplexingDropper).predicateItem == slotStack.getItem())
				{
					if (!mergeItemStack(slotStack, 5, 9, false))
					{
						return ItemStack.EMPTY;
					}
				}
				else
				{
					if (!mergeItemStack(slotStack, 0, 4, false))
					{
						return ItemStack.EMPTY;
					}
				}
			}
			
			if (slotStack.getCount() == 0)
			{
				slot.putStack(ItemStack.EMPTY);
			}
			else 
			{
				slot.onSlotChanged();
			}
			
			if (slotStack.getCount() != oldStack.getCount())
			{
				return ItemStack.EMPTY;
			}
			
			slot.onTake(playerIn, slotStack);
		}
		return oldStack;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return multiplexingDropper.isUsableByPlayer(playerIn);
	}
}
