package com.unclecat.vanillaextended.content.items.content;

import java.util.List;

import com.unclecat.vanillaextended.main.Main;
import com.unclecat.vanillaextended.utils.IHasModel;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

public class ChargingBreaker extends ItemPickaxe implements IHasModel
{
	public static ToolMaterial toolMaterialchargingPickaxe = EnumHelper.addToolMaterial("chargingPickaxe",  3, 3580, 0.05F, 3, 25);
	public static final int MAX_CAHRGE = 2048;
	
	
	public ChargingBreaker()
	{
		super(toolMaterialchargingPickaxe);
		
		setCreativeTab(CreativeTabs.TOOLS);
		setRegistryName("charging_breaker");
		setUnlocalizedName("charging_pickaxe");
		
		Main.ITEMS.add(this);
	}
	
	
	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state)
	{
		NBTTagCompound nbt  = stack.getTagCompound();
		try 
		{
			if (nbt != null && state.getBlock().getRegistryName().toString().contentEquals(nbt.getString("ChargeBlock")) && state.getBlock().getPickBlock(state, null, null, null, null).getItemDamage() == nbt.getInteger("ChargeBlockDamage"))
			{
				return nbt.getInteger("ChargePower") * (float)Math.pow(3.2F, state.getBlock().getHarvestLevel(state)) / 40;
			}
		} catch (Exception e) { }
		
		return toolMaterialchargingPickaxe.getEfficiency();
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		
		if (nbt == null) tooltip.add(TextFormatting.RED + "" + TextFormatting.ITALIC + I18n.translateToLocal("item.charging_pickaxe.noBlockBound"));
		else
		{
			ItemStack itemStack = new ItemStack(Block.getBlockFromName(nbt.getString("ChargeBlock")));
			itemStack.setItemDamage(nbt.getInteger("ChargeDamage"));
			
			int power = nbt.getInteger("ChargePower");
			tooltip.add(TextFormatting.AQUA + I18n.translateToLocal(itemStack.getUnlocalizedName() + ".name") + " " + (power >= MAX_CAHRGE ? TextFormatting.DARK_PURPLE : TextFormatting.BLUE) + "" + TextFormatting.ITALIC + power);
		}
	}
	

	@Override
	public void registerModels() 
	{
		Main.proxy.registerItemRenderer(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));	
	}
}
