package com.unclecat.vanillaextended.content.items.content;

import com.unclecat.vanillaextended.content.enchantments.WandSpellBase;
import com.unclecat.vanillaextended.content.items.ItemBase;
import com.unclecat.vanillaextended.main.Main;

import net.minecraft.advancements.critereon.EnchantedItemTrigger;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class ObsidianWand extends ItemBase
{

	public ObsidianWand()
	{
		super("obsidian_stick", CreativeTabs.TOOLS);
		
		setMaxDamage(420);
	}
	
	
	
	@Override
	public int getItemStackLimit(ItemStack stack)
	{
		return stack.isItemDamaged() || stack.isItemEnchanted() ? 1 : 64;
	}
	
	@Override
	public boolean isDamageable()
	{
		return true;
	}
	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return stack.isItemDamaged();
	}
	
	@Override
	public boolean isRepairable()
	{
		return true;
	}
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		return OreDictionary.itemMatches(new ItemStack(Blocks.OBSIDIAN), repair, false);
	}
	
	
	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) 
	{
		NBTTagList list = ItemEnchantedBook.getEnchantments(book);
		
		if (list.tagCount() > 1 || list.tagCount() <= 0)
		{
			return false; // You can apply only one spell enchantment
		}
		
		if (!(Enchantment.getEnchantmentByID( ((NBTTagCompound)list.getCompoundTagAt(1)).getInteger("id")) instanceof WandSpellBase))
		{
			return true;
		} else
		{
			return false;
		}
	}
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
	{
		if (enchantment instanceof WandSpellBase)
		{
			return ((WandSpellBase)enchantment).canApplyAtEnchantingTable(stack);
		} else
		{
			return false;
		}
	}
	
	@Override
	public int getItemEnchantability(ItemStack stack)
	{
		return 30;
	}
	
	@Override
	public boolean isEnchantable(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public String getHighlightTip(ItemStack item, String displayName)
	{
		NBTTagCompound spellnbt = getWandSpellFromStack(item);
		if (spellnbt != null)
		{
			WandSpellBase spell = (WandSpellBase) Enchantment.getEnchantmentByID( spellnbt.getInteger("id") );
			
			return TextFormatting.LIGHT_PURPLE + I18n.format(spell.getName());
		}
		
		return displayName;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack stack = playerIn.getHeldItem(handIn);
		NBTTagCompound spellnbt = getWandSpellFromStack(stack);
		
		if (spellnbt != null)
		{
			playerIn.setActiveHand(handIn);
			
			WandSpellBase spell = (WandSpellBase) Enchantment.getEnchantmentByID( spellnbt.getInteger("id") );
			int lvl = spellnbt.getInteger("lvl");
			return new ActionResult<ItemStack>(spell.onItemRightCick(lvl, playerIn, worldIn, handIn), stack); 
		}
		
		return new ActionResult(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	{
		NBTTagCompound spellnbt = getWandSpellFromStack(stack);
		
		if (spellnbt != null)
		{
			WandSpellBase spell = (WandSpellBase) Enchantment.getEnchantmentByID( spellnbt.getInteger("id") );
			int lvl = spellnbt.getInteger("lvl");
			stack.damageItem(1, entityLiving);
			return spell.onItemUseFinish(lvl, stack, worldIn, entityLiving);
		}
		
		return stack;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.BOW;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		if (!stack.isItemEnchanted()) return 0;
		
		NBTTagCompound spellnbt = getWandSpellFromStack(stack);
		
		if (spellnbt != null)
		{
			WandSpellBase spell = (WandSpellBase) Enchantment.getEnchantmentByID( spellnbt.getInteger("id") );
			return spell.getUsingDuration(stack, spellnbt.getInteger("lvl"));
		}
		
		return 0;
	}
	
	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase player, int count)
	{
		NBTTagCompound spellnbt = getWandSpellFromStack(stack);
		
		if (spellnbt != null)
		{
			WandSpellBase spell = (WandSpellBase) Enchantment.getEnchantmentByID( spellnbt.getInteger("id") );
			int lvl = spellnbt.getInteger("lvl");
			spell.onUsingTick(lvl, player, EnumHand.MAIN_HAND, stack);
			return; 
		}
		
		return;
	}
	
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX,
			float hitY, float hitZ, EnumHand hand)
	{	
		ItemStack stack = player.getHeldItem(hand);
		NBTTagCompound spellnbt = getWandSpellFromStack(stack);
		
		if (spellnbt != null)
		{
			WandSpellBase spell = (WandSpellBase) Enchantment.getEnchantmentByID( spellnbt.getInteger("id") );
			int lvl = spellnbt.getInteger("lvl");
			stack.damageItem(1, player);
			
			return spell.onItemUseFirst(lvl, player, world, pos, side, hitX, hitY, hitZ, hand);
		}
		
		return EnumActionResult.PASS;
	}
	
	
	/* Safely gets spell from stack */
	public static NBTTagCompound getWandSpellFromStack(ItemStack stack)
	{
		if (stack.getItem() == Main.OBSIDIAN_STICK && stack.isItemEnchanted())
		{			
			return WandSpellBase.getWandSpell(stack);	
		}
		
		return null;
	}
	
	
}
