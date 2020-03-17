package com.unclecat.vanillaextended.content.gui.content;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import com.unclecat.vanillaextended.content.blocks.content.MultiplexingDropper;
import com.unclecat.vanillaextended.content.containers.content.MultiplexDropperContainer;
import com.unclecat.vanillaextended.main.References;
import com.unclecat.vanillaextended.utils.ModNetWrapper;
import com.unclecat.vanillaextended.utils.messages.MessageMultiplexingDropperPredicateChange;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import sun.java2d.pipe.RenderingEngine;

public class GuiMultiplexingDropper extends GuiContainer
{	
	public static final ResourceLocation TEXTURE_BACKGROUND = new ResourceLocation(References.MOD_ID + ":textures/gui/container/multiplexing_dropper.png");
	IInventory multiplexingDropper;
	InventoryPlayer plalyerInv;
	
	
	
	public GuiMultiplexingDropper(EntityPlayer player, IInventory multiplexingDropper)
	{
		super(new MultiplexDropperContainer(player, multiplexingDropper));
		this.multiplexingDropper = multiplexingDropper;
		this.plalyerInv = player.inventory;
	}

	
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		mc.getTextureManager().bindTexture(TEXTURE_BACKGROUND);
		drawTexturedModalRect((width - xSize) / 2, (height - ySize) / 2, 0, 0, xSize, ySize);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		GL11.glPushMatrix();
		
		String s = ((MultiplexingDropper.ThisTileEntity)multiplexingDropper).getDisplayName().getUnformattedText();
		fontRenderer.drawString(s, (width - xSize) / 2 - fontRenderer.getStringWidth(s), 3, 4210752);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		if (((MultiplexingDropper.ThisTileEntity)multiplexingDropper).predicateItem != null) itemRender.renderItemIntoGUI(new ItemStack(((MultiplexingDropper.ThisTileEntity)multiplexingDropper).predicateItem), 80, 36);
		GL11.glPopMatrix();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		
		mouseX -= guiLeft;
		mouseY -= guiTop;
		
		if (80 < mouseX && mouseX < 98 && 36 < mouseY && mouseY < 54)
		{
			ItemStack heldStack = plalyerInv.getItemStack();
			if (heldStack != null) ((MultiplexingDropper.ThisTileEntity)multiplexingDropper).predicateItem = heldStack.getItem();
			else ((MultiplexingDropper.ThisTileEntity)multiplexingDropper).predicateItem = null;
			ModNetWrapper.WRAPPER.sendToServer(new MessageMultiplexingDropperPredicateChange(((MultiplexingDropper.ThisTileEntity)multiplexingDropper).predicateItem, ((MultiplexingDropper.ThisTileEntity)multiplexingDropper).getPos()));
			((MultiplexingDropper.ThisTileEntity)multiplexingDropper).markDirty();
		}
	}
}
