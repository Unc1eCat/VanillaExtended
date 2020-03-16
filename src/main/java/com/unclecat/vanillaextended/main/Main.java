package com.unclecat.vanillaextended.main;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;

import com.google.common.base.Predicate;
import com.unclecat.vanillaextended.content.blocks.BlockBase;
import com.unclecat.vanillaextended.content.blocks.content.BlockOfRepeaters;
import com.unclecat.vanillaextended.content.blocks.content.BlockRedstoneEnhancer;
import com.unclecat.vanillaextended.content.blocks.content.BlockRedstoneGate;
import com.unclecat.vanillaextended.content.blocks.content.BlockRedstoneSensor;
import com.unclecat.vanillaextended.content.blocks.content.ExperienceBlock;
import com.unclecat.vanillaextended.content.blocks.content.ForceBlock;
import com.unclecat.vanillaextended.content.blocks.content.ITNTBlock;
import com.unclecat.vanillaextended.content.blocks.content.LuminousBlock;
import com.unclecat.vanillaextended.content.blocks.content.MultiplexingDropper;
import com.unclecat.vanillaextended.content.blocks.content.OreXP;
import com.unclecat.vanillaextended.content.blocks.content.TileBed;
import com.unclecat.vanillaextended.content.blocks.content.WaffleBlock;
import com.unclecat.vanillaextended.content.enchantments.content.WandSpellBuilder;
import com.unclecat.vanillaextended.content.enchantments.content.WandSpellExperiencePlacer;
import com.unclecat.vanillaextended.content.enchantments.content.WandSpellFireball;
import com.unclecat.vanillaextended.content.enchantments.content.WandSpellFloating;
import com.unclecat.vanillaextended.content.enchantments.content.WandSpellForceShield;
import com.unclecat.vanillaextended.content.enchantments.content.WandSpellLumin;
import com.unclecat.vanillaextended.content.enchantments.content.WandSpellPowerPush;
import com.unclecat.vanillaextended.content.enchantments.content.WandSpellQuantumPotion;
import com.unclecat.vanillaextended.content.enchantments.content.WandSpellRay;
import com.unclecat.vanillaextended.content.enchantments.content.WandSpellSoulExtractor;
import com.unclecat.vanillaextended.content.enchantments.content.WandSpellTeleportation;
import com.unclecat.vanillaextended.content.entities.EntityExplodingArrow;
import com.unclecat.vanillaextended.content.entities.EntitySlowmotionArrow;
import com.unclecat.vanillaextended.content.gui.GuiHandler;
import com.unclecat.vanillaextended.content.gui.GuiHandler;
import com.unclecat.vanillaextended.content.gui.GuiHandler;
import com.unclecat.vanillaextended.content.items.DoubleCustomUniversalTrade;
import com.unclecat.vanillaextended.content.items.ItemBase;
import com.unclecat.vanillaextended.content.items.content.ChargingBreaker;
import com.unclecat.vanillaextended.content.items.content.ExplodingArrow;
import com.unclecat.vanillaextended.content.items.content.Extinguisher;
import com.unclecat.vanillaextended.content.items.content.HolySword;
import com.unclecat.vanillaextended.content.items.content.ObsidianWand;
import com.unclecat.vanillaextended.content.items.content.OreFinder;
import com.unclecat.vanillaextended.content.items.content.SlowmotionArrow;
import com.unclecat.vanillaextended.content.items.content.Syringe;
import com.unclecat.vanillaextended.content.items.content.Waffle;
import com.unclecat.vanillaextended.content.recipies.content.ChargingBreakerCharge;
import com.unclecat.vanillaextended.content.recipies.content.OreFinderBindOre;
import com.unclecat.vanillaextended.content.worldgen.GeneratorBase;
import com.unclecat.vanillaextended.main.proxy.CommonProxy;
import com.unclecat.vanillaextended.utils.IHasModel;
import com.unclecat.vanillaextended.utils.ModNetWrapper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

@EventBusSubscriber
@Mod(modid = References.MOD_ID, name = References.NAME, version = References.VERSION, acceptedMinecraftVersions = References.ACCEPTED_VERSION)
public class Main 
{
	public static org.apache.logging.log4j.Logger LOGGER = null;
	

	@Instance
	public static Main instance = new Main();
	
	@SidedProxy(clientSide = References.CLIENT_PROXY_CLASS, serverSide = References.COMMON_PROXY_CLASS)
	public static CommonProxy proxy;
	
	
	//// REGISTRY
	
	public static ArrayList<Item> ITEMS = new ArrayList<Item>();
	public static ArrayList<Block> BLOCKS = new ArrayList<Block>();
	
