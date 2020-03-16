package com.unclecat.vanillaextended.content.entities;

import com.unclecat.vanillaextended.main.Main;
import com.unclecat.vanillaextended.main.References;

import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySlowmotionArrow extends EntityArrow
{
	public EntitySlowmotionArrow(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
		this.setIsCritical(false);
		setNoGravity(true);
		setKnockbackStrength(-1);
	}
	public EntitySlowmotionArrow(World worldIn, EntityLivingBase shooter)
	{
		super(worldIn, shooter);
		this.setIsCritical(false);
		setNoGravity(true);
		setKnockbackStrength(-1);
	}
	public EntitySlowmotionArrow(World worldIn)
	{
		super(worldIn);
		this.setIsCritical(false);
		setNoGravity(true);
		setKnockbackStrength(-1);
	}
	
	
	@Override
	protected ItemStack getArrowStack()
	{
		return new ItemStack(Main.SLOWMOTION_ARROW, 1);
	}
	
	@Override
	public boolean getIsCritical() 
	{
		return false;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
//	    if (Math.abs(motionY) >= 0.4 || Math.abs(motionX) >= 0.4 || Math.abs(motionZ) >= 0.4) // Slowness
//	    {
//	    	this.motionX /= 4;
//	    	this.motionY /= 4;
//	    	this.motionZ /= 4;
//	    }
		
		if (Math.sqrt(motionX * motionX + motionY * motionY + motionZ * motionZ) > 0.0001) // TODO: Insert in update log 0.2
		{
			motionX /= 4;
	    	motionY /= 4;
	    	motionZ /= 4;
		}
	    
	    this.motionY -= 0.00005000000074505806D; // Gravity
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void spawnRunningParticles()
	{
		if (world.rand.nextInt(3) <= 1) return;
		double d0 = world.rand.nextFloat() + posX - 0.5;
		double d1 = world.rand.nextFloat() - 0.4 + posY;
		double d2 = world.rand.nextFloat() + posZ - 0.5;
		
		world.spawnParticle(EnumParticleTypes.REDSTONE, false, d0, d1, d2, 0, 0, 255);
	}
	
	
	public static class ThisRenderer extends RenderArrow<EntitySlowmotionArrow>
	{

		public ThisRenderer(RenderManager renderManagerIn)
		{
			super(renderManagerIn);
		}

		@Override
		protected ResourceLocation getEntityTexture(EntitySlowmotionArrow entity)
		{
			return new ResourceLocation(References.ENTITIES_TEXUTRES_DIR + "slowmotion_arrow.png");
		}
		
	}
}

