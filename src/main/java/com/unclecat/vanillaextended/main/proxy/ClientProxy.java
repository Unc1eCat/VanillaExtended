package com.unclecat.vanillaextended.main.proxy;

import com.unclecat.vanillaextended.content.entities.EntityExplodingArrow;
import com.unclecat.vanillaextended.content.entities.EntitySlowmotionArrow;
import com.unclecat.vanillaextended.main.Main;
import com.unclecat.vanillaextended.utils.ItemColorHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerItemRenderer(Item item, int meta, ModelResourceLocation modelResourceLocation)
	{
		ModelLoader.setCustomModelResourceLocation(item, meta, modelResourceLocation);
	}
	@Override
	public void registerItemRenderer(Item item, int meta, String registryName, String id)
	{
		registerItemRenderer(item, meta, new ModelResourceLocation(new ResourceLocation(registryName), id));
	}
	
	
	public <T extends TileEntity> void registerRenderer(Class<T> clazz, TileEntitySpecialRenderer<? super T> specialRenderer)
	{
		ClientRegistry.bindTileEntitySpecialRenderer(clazz, specialRenderer);
	}
	
	@Override
	public void registerItemColors()
	{
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(ItemColorHandler.INSTANCE, Main.SYRINGE);
	}
	
	@Override
	public void runOnClient(Runnable toRun)
	{
		toRun.run();
	}

	@Override
	public void initTheArrow()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityExplodingArrow.class, new IRenderFactory<EntityExplodingArrow>()
		{
			@Override
			public Render<? super EntityExplodingArrow> createRenderFor(RenderManager manager)
			{
				return new EntityExplodingArrow.ThisRenderer(manager);
			}
		});
		
		RenderingRegistry.registerEntityRenderingHandler(EntitySlowmotionArrow.class, new IRenderFactory<EntitySlowmotionArrow>()
		{
			@Override
			public Render<? super EntitySlowmotionArrow> createRenderFor(RenderManager manager)
			{
				return new EntitySlowmotionArrow.ThisRenderer(manager);
			}
		});
	}
}
