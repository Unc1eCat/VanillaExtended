package com.unclecat.vanillaextended.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import scala.swing.TextComponent;
import scala.tools.nsc.backend.icode.Primitives.Arithmetic;

public class ModMath
{
	/** Decreases player's experience properly */
	public static void decreaseExp(EntityPlayer player, float amount)
	{		
		if (!player.isServerWorld()) return;
		
        if (player.experienceTotal - amount <= 0)
        {
            player.experienceLevel = 0;
            player.experience = 0;
            player.experienceTotal = 0;
            return;
        }
        
        player.experienceTotal -= amount;

        if (player.experience * (float)player.xpBarCap() < amount)
        {
        	amount -= player.experience * (float)player.xpBarCap();
        	player.experience = 1.0f;
        	player.experienceLevel--;
        }

        while (player.xpBarCap() < amount)
        {
        	amount -= player.xpBarCap();
            player.experienceLevel--;
        }
        
        player.experience -= amount / (float)player.xpBarCap();
//        player.sendMessage(new TextComponentString("Level:  " + player.experienceLevel + "\nExperience in exp bar:  " + player.experience + "\nTotal experience:  " + player.experienceTotal + "\n"));
	}
	
	
	// Just took it from Entity class
	public static Vec3d getVectorForRotation(float pitch, float yaw)
    {
        float f = MathHelper.cos(-yaw * 0.017453292F - (float)Math.PI);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - (float)Math.PI);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3d((double)(f1 * f2), (double)f3, (double)(f * f2));
    }
	
	public static int clip(int val, int min, int max)
	{
		return min > val ? min : (max < val ? max : val);
	}
	public static double clip(double val, double min, double max)
	{
		return min > val ? min : (max < val ? max : val);
	}
	public static float clip(float val, float min, float max)
	{
		return min > val ? min : (max < val ? max : val);
	}
	
	
	public static float getRotationForVector(double x, double z)
	{
        return (float) ((float) Math.atan2(z, x)) * 57.2957795130823208F + 180.0F; // Argh math is hard
	}
	
	public static Vec3d abs(Vec3d a)
	{
		return new Vec3d(Math.abs(a.x), Math.abs(a.y), Math.abs(a.z));
	}
	public static BlockPos abs(BlockPos a)
	{
		return new BlockPos(Math.abs(a.getX()), Math.abs(a.getY()), Math.abs(a.getZ()));
	}
}