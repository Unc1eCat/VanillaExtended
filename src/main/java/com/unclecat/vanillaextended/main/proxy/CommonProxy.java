package com.unclecat.vanillaextended.main.proxy;

import com.unclecat.vanillaextended.content.blocks.content.BlockRedstoneGate;
import com.unclecat.vanillaextended.main.References;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy 
{
	public void registerItemRenderer(Item item, int meta, ModelResourceLocation modelResourceLocation)
	{
		
	}
	public void registerItemRenderer(Item item, int meta, String registryName, String id)
	{
		
	}
	
	
	public void registerTileEntity(Class<? extends TileEntity> tileEntity, String name)
	{
		GameRegistry.registerTileEntity(tileEntity, new ResourceLocation(References.MOD_ID + ":" + name));
	}
	
	public <T extends TileEntity> void registerRenderer(Class<T> tileEntityClass, TileEntitySpecialRenderer<? super T> specialRenderer)
	{

	}
	
	public void registerItemColors()
	{
		
	}
	
	public void runOnClient(Runnable toRun)
	{
		
	}
	public void initTheArrow()
	{
		
	}
}
