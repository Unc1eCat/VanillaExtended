package com.unclecat.vanillaextended.utils.messages;

import com.unclecat.vanillaextended.utils.ModNetWrapper;

import io.netty.buffer.ByteBuf;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.DamageSource;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageMagicAttackEntity implements IMessage
{
	public int entityId;
	public float damage;
	
	
	public MessageMagicAttackEntity() {}
	
	public MessageMagicAttackEntity(int entityId, float damage)
	{
		this.entityId = entityId;
		this.damage = damage;
	}
	
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		entityId = buf.readInt(); 
		damage = buf.readFloat();
	}
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(entityId);
		buf.writeFloat(damage);
	}
	
	
	public static class ThisHandler implements IMessageHandler<MessageMagicAttackEntity, IMessage>
	{

		@Override
		public IMessage onMessage(MessageMagicAttackEntity message, MessageContext ctx)
		{	
			ModNetWrapper.sheduleOnServer(ctx, () -> { ctx.getServerHandler().player.getServerWorld().getEntityByID(message.entityId).attackEntityFrom(DamageSource.MAGIC, message.damage); });
			
			return null;
		}
		
	}
}
