package com.unclecat.vanillaextended.content.gui;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.Nullable;

import com.unclecat.vanillaextended.content.containers.content.MultiplexDropperContainer;
import com.unclecat.vanillaextended.content.gui.content.GuiMultiplexingDropper;

import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	public static enum GUIS
	{
		MULTIPLEXING_DROPPER(MultiplexDropperContainer.class, GuiMultiplexingDropper.class);
		
		
		
		public Class<? extends Container> container;
		public Class<? extends Gui> gui;
		
		
		GUIS(Class<? extends Container> container, @Nullable Class<? extends Gui> gui)
		{
			this.gui = gui;
			this.container = container;
		}
	}
	
		

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		try
		{
			return GUIS.values()[ID].container.getDeclaredConstructor(EntityPlayer.class, IInventory.class).newInstance(player, (IInventory)world.getTileEntity(new BlockPos(x, y, z)));
		} catch (Throwable e)
		{
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		try
		{
			return GUIS.values()[ID].gui.getDeclaredConstructor(EntityPlayer.class, IInventory.class).newInstance(player, (IInventory)world.getTileEntity(new BlockPos(x, y, z)));
		} catch (Throwable e)
		{
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
	}
	
}
