package com.unclecat.vanillaextended.content.renderers.content;

import com.unclecat.vanillaextended.content.blocks.content.BlockOfRepeaters;
import com.unclecat.vanillaextended.content.blocks.content.BlockOfRepeaters.ThisTileEntity;
import com.unclecat.vanillaextended.content.renderers.models.ModelRedstoneTorchOff;
import com.unclecat.vanillaextended.main.References;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BlockOfRepeatersRenderer extends TileEntitySpecialRenderer<ThisTileEntity> {
	protected static ModelBase TORCH_OFF_MODEL = new ModelRedstoneTorchOff();
	protected static ModelBase TORCH_ON_MODEL;
	protected static ResourceLocation TEXTURE = new ResourceLocation(References.MOD_ID + ":textures/blocks/redstone_torch_off.png");


	public BlockOfRepeatersRenderer() 
	{
	}

	@Override
	public void render(BlockOfRepeaters.ThisTileEntity te, double x, double y, double z, float partialTicks,
			int destroyStage, float alpha) 
	{
		double a = te.delayA;
		double b = te.delayB;
//		EnumFacing facing = getWorld().getBlockState(new BlockPos(x, y, z)).getValue(BlockOfRepeaters.FACING);
		
		GlStateManager.pushMatrix();
			this.bindTexture(TEXTURE);
			
			y += 0.0625 * 14;	
			
			
			if (((BlockOfRepeaters) te.getBlockType()).isPoweredType()) {
				GlStateManager.translate(x + 0.0625 * 4, 0.0625 * 14 + y, z + (14.0 - a) / 16.0);
				GlStateManager.rotate(90f, 0f, 1f, 0f);
				TORCH_OFF_MODEL.render(null, 0, 0, 0, 0, 0, 1);

			} else {
				GlStateManager.translate(x + 0.0625 * 4, 0.0625 * 14 + y, z + (14.0 - a) / 16.0);
				TORCH_OFF_MODEL.render(null, 0, 0, 0, 0, 0, 1);
			}
			
		GlStateManager.popMatrix();
	}
}