	public static ItemBase CLOTH = new ItemBase("cloth", CreativeTabs.MATERIALS);
	public static ObsidianWand OBSIDIAN_STICK = new ObsidianWand();
	public static Waffle WAFFLE = new Waffle();
	public static Syringe SYRINGE = new Syringe();
	public static HolySword HOLY_SWORD = new HolySword();
	public static ExplodingArrow EXPLODING_ARROW = new ExplodingArrow();
	public static SlowmotionArrow SLOWMOTION_ARROW = new SlowmotionArrow();
	public static Extinguisher EXTINGUISHER = new Extinguisher();
	public static OreFinder ORE_FINDER = new OreFinder();
	public static ChargingBreaker CHARGING_BREAKER = new ChargingBreaker();
	
	public static MultiplexingDropper MULTIPLEXING_DROPPER = new MultiplexingDropper();
	public static BlockRedstoneSensor REDSTONE_SENSOR = new BlockRedstoneSensor();
	public static LuminousBlock LUMINOUS_BLOCK = new LuminousBlock();
	public static ForceBlock FORCE_BLOCK = new ForceBlock();
	public static ITNTBlock ITNT = new ITNTBlock();
	public static ExperienceBlock EXPERIENCE_BLOCK = new ExperienceBlock();
	public static BlockBase COTTON_WOOL = new BlockBase("cotton_wool_block", CreativeTabs.BUILDING_BLOCKS, SoundType.CLOTH, Material.CLOTH, MapColor.CLOTH, 0.2f, 0.5f, true);      
	public static BlockBase FEATHER_BLOCK = new BlockBase("feather_block", CreativeTabs.BUILDING_BLOCKS, SoundType.SNOW, Material.CLOTH, MapColor.CLOTH, 0.2f, 0.5f, true);
	public static TileBed TILE_BED = new TileBed();
	public static OreXP ORE_XP = new OreXP();
	public static BlockOfRepeaters POWERED_BLOCK_OF_REPEATERS = new BlockOfRepeaters("powered_block_of_repeaters",true);
	public static BlockOfRepeaters UNPOWERED_BLOCK_OF_REPEATERS = new BlockOfRepeaters("unpowered_block_of_repeaters", false);
	public static BlockRedstoneGate POWERED_REDSTONE_GATE = new BlockRedstoneGate("powered_redstone_gate", true);
	public static BlockRedstoneGate UNPOWERED_REDSTONE_GATE = new BlockRedstoneGate("unpowered_redstone_gate", false);
	public static BlockRedstoneEnhancer REDSTONE_ENHANCER = new BlockRedstoneEnhancer();
	public static WaffleBlock WAFFLE_BLOCK = new WaffleBlock();	
	
	public static DoubleCustomUniversalTrade TRADE_HOLY_SWORD = new DoubleCustomUniversalTrade(new ItemStack(Items.GOLDEN_APPLE, 14), 3,  new ItemStack(OBSIDIAN_STICK, 10), 2, new ItemStack(HOLY_SWORD, 1), 1); 
	
	//// REGISTRY END
	
	// BIG TODO: Assets
	// TODO: Remove unnecessary logs and comments 
	// TODO: Fix experience decreasing function
	// TODO: Test and post on CurseForge!!!!!! DM that boy that has been excited for the mod!!!!!!!!!!!
	// TODO: Remove ability to apply wand spells to all enchantablie items
	
	// 0.2 version
	// TODO: Add saving(economy) and speed boost enchantments for obsidian wand
	// TODO: Usage for Cloth and Feather block
	// TODO: Multiplexing dropper that stores specific amount of specific item
	// TODO: Distributing hopper 
	// TODO: Parachute 
	// DONE: Quantum potion spell that drinks gives player potion effects from potion held in off-hand but doesn't consumes the potion
	// DONE: Ore finder
	// TODO: Nether furnace working on Soulsand
	// TODO: Tile door
	// TODO: Door and chest lock and key
	// DONE: Chargeable pickaxe that charges for some block and breaks it faster than other which it breaks extremely slow
	// TODO: Spectral sword
	// TODO: Lightning spell
	// TODO: Redstone indicator - displays redstone power  
	// TODO: Quantum item stacks(enchantment) that can be stored in multiple inventories and perform actions but affected item will be onlu one
	// TODO: Experience disorder potion effect that slowly consumes experience
	
	// Maybe...
	// Redstone crossway or something like this
	
	
	// Oh I should've set up Git
	
	@SubscribeEvent
	public void onCreeperExplosion(ExplosionEvent event)
	{
		Explosion expl = event.getExplosion();
		
		if (!(expl.getExplosivePlacedBy() instanceof EntityCreeper)) return;
		
		Vec3d pos = expl.getPosition();
		java.util.List<Entity> entites = event.getWorld().getEntitiesInAABBexcluding(expl.getExplosivePlacedBy(), new AxisAlignedBB(pos.subtract(4.5, 4.5, 4.5), pos.addVector(4.5, 4.5, 4.5)), new Predicate<Entity>() 
		{ 
			
			@Override
			public boolean apply(Entity input)
			{
				return input instanceof EntityCreeper;
			} 
		});
		
		for (Entity i : entites)
		{
			((EntityCreeper)i).ignite();
			((EntityCreeper)i).setEntityInvulnerable(true);//
			i.motionY += 0.1;
		}
	}
	
