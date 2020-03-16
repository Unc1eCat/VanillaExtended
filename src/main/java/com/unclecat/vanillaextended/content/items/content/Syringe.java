package com.unclecat.vanillaextended.content.items.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import com.google.common.collect.Lists;
import com.unclecat.vanillaextended.content.items.ItemBase;
import com.unclecat.vanillaextended.main.Main;
import com.unclecat.vanillaextended.utils.ModNetWrapper;
import com.unclecat.vanillaextended.utils.messages.MessageApplyEffectToEntity;
import com.unclecat.vanillaextended.utils.messages.MessageMagicAttackEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class Syringe extends ItemBase
{

	public Syringe()
	{
		super("syringe", CreativeTabs.BREWING);
		setMaxStackSize(4);
	}
	
	
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		NBTTagCompound nbt = stack.getTagCompound();

        if (nbt == null) return;
        if (!nbt.hasKey("PotionEffect")) return;
        
		List<PotionEffect> list = new ArrayList<PotionEffect>();
		NBTTagList listNBT = nbt.getTagList("PotionEffect", 10);
        List<Tuple<String, AttributeModifier>> list1 = Lists.<Tuple<String, AttributeModifier>>newArrayList();
        
		for (int i = listNBT.tagCount() - 1; i >= 0; i--)
		{
			list.add(PotionEffect.readCustomPotionEffectFromNBT(listNBT.getCompoundTagAt(i)));
		}

        if (list.isEmpty())
        {
            String s = I18n.translateToLocal("effect.none").trim();
            tooltip.add(TextFormatting.GRAY + s);
        }
        else
        {
            for (PotionEffect potioneffect : list)
            {
                String s1 = I18n.translateToLocal(potioneffect.getEffectName()).trim();
                Potion potion = potioneffect.getPotion();
                Map<IAttribute, AttributeModifier> map = potion.getAttributeModifierMap();

                if (!map.isEmpty())
                {
                    for (Entry<IAttribute, AttributeModifier> entry : map.entrySet())
                    {
                        AttributeModifier attributemodifier = entry.getValue();
                        AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), potion.getAttributeModifierAmount(potioneffect.getAmplifier(), attributemodifier), attributemodifier.getOperation());
                        list1.add(new Tuple(((IAttribute)entry.getKey()).getName(), attributemodifier1));
                    }
                }

                if (potioneffect.getAmplifier() > 0)
                {
                    s1 = s1 + " " + I18n.translateToLocal("potion.potency." + potioneffect.getAmplifier()).trim();
                }

                if (potioneffect.getDuration() > 20)
                {
                    s1 = s1 + " (" + Potion.getPotionDurationString(potioneffect, 1) + ")";
                }

                if (potion.isBadEffect())
                {
                    tooltip.add(TextFormatting.RED + s1);
                }
                else
                {
                    tooltip.add(TextFormatting.BLUE + s1);
                }
            }
        }

        if (!list1.isEmpty())
        {
            tooltip.add("");
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.translateToLocal("potion.whenDrank"));

            for (Tuple<String, AttributeModifier> tuple : list1)
            {
                AttributeModifier attributemodifier2 = tuple.getSecond();
                double d0 = attributemodifier2.getAmount();
                double d1;

                if (attributemodifier2.getOperation() != 1 && attributemodifier2.getOperation() != 2)
                {
                    d1 = attributemodifier2.getAmount();
                }
                else
                {
                    d1 = attributemodifier2.getAmount() * 100.0D;
                }

                if (d0 > 0.0D)
                {
                	tooltip.add(TextFormatting.BLUE + I18n.translateToLocalFormatted("attribute.modifier.plus." + attributemodifier2.getOperation(), ItemStack.DECIMALFORMAT.format(d1), I18n.translateToLocal("attribute.name." + (String)tuple.getFirst())));
                }
                else if (d0 < 0.0D)
                {
                    d1 = d1 * -1.0D;
                    tooltip.add(TextFormatting.RED + I18n.translateToLocalFormatted("attribute.modifier.take." + attributemodifier2.getOperation(), ItemStack.DECIMALFORMAT.format(d1), I18n.translateToLocal("attribute.name." + (String)tuple.getFirst())));
                }
            }
        }
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack)
	{
		return 4;
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stackIn, World worldIn, EntityLivingBase entityLiving)
	{
		EntityPlayer playerIn = null;
		if (entityLiving instanceof EntityPlayer) playerIn = (EntityPlayer)entityLiving;
			else return stackIn;
		
		NBTTagCompound nbt = (NBTTagCompound) stackIn.getTagCompound();	
		
		if (nbt != null && nbt.hasKey("PotionEffect") && !playerIn.isSneaking()) // Refill the syringe or inject it
		{
			if (worldIn.isRemote) return stackIn;
			
			Entity pntEntity = getMouseOver(Minecraft.getMinecraft().getRenderPartialTicks(), 1); // Gets pointed entity
			
			if (pntEntity instanceof EntityLiving) // Who to hit: pointed entity or the player
			{
				addEffectsToEntity(nbt, (EntityLiving) pntEntity);
				ModNetWrapper.WRAPPER.sendToServer(new MessageMagicAttackEntity(pntEntity.getEntityId(), 2));
			} else
			{
				addEffectsToEntity(nbt, (EntityLivingBase)playerIn);
				playerIn.attackEntityFrom(DamageSource.GENERIC, 3);
			}
			
			if (!playerIn.isCreative())
			{
				stackIn.shrink(1);
				if (stackIn.isEmpty())
				{
					return new ItemStack(Main.SYRINGE, 1);
				} else
				{
					playerIn.inventory.addItemStackToInventory(new ItemStack(Main.SYRINGE, 1));
				}
			}
		} else // TODO: If potion effect exist don't add it again
		{			
			ItemStack heldItem = playerIn.getHeldItemOffhand(); // Gets item from off-hand
			
			if (!(heldItem.getItem() instanceof ItemPotion)) return stackIn; // Checks if there is potion in off-hand
			List<PotionEffect> effects = PotionUtils.getPotionFromItem(heldItem).getEffects(); // Gets effects list from the potion
			
			if (effects.isEmpty()) return stackIn;	// If there are effects
			
			if (nbt == null) nbt = new NBTTagCompound();
			else if (nbt.hasKey("PotionEffect") && nbt.getTagList("PotionEffect", 10).tagCount() >= 4) 
				return stackIn; // You can't put more than 4 effects in one syringe
			
			nbt = copyEffectsToSyringeNBT(nbt, effects); // Copies effect from the potion into the syringe
			
			if (!playerIn.isCreative())
			{
				playerIn.setHeldItem(EnumHand.OFF_HAND, new ItemStack(Items.GLASS_BOTTLE, 1)); // Cleans potion bottle from potion
			}
				
			ItemStack stackOut =  new ItemStack(Main.SYRINGE, 1);
			stackOut.setTagCompound(nbt);
			
			stackIn.shrink(1);
			
			if (stackIn.isEmpty())
			{
				return stackOut;
			} else
			{
				playerIn.inventory.addItemStackToInventory(stackOut);
			}

			
			worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.BLOCK_WATERLILY_PLACE, SoundCategory.NEUTRAL, 10, 1.2f);
		}
		return stackIn;
	}
	
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        playerIn.setActiveHand(EnumHand.MAIN_HAND);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(EnumHand.MAIN_HAND));
    }

	
	public static NBTTagCompound copyEffectsToSyringeNBT(NBTTagCompound syringe, List<PotionEffect> effects) // TODO: Make it simply copy NBT of potion bottle but not extract each fucking potion effect and put its properties in newly fucking created effect in syringe
	{
		NBTTagList list = syringe.hasKey("PotionEffect") ? syringe.getTagList("PotionEffect", 10) : new NBTTagList();
		
		if (effects.size() > 4) return syringe; // You can't put more than 4 effects in one syringe
		
		for (int i = effects.size() - 1; i >= 0; i--)
		{	
			NBTTagCompound effectNBT = new NBTTagCompound();
			
			effectNBT.setByte("Id", (byte)Potion.getIdFromPotion(effects.get(i).getPotion()));
			effectNBT.setByte("Amplifier", (byte)effects.get(i).getAmplifier());
			effectNBT.setInteger("Duration", effects.get(i).getDuration());
			
	        list.appendTag(effectNBT);
		}
		
		syringe.setInteger("PotionColor", PotionUtils.getPotionColorFromEffectList(effects)); // Caching color
		syringe.setTag("PotionEffect", list);
		return syringe; 
	}
	
	public static void addEffectsToEntity(NBTTagCompound syringe, EntityLivingBase entityIn)
	{	
		NBTTagList list = syringe.getTagList("PotionEffect", 10);
		
		for (int i = list.tagCount() - 1; i >= 0; i--)
		{
//			entityIn.addPotionEffect(PotionEffect.readCustomPotionEffectFromNBT(list.getCompoundTagAt(i)));
			ModNetWrapper.WRAPPER.sendToServer(new MessageApplyEffectToEntity(list.getCompoundTagAt(i), entityIn.getEntityId()));
		}
	}
	
	
	@Deprecated // Found solution replacing this method
	public PotionEffect getEffectFromItemPotion(ItemStack stack)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt.hasKey("CustomPotionEffects")) // Get from custom effect or from default potion
		{
			return PotionEffect.readCustomPotionEffectFromNBT((NBTTagCompound) nbt.getTagList("CustomPotionEffects", 10).get(0));
		} else if (nbt.hasKey("Potion"))
		{
			//return ((ItemPotion)ItemPotion.getByNameOrId(nbt.getString("Potion"))).;
		}
		return null;
	}
}
