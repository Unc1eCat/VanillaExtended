package com.unclecat.vanillaextended.utils.messages;

import com.google.common.base.Charsets;
import com.unclecat.vanillaextended.content.blocks.content.MultiplexingDropper;
import com.unclecat.vanillaextended.utils.ModNetWrapper;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageMultiplexingDropperPredicateChange implements IMessage
{
	public String predicateItem;
	public BlockPos tePos;
	
	
	public MessageMultiplexingDropperPredicateChange() {}
	
	public MessageMultiplexingDropperPredicateChange(Item predicateItem, BlockPos tePos)
	{
		if (predicateItem == null) this.predicateItem = "null";
		else this.predicateItem = predicateItem.getRegistryName().toString();
		this.tePos = tePos;
	}
	
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		tePos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
		predicateItem = new String(buf.slice(Integer.SIZE / 8, buf.capacity() - Integer.SIZE / 8).array(), Charsets.UTF_8);
	}
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(tePos.getX());
		buf.writeInt(tePos.getY());
		buf.writeInt(tePos.getZ());
		predicateItem.getBytes(Charsets.UTF_8);
	}
	
	
	public static class ThisHandler implements IMessageHandler<MessageMultiplexingDropperPredicateChange, IMessage>
	{

		@Override
		public IMessage onMessage(MessageMultiplexingDropperPredicateChange message, MessageContext ctx)
		{	
			ModNetWrapper.sheduleOnServer(ctx, () -> 
			{ 
				MultiplexingDropper.ThisTileEntity invte = ((MultiplexingDropper.ThisTileEntity)ctx.getServerHandler().player.world.getTileEntity(message.tePos));
				invte.predicateItem = message.predicateItem == "null" || message.predicateItem == null ? null : Item.getByNameOrId(message.predicateItem);
				invte.markDirty();
			});
			
			return null;
		}
		
	}
}