	public void serverStarting(FMLServerStartingEvent event)
	{
//		event.registerServerCommand(new ApplyEditAndContinueMinecraftLevelCommand());
	}
	
	
	@SubscribeEvent
	public static void onItemRegister(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(ITEMS.toArray(new Item[0]));
	}
	
	@SubscribeEvent
	public static void onBlockRegister(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(BLOCKS.toArray(new Block[0]));
		TileEntity.register(References.MOD_ID + ":redstone_gate", com.unclecat.vanillaextended.content.blocks.content.BlockRedstoneGate.ThisTileEntity.class);
		TileEntity.register(References.MOD_ID + ":block_of_repeaters", BlockOfRepeaters.ThisTileEntity.class);
		TileEntity.register(References.MOD_ID + ":multiplexing_dropper", MultiplexingDropper.ThisTileEntity.class);
	}
	
	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent event)
	{
		for (Item item : ITEMS)
		{
			if (item instanceof IHasModel)
			{
				((IHasModel) item).registerModels();
			}
		}
		for (Block block : BLOCKS)
		{
			if (block instanceof IHasModel)
			{
				((IHasModel) block).registerModels();
			}
		}	
	}

	@SubscribeEvent
	public static void onRecipeRegister(RegistryEvent.Register<IRecipe> event)
	{
		event.getRegistry().register(new OreFinderBindOre());
		event.getRegistry().register(new ChargingBreakerCharge());
	}
	
	
	
	@EventHandler
	public static void PreInit(FMLPreInitializationEvent event)
	{
		LOGGER = LogManager.getLogger(); // Logger init
				
		GameRegistry.registerWorldGenerator(new GeneratorBase(), 2);	
		
		MinecraftForge.EVENT_BUS.register(instance);
		
		
//		proxy.registerRenderer(BlockOfRepeaters.ThisTileEntity.class, new BlockOfRepeatersRenderer());
		
		proxy.initTheArrow();
	}
	
	
	@EventHandler
	public static void init(FMLInitializationEvent event)
	{
		ModNetWrapper.init();
		proxy.registerItemColors();
		
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
				
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Main.EXPLODING_ARROW, new BehaviorProjectileDispense()
        {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn)
            {
            	EntityExplodingArrow arrow = new EntityExplodingArrow(worldIn, position.getX(), position.getY(), position.getZ());
            	arrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return arrow;
            }
        });
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Main.SLOWMOTION_ARROW, new BehaviorProjectileDispense()
        {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn)
            {
            	EntitySlowmotionArrow arrow = new EntitySlowmotionArrow(worldIn, position.getX(), position.getY(), position.getZ());
            	arrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return arrow;
            }
            protected float getProjectileInaccuracy()
            {
                return 2.0F;
            }
        });
		
		TRADE_HOLY_SWORD.register("minecraft:smith", 3, 1);
		
		EntityRegistry.registerModEntity(new ResourceLocation(References.MOD_ID + ":exploding_arrow"), EntityExplodingArrow.class, "exploding_arrow", 420, Main.instance, 64, 20, true);
		EntityRegistry.registerModEntity(new ResourceLocation(References.MOD_ID + ":slowmotion_arrow"), EntitySlowmotionArrow.class, "glowing_arrow", 421, Main.instance, 64, 20, true);

		ForgeRegistries.ENCHANTMENTS.register(new WandSpellFireball());
		ForgeRegistries.ENCHANTMENTS.register(new WandSpellPowerPush());
		ForgeRegistries.ENCHANTMENTS.register(new WandSpellTeleportation());
		ForgeRegistries.ENCHANTMENTS.register(new WandSpellFloating());
		ForgeRegistries.ENCHANTMENTS.register(new WandSpellRay());
		ForgeRegistries.ENCHANTMENTS.register(new WandSpellSoulExtractor());
		ForgeRegistries.ENCHANTMENTS.register(new WandSpellForceShield());
		ForgeRegistries.ENCHANTMENTS.register(new WandSpellBuilder());
		ForgeRegistries.ENCHANTMENTS.register(new WandSpellLumin());
		ForgeRegistries.ENCHANTMENTS.register(new WandSpellExperiencePlacer());
		ForgeRegistries.ENCHANTMENTS.register(new WandSpellQuantumPotion());
	}
	
	
	@EventHandler
	public static void PostInit(FMLPostInitializationEvent event)
	{
		
	}
	
}
