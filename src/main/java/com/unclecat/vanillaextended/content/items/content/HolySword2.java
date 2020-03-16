package com.unclecat.vanillaextended.content.items.content;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;
import com.unclecat.vanillaextended.content.items.ToolSword;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

@Deprecated
public class HolySword2 extends ToolSword
{
	public static ToolMaterial holySwordMaterial = EnumHelper.addToolMaterial("holy_sword", 2, 42069, 6.0f, 0.0f, 35);
	

	public HolySword2() 
	{
		super("holy_sword", holySwordMaterial);
	}
	
	
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
		NBTTagCompound compound = stack.getTagCompound();
		
		if (compound == null)
		{
			compound = new NBTTagCompound();
			compound.setTag("AttributeModifier", new NBTTagList());
		}        
		
		double newAttackDamage = getAttackAttributeValue(compound) - 0.03;
		
		if (newAttackDamage <= 3.0)
		{
			return;
		}
		
        compound = setAttackAttributeValue(compound, newAttackDamage);
        
		stack.setTagCompound(compound);
    }
	
	
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        super.hitEntity(stack, target, attacker);
		  
		NBTTagCompound compound = stack.getTagCompound();
		
		if (compound == null)
		{
			compound = new NBTTagCompound();
			compound.setTag("AttributeModifier", new NBTTagList());
		}        
		
        compound = setAttackAttributeValue(compound, getAttackAttributeValue(compound) + 20.0);
        
        attacker.attackEntityFrom(DamageSource.MAGIC, 2);

        stack.setTagCompound(compound);
        
        return true;
    }
	
	// 4(default) will be returned if no attribute modifier has been found
	public static double getAttackAttributeValue(NBTTagCompound stack)
	{
		try
		{
			
			NBTTagList attribList =  stack.getTagList("AttributeModifier", 10);
			NBTTagCompound atcAttrib = null;
			
			for (int i = attribList.tagCount() - 1; i >= 0; i--) // Finds attack damage compound
			{
				atcAttrib = attribList.getCompoundTagAt(i);
				if (atcAttrib.getString("AttributeName") == "generic.attackDamage")
				{
					return atcAttrib.getDouble("Amount");
				}
			}
			
			return 4.0;

		} catch (Exception e)
		{
			return 4.0;
		}
	}
	
	// Null will be returned if any error is occurred
	public static NBTTagCompound setAttackAttributeValue(NBTTagCompound stack, double value)
	{
		try
		{
			
			NBTTagList attribList =  stack.getTagList("AttributeModifier", 10);
			NBTTagCompound atcAttrib = null;
			boolean flag = true; // If it doesn't have attack damage compound
			
			int i = attribList.tagCount() - 1;
			for (; i >= 0; i--) // Finds attack damage compound
			{
				atcAttrib = attribList.getCompoundTagAt(i);
				if (atcAttrib.getString("AttributeName") == "generic.attackDamage")
				{
					flag = false;
					break;
				}
			}
			
			if (flag)
			{
				atcAttrib = new NBTTagCompound();
				atcAttrib.setString("AttributeName", "generic.attackDamage");
				atcAttrib.setString("Name", "generic.attackDamage");
				atcAttrib.setInteger("Operation", 0);
				atcAttrib.setInteger("UUIDLeast", 8000);
				atcAttrib.setInteger("UUIDMost", 4000);
			} else 
			{
				attribList.removeTag(i);
			}
			
			atcAttrib.setDouble("Amount", value);	
			attribList.appendTag(atcAttrib);
			stack.setTag("AttributeModifier", attribList);
			
			return stack;
			
		} catch (Exception e)
		{
			return null;
		}
	}
	
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
    {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
        {
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
        }

        return multimap;
    }
}


