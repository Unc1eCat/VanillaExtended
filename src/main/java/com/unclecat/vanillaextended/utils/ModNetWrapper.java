package com.unclecat.vanillaextended.utils;

import org.apache.logging.log4j.Level;

import com.unclecat.vanillaextended.main.Main;
import com.unclecat.vanillaextended.main.References;
import com.unclecat.vanillaextended.utils.messages.MessageApplyEffectToEntity;
import com.unclecat.vanillaextended.utils.messages.MessageMagicAttackEntity;
import com.unclecat.vanillaextended.utils.messages.MessageMultiplexingDropperPredicateChange;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ModNetWrapper
{
	public static final SimpleNetworkWrapper WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(References.MOD_ID);
	protected static int id = 0;
	
	
	public static void init()
	{
		WRAPPER.registerMessage(MessageMagicAttackEntity.ThisHandler.class, MessageMagicAttackEntity.class, id++, Side.SERVER);
		WRAPPER.registerMessage(MessageApplyEffectToEntity.ThisHandler.class, MessageApplyEffectToEntity.class, id++, Side.SERVER);
		WRAPPER.registerMessage(MessageMultiplexingDropperPredicateChange.ThisHandler.class, MessageMultiplexingDropperPredicateChange.class, id++, Side.SERVER);
	}
	
	public static void sheduleOnServer(MessageContext ctx, Runnable task)
	{
		ctx.getServerHandler().player.getServerWorld().addScheduledTask(task);
	}
	
	public static void sheduleOnClient(Runnable task)
	{
		Minecraft.getMinecraft().addScheduledTask(task);
	}
	
//	public static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<REQ> clazz, Side recieveOn)
//	{
//		try
//		{
//			Class clazzes[] = clazz.getClasses();
//			for (Class thisHandlerClazz : clazzes)
//			{
//				String s = thisHandlerClazz.getSimpleName().toString();
//				if (s == "ThisHandler")
//				{					
//					WRAPPER.registerMessage((Class<? extends IMessageHandler<REQ, REPLY>>)thisHandlerClazz, clazz, id++, Side.SERVER);
//					break;
//				}
//			}
//			
//		} catch (IllegalArgumentException e)
//		{
//			Main.LOGGER.log(Level.ERROR, e.toString());
//			e.printStackTrace();
//		}
//	}
}
