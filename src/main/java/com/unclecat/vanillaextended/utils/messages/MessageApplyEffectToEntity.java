package com.unclecat.vanillaextended.utils.messages;

import com.unclecat.vanillaextended.utils.ModNetWrapper;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageApplyEffectToEntity implements IMessage
{
	public int entityId;
	public NBTTagCompound effect;
	
	
	public MessageApplyEffectToEntity() {}
	
	public MessageApplyEffectToEntity(NBTTagCompound effect, int entityId)
	{
		this.effect = effect;
		this.entityId = entityId;
	}
	
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		entityId = buf.readInt();
		
		effect = new NBTTagCompound();
		effect.setByte("Id", buf.readByte());
		effect.setByte("Amplifier", buf.readByte());
		effect.setInteger("Duration", buf.readInt());
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(entityId);
		buf.writeByte(effect.getByte("Id"));
		buf.writeByte(effect.getByte("Amplifier"));
		buf.writeInt(effect.getInteger("Duration"));
	}
	
	
	public static class ThisHandler implements IMessageHandler<MessageApplyEffectToEntity, IMessage>
	{
		@Override
		public IMessage onMessage(MessageApplyEffectToEntity message, MessageContext ctx)
		{
			ModNetWrapper.sheduleOnServer(ctx, () -> 
			{
				Entity e = ctx.getServerHandler().player.getServerWorld() .getEntityByID(message.entityId);
				if (e instanceof EntityLivingBase) ((EntityLivingBase)e).addPotionEffect(PotionEffect.readCustomPotionEffectFromNBT(message.effect)); 
			});
			
			return null;
		}
	}
}





