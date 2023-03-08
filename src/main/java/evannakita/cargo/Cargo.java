package evannakita.cargo;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import evannakita.cargo.block.BicycleFrameBlock;
import evannakita.cargo.block.JackBlock;
import evannakita.cargo.block.RedstoneBatteryBlock;
import evannakita.cargo.block.RefineryBlock;
import evannakita.cargo.block.TrainAxleBlock;
import evannakita.cargo.block.TrainJunctionBlock;
import evannakita.cargo.block.TrainSwitchBlock;
import evannakita.cargo.block.TrainTrackBlock;
import evannakita.cargo.block.TrainWheelBlock;
import evannakita.cargo.block.entity.JackBlockEntity;
import evannakita.cargo.block.entity.RefineryBlockEntity;
import evannakita.cargo.entity.BicycleEntity;
import evannakita.cargo.entity.BogieEntity;
import evannakita.cargo.recipe.RefiningRecipe;
import evannakita.cargo.screen.RefineryScreenHandler;

public class Cargo implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("cargo");
	public static final String MOD_ID = "cargo";

	// Asphalt
	public static final Block ASPHALT = new Block(
		FabricBlockSettings.of(Material.STONE)
		.strength(1.8F,1.8F)
		.sounds(BlockSoundGroup.TUFF)
	);

	// Bicycle
	public static final EntityType<BicycleEntity> BICYCLE = Registry.register(
		Registries.ENTITY_TYPE,
		new Identifier(MOD_ID, "bicycle"),
		FabricEntityTypeBuilder.<BicycleEntity>create(SpawnGroup.MISC, BicycleEntity::new).build()
	);

	// Bicycle Frame
	public static final BicycleFrameBlock BICYCLE_FRAME = new BicycleFrameBlock(
		FabricBlockSettings.of(Material.METAL)
		.strength(3.5F,3.5F)
		.sounds(BlockSoundGroup.METAL)
		.nonOpaque()
	);

	// Bicycle Wheel
	public static final Item BICYCLE_WHEEL = new Item(
		new Item.Settings()
	);
	
	// Bitumen
	public static final Item BITUMEN = new Item(
		new Item.Settings()
	);
	
	// Bogie
	public static final EntityType<BogieEntity> BOGIE = Registry.register(
		Registries.ENTITY_TYPE,
		new Identifier(MOD_ID, "bogie"),
		FabricEntityTypeBuilder.<BogieEntity>create(SpawnGroup.MISC, BogieEntity::new).build()
	);

	// Bucket of Diesel
	public static final Item DIESEL_BUCKET = new Item(
		new Item.Settings()
	);
	
	// Bucket of Latex
	public static final Item LATEX_BUCKET = new Item(
		new Item.Settings()
	);
	
	// Bucket of Petroleum
	public static final Item PETROLEUM_BUCKET = new Item(
		new Item.Settings()
	);
	
	// Jack
	public static final JackBlock JACK = new JackBlock(
		FabricBlockSettings.of(Material.METAL)
		.strength(3.5F,3.5F)
		.sounds(BlockSoundGroup.METAL)
		.nonOpaque()
	);

	public static final BlockEntityType<JackBlockEntity> JACK_ENTITY = Registry.register(
		Registries.BLOCK_ENTITY_TYPE,
		new Identifier(MOD_ID, "jack"),
		FabricBlockEntityTypeBuilder.create(JackBlockEntity::new, JACK).build()
	);

	// Redstone Battery
	public static final RedstoneBatteryBlock REDSTONE_BATTERY = new RedstoneBatteryBlock(
		FabricBlockSettings.of(Material.METAL)
		.strength(3.5F,3.5F)
		.sounds(BlockSoundGroup.STONE)
	);

	// Refinery
	public static final RefineryBlock REFINERY = new RefineryBlock(
		FabricBlockSettings.of(Material.METAL)
		.strength(3.5F,3.5F)
		.sounds(BlockSoundGroup.METAL)
	);

	public static final BlockEntityType<RefineryBlockEntity> REFINERY_ENTITY = Registry.register(
		Registries.BLOCK_ENTITY_TYPE,
		new Identifier(MOD_ID, "refinery"),
		FabricBlockEntityTypeBuilder.create(RefineryBlockEntity::new, REFINERY).build()
	);

	public static final ScreenHandlerType<RefineryScreenHandler> REFINERY_SCREEN_HANDLER = Registry.register(
		Registries.SCREEN_HANDLER,
		new Identifier(MOD_ID, "refinery"),
		new ScreenHandlerType<RefineryScreenHandler>(RefineryScreenHandler::new)
	);

	public static final RecipeType<RefiningRecipe> REFINING_RECIPE = Registry.register(
		Registries.RECIPE_TYPE,
		new Identifier(MOD_ID, "refinery"),
		new RecipeType<RefiningRecipe>() {
			@Override
			public String toString() {
				return "refinery";
			}
		}
	);

	// Rubber
	public static final Item RUBBER = new Item(
		new Item.Settings()
	);
	
	// Rubber Block
	public static final Block RUBBER_BLOCK = new Block(
		FabricBlockSettings.of(Material.DECORATION)
		.strength(0.0F,0.0F)
		.sounds(BlockSoundGroup.FROGLIGHT)
	);
	
	// Train Axle
	public static final Item TRAIN_AXLE = new Item(
		new Item.Settings()
	);

	public static final TrainAxleBlock TRAIN_AXLE_BLOCK = new TrainAxleBlock(
		FabricBlockSettings.of(Material.METAL)
		.strength(2.0F, 2.0F)
		.sounds(BlockSoundGroup.METAL)
		.nonOpaque()
	);

	// Train Junction
	public static final TrainJunctionBlock TRAIN_JUNCTION = new TrainJunctionBlock(
		FabricBlockSettings.of(Material.METAL)
		.strength(2.0F,2.0F)
		.sounds(BlockSoundGroup.WOOD)
		.nonOpaque()
	);

	// Train Switch
	public static final TrainSwitchBlock TRAIN_SWITCH = new TrainSwitchBlock(
		FabricBlockSettings.of(Material.METAL)
		.strength(2.0F,2.0F)
		.sounds(BlockSoundGroup.WOOD)
		.nonOpaque()
	);

	// Train Tracks
	public static final TrainTrackBlock TRAIN_TRACKS = new TrainTrackBlock(
		FabricBlockSettings.of(Material.METAL)
		.strength(2.0F,2.0F)
		.sounds(BlockSoundGroup.WOOD)
		.nonOpaque()
	);

	// Train Wheel
	public static final TrainWheelBlock TRAIN_WHEEL = new TrainWheelBlock(
		FabricBlockSettings.of(Material.METAL)
		.strength(2.0F, 2.0F)
		.sounds(BlockSoundGroup.METAL)
		.nonOpaque()
	);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
		
		// Asphalt
		Registry.register(Registries.BLOCK, new Identifier("cargo", "asphalt"), ASPHALT);
		Registry.register(Registries.ITEM, new Identifier("cargo", "asphalt"), new BlockItem(ASPHALT, new FabricItemSettings()));

		// Bicycle Frame
		Registry.register(Registries.BLOCK, new Identifier("cargo", "bicycle_frame"), BICYCLE_FRAME);
		Registry.register(Registries.ITEM, new Identifier("cargo", "bicycle_frame"), new BlockItem(BICYCLE_FRAME, new FabricItemSettings()));

		// Bicycle Wheel
		Registry.register(Registries.ITEM, new Identifier("cargo", "bicycle_wheel"), BICYCLE_WHEEL);

		// Bitumen
		Registry.register(Registries.ITEM, new Identifier("cargo", "bitumen"), BITUMEN);

		// Bucket of Diesel
		Registry.register(Registries.ITEM, new Identifier("cargo", "diesel_bucket"), DIESEL_BUCKET);

		// Bucket of Latex
		Registry.register(Registries.ITEM, new Identifier("cargo", "latex_bucket"), LATEX_BUCKET);

		// Bucket of Petroleum
		Registry.register(Registries.ITEM, new Identifier("cargo", "petroleum_bucket"), PETROLEUM_BUCKET);

		// Jack
		Registry.register(Registries.BLOCK, new Identifier("cargo", "jack"), JACK);
		Registry.register(Registries.ITEM, new Identifier("cargo", "jack"), new BlockItem(JACK, new FabricItemSettings()));

		// Redstone Battery
		Registry.register(Registries.BLOCK, new Identifier("cargo", "redstone_battery"), REDSTONE_BATTERY);
		Registry.register(Registries.ITEM, new Identifier("cargo", "redstone_battery"), new BlockItem(REDSTONE_BATTERY, new FabricItemSettings()));

		// Refinery
		Registry.register(Registries.BLOCK, new Identifier("cargo", "refinery"), REFINERY);
		Registry.register(Registries.ITEM, new Identifier("cargo", "refinery"), new BlockItem(REFINERY, new FabricItemSettings()));

		// Rubber
		Registry.register(Registries.ITEM, new Identifier("cargo", "rubber"), RUBBER);

		// Rubber Block
		Registry.register(Registries.BLOCK, new Identifier("cargo", "rubber_block"), RUBBER_BLOCK);
		Registry.register(Registries.ITEM, new Identifier("cargo", "rubber_block"), new BlockItem(RUBBER_BLOCK, new FabricItemSettings()));

		// Train Axle (item)
		Registry.register(Registries.ITEM, new Identifier("cargo", "train_axle"), TRAIN_AXLE);

		// Train Axle (block)
		Registry.register(Registries.BLOCK, new Identifier("cargo", "train_axle_block"), TRAIN_AXLE_BLOCK);

		// Train Junction
		Registry.register(Registries.BLOCK, new Identifier("cargo", "train_junction"), TRAIN_JUNCTION);
		Registry.register(Registries.ITEM, new Identifier("cargo", "train_junction"), new BlockItem(TRAIN_JUNCTION, new FabricItemSettings()));

		// Train Switch
		Registry.register(Registries.BLOCK, new Identifier("cargo", "train_switch"), TRAIN_SWITCH);
		Registry.register(Registries.ITEM, new Identifier("cargo", "train_switch"), new BlockItem(TRAIN_SWITCH, new FabricItemSettings()));

		// Train Tracks
		Registry.register(Registries.BLOCK, new Identifier("cargo", "train_tracks"), TRAIN_TRACKS);
		Registry.register(Registries.ITEM, new Identifier("cargo", "train_tracks"), new BlockItem(TRAIN_TRACKS, new FabricItemSettings()));

		// Train Wheel
		Registry.register(Registries.BLOCK, new Identifier("cargo", "train_wheel"), TRAIN_WHEEL);
		Registry.register(Registries.ITEM, new Identifier("cargo", "train_wheel"), new BlockItem(TRAIN_WHEEL, new FabricItemSettings()));

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(content -> {
			content.addAfter(Items.HONEY_BLOCK,
				RUBBER_BLOCK
			);
		});

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> {
			content.addAfter(Items.HONEY_BLOCK,
				RUBBER_BLOCK
			);
		});

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
			content.addAfter(Items.MINECART,
				TRAIN_AXLE,
				TRAIN_WHEEL
			);
			content.addAfter(Items.ACTIVATOR_RAIL,
				TRAIN_TRACKS,
				TRAIN_SWITCH,
				TRAIN_JUNCTION
			);
		});

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
			content.addAfter(Items.SLIME_BALL,
				RUBBER
			);
			content.addAfter(Items.PHANTOM_MEMBRANE,
				LATEX_BUCKET
			);
		});
	}
}