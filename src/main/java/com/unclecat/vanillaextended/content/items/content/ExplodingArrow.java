package com.unclecat.vanillaextended.content.items.content;

import com.unclecat.vanillaextended.content.entities.EntityExplodingArrow;
import com.unclecat.vanillaextended.main.Main;
import com.unclecat.vanillaextended.utils.IHasModel;

import net.minecraft.block.BlockDispenser;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ExplodingArrow extends ItemArrow implements IHasModel
{
	public ExplodingArrow()
	{
		super();
		
		setRegistryName("exploding_arrow");
		setUnlocalizedName("exploding_arrow");
								
		Main.ITEMS.add(this);
	}
	

	
	@Override
	public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter)
    {
        return new EntityExplodingArrow(worldIn, shooter);
    }
	
	
	@Override
	public void registerModels()
	{
		Main.proxy.registerItemRenderer(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		
	}
}
	
	
//	public static class ThisDespenseBehavior implements IBehaviorDispenseItem
//	{
//
//		@Override
//		public ItemStack dispense(IBlockSource source, ItemStack stack)
//		{
//			EnumFacing facing = source.getBlockState().getValue(BlockDispenser.FACING);
//			double x = source.getX() + facing.getFrontOffsetX() + 0.0;
//			double y = source.getY() + facing.getFrontOffsetY() + 0.0;
//			double z = source.getZ() + facing.getFrontOffsetZ() + 0.0;
//			
//			float yaw = 0;
//			float pitch = 0;
//			
//			switch (facing.getAxis())
//			{
//			case X:
//				yaw = facing.getFrontOffsetX() == 1 ? -90.0f : 90.0f;
//				break;
//				
//			case Y:
//				pitch = facing.getFrontOffsetY() * 90;
//				break;
//				
//			case Z:
//				yaw = facing.getFrontOffsetZ() == 1 ? 0.0f : 180.0f;
//				break;
//				
//			default:
//				break;
//				
//			} 
//			
//			EntityExplodingArrow arrow = Main.EXPLODING_ARROW.createArrow(worldIn, stack, shooter) new EntityExplodingArrow(source.getWorld(), x, y, z);
//			arrow.shoot(arrow, pitch, yaw, 0.0f, 0.1f, 0.4f);
//			source.getWorld().spawnEntity(arrow);
//			
//			stack.shrink(1);
//			
//			return stack;
//		}
//		
//	}
//}
