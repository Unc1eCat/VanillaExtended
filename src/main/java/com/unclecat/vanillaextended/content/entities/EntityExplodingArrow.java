package com.unclecat.vanillaextended.content.entities;

import com.unclecat.vanillaextended.main.Main;
import com.unclecat.vanillaextended.main.References;

import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityExplodingArrow extends EntityArrow
{
	public EntityExplodingArrow(World worldIn, double x, double y, double z)
	{
		super(worldIn, x, y, z);
	}
	public EntityExplodingArrow(World worldIn, EntityLivingBase shooter)
	{
		super(worldIn, shooter);
	}
	public EntityExplodingArrow(World worldIn)
	{
		super(worldIn);
	}
	
	
	
	@Override
	protected ItemStack getArrowStack()
	{
		return new ItemStack(Main.EXPLODING_ARROW, 1);
	}

	@Override
	protected void onHit(RayTraceResult raytraceResultIn)
	{
		if	(world.isRemote) return;
		
		if (raytraceResultIn.entityHit instanceof EntityCreeper) 
		{
			((EntityCreeper)raytraceResultIn.entityHit).ignite();
			world.removeEntity(this);
			return;
		}
		
		world.createExplosion(this, posX, posY, posZ, 2.5f, true);
		
		world.removeEntity(this);
	}
	
	
	@Override
	public void spawnRunningParticles()
	{
		double d0 = world.rand.nextFloat() * 0.3 + posX;
		double d1 = world.rand.nextFloat() * 0.3 - 0.2 + posY;
		double d2 = world.rand.nextFloat() * 0.3 + posZ;
		
		world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, true, d0, d1, d2, 0.06, 0.0, 0.06);
	}
	
	
	public static class ThisRenderer extends RenderArrow<EntityExplodingArrow>
	{

		public ThisRenderer(RenderManager renderManagerIn)
		{
			super(renderManagerIn);
		}

		@Override
		protected ResourceLocation getEntityTexture(EntityExplodingArrow entity)
		{
			return new ResourceLocation(References.ENTITIES_TEXUTRES_DIR + "exploding_arrow.png");
		}
		
	}
}
