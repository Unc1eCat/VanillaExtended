package com.unclecat.vanillaextended.content.enchantments.content;

import java.util.Random;

import com.unclecat.vanillaextended.content.enchantments.WandSpellBase;
import com.unclecat.vanillaextended.utils.ModMath;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class WandSpellTeleportation extends WandSpellBase
{
	Random rand = new Random();

	public WandSpellTeleportation()
	{
		super("teleportation", 20, Rarity.RARE);
	}
	
	
	
	@Override
	public int getMaxLevel()
	{
		return 1;
	}
	@Override
	public int getMinLevel()
	{
		return 1;
	}
	
	
	public static BlockPos getSafeLocation(World worldIn, BlockPos pos, int tries)
	{
		int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();

        for (int l = 0; l <= 1; ++l)
        {
            int i1 = i - 1 * l - 1;
            int j1 = k - 1;
            int k1 = i1 + 2;
            int l1 = j1 + 2;

            for (int i2 = i1; i2 <= k1; ++i2)
            {
                for (int j2 = j1; j2 <= l1; ++j2)
                {
                    BlockPos blockpos = new BlockPos(i2, j, j2);

                    if (worldIn.getBlockState(pos.down()).isTopSolid() && !worldIn.getBlockState(pos).getMaterial().isSolid() && !worldIn.getBlockState(pos.up()).getMaterial().isSolid())
                    {
                        if (tries <= 0)
                        {
                            return blockpos;
                        }

                        --tries;
                    }
                }
            }
        }

        return pos;
	}
	
	
	@Override
	public EnumActionResult onItemRightCick(int lvl, EntityPlayer player, World worldIn, EnumHand hand)
	{
		if (hasEnoughExpToCast(player, lvl))
		{
			consumeExpForCast(player, lvl);
		} else
		{
			return EnumActionResult.FAIL;
		}
		
		try 
		{			
			Vec3d lookAt = ModMath.getVectorForRotation(player.rotationPitch, player.rotationYawHead);
			
			// Get position player is looking at
			BlockPos tpTo = ForgeHooks.rayTraceEyes(player, 640).getBlockPos().up();//(new Vec3d(player.posX, player.posY + player.eyeHeight, player.posZ), lookAt).getBlockPos().up();
			tpTo = getSafeLocation(worldIn, tpTo, 8);
			
			// You can't teleport further than 128 blocks away
			if (player.getDistance(tpTo.getX(), tpTo.getY(), tpTo.getZ()) > 128)
			{
				return EnumActionResult.SUCCESS; 
			}
				
			// Create beautiful FX
			worldIn.playSound(player, tpTo.getX(), tpTo.getY(), tpTo.getZ(), SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport")), SoundCategory.PLAYERS, 16, 1);
	
//			Vec3d ray = new Vec3d(tpTo.getX(), tpTo.getY(), tpTo.getZ());
//			double distance = player.getDistance(tpTo.getX(), tpTo.getY(), tpTo.getZ());
//			for (double i = 0; i <= distance; i += 0.01)
//			{
//				ray = ray.subtract(new Vec3d(lookAt.x * i, lookAt.y * i, lookAt.z * i));
//				worldIn.spawnParticle(EnumParticleTypes.PORTAL, true, ray.x + rand.nextDouble(), ray.y + rand.nextDouble(), ray.z + rand.nextDouble(), 0, 0, 0);
//			}
			
			// Move player there
			player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("resistance"), 20, 255, false, false));
			((EntityPlayerMP)player).connection.setPlayerLocation(tpTo.getX(), tpTo.getY(), tpTo.getZ(), player.rotationYaw, player.rotationPitch);
			
		} catch (Throwable e)
		{
			return EnumActionResult.FAIL;
		}
		return EnumActionResult.SUCCESS;
	}
}
