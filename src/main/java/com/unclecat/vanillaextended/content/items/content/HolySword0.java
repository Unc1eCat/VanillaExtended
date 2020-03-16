package com.unclecat.vanillaextended.content.items.content;

import com.unclecat.vanillaextended.content.items.ToolSword;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

@Deprecated
public class HolySword0 extends ToolSword
{
	public static ToolMaterial holySwordMaterial = EnumHelper.addToolMaterial("holy_sword", 2, 42069, 6.0f, 0.0f, 35);
	

	public HolySword0() 
	{
		super("holy_sword", holySwordMaterial);
	}
	
	
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
		NBTTagCompound compound = stack.getTagCompound();
		
		if (compound == null)
		{
			compound = new NBTTagCompound();
		}
		
		if (!compound.hasKey("attackDamage"))
		{
			compound.setDouble("attackDamage", 1.0);
		}
		if (compound.getDouble("attackDamage") < 1.0)
		{
			return;
		}
		compound.setDouble("attackDamage", compound.getDouble("attackDamage") - 0.03);
		stack.setTagCompound(compound);
    }
	
	
	/*@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) 
	{
		double d0 = player.posX;
		double d1 = player.posY + 2;
		double d2 = player.posZ;
		double distance = player.renderOffsetX - 2; //how far from parent's center;
		float f1 = player.renderYawOffset + 10;
		d2 = d2 + distance * MathHelper.cos(-f1 * 0.017453292F - (float)Math.PI);
		d0 = d0 + distance * MathHelper.sin(-f1 * 0.017453292F - (float)Math.PI);
		
		
		Minecraft.getMinecraft().world.spawnParticle(particleType, d0, d1, d2, xSpeed, ySpeed, zSpeed, parameters);
		return false;
	}*/
	
	
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        super.hitEntity(stack, target, attacker);
		  
		NBTTagCompound compound = stack.getTagCompound();
		
		if (compound == null)
		{
			compound = new NBTTagCompound();
		}        
		
        if (!compound.hasKey("attackDamage"))
		{
			compound.setDouble("attackDamage", 1.0);
		}
        
        target.setHealth(target.getHealth() - (float) compound.getDouble("attackDamage"));
        //System.out.println(Float.toString(target.getHealth()) + "   " + Double.toString(compound.getDouble("attackDamage")));
        attacker.attackEntityFrom(DamageSource.MAGIC, 2);
        
        compound.setDouble("attackDamage", (compound.getDouble("attackDamage") + 2.0));
        
        stack.setTagCompound(compound);
        
        return true;
    }	
}
