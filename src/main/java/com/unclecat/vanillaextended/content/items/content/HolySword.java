package com.unclecat.vanillaextended.content.items.content;

import java.util.Random;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.unclecat.vanillaextended.content.items.ToolSword;
import com.unclecat.vanillaextended.main.References;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

public class HolySword extends ToolSword
{
	public static ToolMaterial holySwordMaterial = EnumHelper.addToolMaterial("holy_sword", 2, 1420, 6.0f, 0.0f, 35);
	

	public HolySword() 
	{
		super("holy_sword", holySwordMaterial);
	}
	
	
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
		NBTTagCompound compound = stack.getTagCompound();
		
		if (compound == null)
		{
			return;
		}
		if (!compound.hasKey(References.MOD_ID + ":attackDamage"))
		{
			return;
		}
		
		if (compound.getDouble(References.MOD_ID + ":attackDamage") < 1.0)
		{
			return;
		}
		
		compound.setDouble(References.MOD_ID + ":attackDamage", compound.getDouble(References.MOD_ID + ":attackDamage") - 0.03);
		stack.setTagCompound(compound);
    }
	
	
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack)
	{
        Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier>create();

        NBTTagCompound compound = stack.getTagCompound();
        
        if (compound == null)
		{
			compound = new NBTTagCompound();
		}        
		
        if (!compound.hasKey(References.MOD_ID + ":attackDamage"))
		{
			compound.setDouble(References.MOD_ID + ":attackDamage", 1.0);
		}
        
        if (slot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", compound.getDouble(References.MOD_ID + ":attackDamage"), 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.800000953674316D, 0));
        }

        return multimap;
    }
	
	
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        super.hitEntity(stack, target, attacker);
		  
		NBTTagCompound compound = stack.getTagCompound();
		
		if (compound == null)
		{
			compound = new NBTTagCompound();
		}        
		
        if (!compound.hasKey(References.MOD_ID + ":attackDamage"))
		{
			compound.setDouble(References.MOD_ID + ":attackDamage", 1.0);
		}
        
        attacker.attackEntityFrom(DamageSource.MAGIC, 2);
        
        compound.setDouble(References.MOD_ID + ":attackDamage", (compound.getDouble(References.MOD_ID + ":attackDamage") + 2.0));
        
        stack.setTagCompound(compound);
        
        return true;
    }	
}
