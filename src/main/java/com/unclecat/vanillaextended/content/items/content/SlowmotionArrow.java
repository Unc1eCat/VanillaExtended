package com.unclecat.vanillaextended.content.items.content;

import com.unclecat.vanillaextended.content.entities.EntitySlowmotionArrow;
import com.unclecat.vanillaextended.main.Main;
import com.unclecat.vanillaextended.utils.IHasModel;

import net.minecraft.advancements.critereon.EnchantedItemTrigger;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class SlowmotionArrow extends ItemArrow implements IHasModel
{
	public SlowmotionArrow()
	{
		super();
		
		setRegistryName("slowmotion_arrow");
		setUnlocalizedName("slowmotion_arrow");
								
		Main.ITEMS.add(this);
	}
	
	
	@Override
	public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter)
    {
        return new EntitySlowmotionArrow(worldIn, shooter);
    }
	
	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		
	}
}
