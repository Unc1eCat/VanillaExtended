package com.unclecat.vanillaextended.content.enchantments;

import java.util.Arrays;
import java.util.List;

import com.unclecat.vanillaextended.main.Main;
import com.unclecat.vanillaextended.main.References;
import com.unclecat.vanillaextended.utils.ModMath;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;

public class WandSpellBase extends Enchantment
{
	/* How many experience will be taken from player per one level of spell for one cast */
	protected int expCost = 0;
	
	
	
	public WandSpellBase(String name, int expCost, Rarity rarityIn)
	{
		super(rarityIn, null, new EntityEquipmentSlot[] {EntityEquipmentSlot.MAINHAND,EntityEquipmentSlot.OFFHAND }); 
		this.expCost = expCost;
		
		name = "wandspell_" + name;
		setName(name);
		setRegistryName(References.MOD_ID, name);
	}


	
	/* Returns only one spell since you can't apply multiple spells to single wand */
	public static NBTTagCompound getWandSpell(ItemStack stack)
	{
		NBTTagList list = stack.getEnchantmentTagList();
		for (NBTBase ench : list)
		{
			if (getEnchantmentByID( ((NBTTagCompound)ench).getInteger("id") ) instanceof WandSpellBase)
			{
				return (NBTTagCompound)ench;
			}
		}
		return null;
	}
	

	@Override
	protected boolean canApplyTogether(Enchantment ench)
	{
		return !(ench instanceof WandSpellBase);
	}
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack)
    {
        return stack.getItem() == Main.OBSIDIAN_STICK;
    }
	@Override
	public boolean canApply(ItemStack stack)
	{
		return stack.getItem() == Main.OBSIDIAN_STICK; 
	}
	
	@Override
	public List<ItemStack> getEntityEquipment(EntityLivingBase entityIn) 
	{
		return Arrays.asList(new ItemStack[] { new ItemStack(Main.OBSIDIAN_STICK, 1) });
	}
	
	
	public boolean hasEnoughExpToCast(EntityPlayer player, int lvl)
	{
		if (player.capabilities.isCreativeMode)
		{
			return true;
		}
		else if (player.experienceTotal >= lvl * expCost)
		{	
			return true;
		} 
		else
		{
			return false;
		}
	}
	public void consumeExpForCast(EntityPlayer player, int lvl)
	{
		ModMath.decreaseExp(player, lvl * expCost);
	}
	
	public boolean consumeIfHasEnoughExp(EntityPlayer player, int lvl)
	{
		if (hasEnoughExpToCast(player, lvl))
		{
			consumeExpForCast(player, lvl);
			return true;
		} else
		{
			return false;
		} 
	}
	
	
	public int getUsingDuration(ItemStack stack, int lvl)
	{
		return 4;
	}
	
	
	public EnumActionResult onItemRightCick(int lvl, EntityPlayer player, World worldIn, EnumHand hand)
	{
		return EnumActionResult.PASS;
	}

	public ItemStack onItemUseFinish(int lvl, ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		return stack;
	}

	public void onUsingTick(int lvl, EntityLivingBase player, EnumHand mainHand, ItemStack stack)
	{
		
	}

	public EnumActionResult onItemUseFirst(int lvl, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX,
			float hitY, float hitZ, EnumHand hand)
	{
		return EnumActionResult.PASS;
	}
}
