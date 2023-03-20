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
import net.minecraft.entity.EntityDimensions;
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

import evannakita.cargo.block.HullBlock;
import evannakita.cargo.block.JackBlock;
import evannakita.cargo.block.RedstoneBatteryBlock;
import evannakita.cargo.block.RefineryBlock;
import evannakita.cargo.block.RoofBlock;
import evannakita.cargo.block.TrackWithWheelsBlock;
import evannakita.cargo.block.TrackWithCouplerBlock;
import evannakita.cargo.block.TrackWithUndercarriageBlock;
import evannakita.cargo.block.TrainJunctionBlock;
import evannakita.cargo.block.TrainStructureBlock;
import evannakita.cargo.block.TrainSwitchBlock;
import evannakita.cargo.block.TrainTrackBlock;
import evannakita.cargo.block.entity.JackBlockEntity;
import evannakita.cargo.block.entity.RefineryBlockEntity;
import evannakita.cargo.entity.BicycleEntity;
import evannakita.cargo.entity.BogieEntity;
import evannakita.cargo.entity.BoxcarEntity;
import evannakita.cargo.item.BicycleItem;
import evannakita.cargo.item.HullItem;
import evannakita.cargo.item.TrainTracksItem;
import evannakita.cargo.recipe.RefiningRecipe;
import evannakita.cargo.screen.RefineryScreenHandler;

public class Cargo implements ModInitializer {
	public static final String MOD_ID = "cargo";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

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

	// Bicycle
	public static final BicycleItem BICYCLE_ITEM = new BicycleItem(
		new Item.Settings()
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
		FabricEntityTypeBuilder.<BogieEntity>create(SpawnGroup.MISC, BogieEntity::new).dimensions(EntityDimensions.fixed(1.5f, 0.875f)).build()
	);

	// Boxcar
	public static final EntityType<BoxcarEntity> BOXCAR = Registry.register(
		Registries.ENTITY_TYPE,
		new Identifier(MOD_ID, "boxcar"),
		FabricEntityTypeBuilder.<BoxcarEntity>create(SpawnGroup.MISC, BoxcarEntity::new).dimensions(EntityDimensions.fixed(1.0f, 3.0f)).build()
	);

	// Boxcar Door
	public static final HullBlock BOXCAR_DOOR = new HullBlock(
		FabricBlockSettings.of(Material.METAL)
		.strength(3.5F, 3.5F)
		.sounds(BlockSoundGroup.METAL)
	);

	// Boxcar Hull
	public static final HullBlock BOXCAR_HULL = new HullBlock(
		FabricBlockSettings.of(Material.METAL)
		.strength(3.5F, 3.5F)
		.sounds(BlockSoundGroup.METAL)
	);

	// Boxcar Roof
	public static final RoofBlock BOXCAR_ROOF = new RoofBlock(
		FabricBlockSettings.of(Material.METAL)
		.strength(3.5F, 3.5F)
		.sounds(BlockSoundGroup.METAL)
		.nonOpaque()
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
	
	// Train Coupler
	public static final Item TRAIN_COUPLER = new Item(
		new Item.Settings()
	);

	// Train Junction
	public static final TrainJunctionBlock TRAIN_JUNCTION = new TrainJunctionBlock(
		FabricBlockSettings.of(Material.METAL)
		.strength(2.0F,2.0F)
		.sounds(BlockSoundGroup.WOOD)
		.nonOpaque()
	);

	// Train Structure Block
	public static final TrainStructureBlock TRAIN_STRUCTURE_BLOCK = new TrainStructureBlock(
		FabricBlockSettings.of(Material.BARRIER)
		.strength(2.0F,2.0F)
		.nonOpaque()
		//.noCollision()
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

	// Train Track With Coupler
	public static final TrackWithCouplerBlock TRACK_WITH_COUPLER = new TrackWithCouplerBlock(
		FabricBlockSettings.of(Material.METAL)
		.strength(2.0F, 2.0F)
		.sounds(BlockSoundGroup.METAL)
		.nonOpaque()
	);

	// Train Track With Undercarriage
	public static final TrackWithUndercarriageBlock TRACK_WITH_UNDERCARRIAGE = new TrackWithUndercarriageBlock(
		FabricBlockSettings.of(Material.METAL)
		.strength(2.0F, 2.0F)
		.sounds(BlockSoundGroup.METAL)
		.nonOpaque()
	);

	// Train Track With Wheels
	public static final TrackWithWheelsBlock TRACK_WITH_WHEELS = new TrackWithWheelsBlock(
		FabricBlockSettings.of(Material.METAL)
		.strength(2.0F, 2.0F)
		.sounds(BlockSoundGroup.METAL)
		.nonOpaque()
	);

	// Train Undercariage
	public static final Item TRAIN_UNDERCARRIAGE = new Item(
		new Item.Settings()
	);

	// Train Wheels
	public static final Item TRAIN_WHEELS = new Item(
		new Item.Settings()
	);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
		
		// Asphalt
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "asphalt"), ASPHALT);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "asphalt"), new BlockItem(ASPHALT, new FabricItemSettings()));

		// Bicycle Frame
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "bicycle_item"), BICYCLE_ITEM);

		// Bicycle Wheel
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "bicycle_wheel"), BICYCLE_WHEEL);

		// Bitumen
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "bitumen"), BITUMEN);

		// Boxcar Door
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "boxcar_door"), BOXCAR_DOOR);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "boxcar_door"), new HullItem(BOXCAR_DOOR, new FabricItemSettings()));

		// Boxcar Hull
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "boxcar_hull"), BOXCAR_HULL);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "boxcar_hull"), new HullItem(BOXCAR_HULL, new FabricItemSettings()));

		// Boxcar Roof
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "boxcar_roof"), BOXCAR_ROOF);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "boxcar_roof"), new HullItem(BOXCAR_ROOF, new FabricItemSettings()));

		// Bucket of Diesel
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "diesel_bucket"), DIESEL_BUCKET);

		// Bucket of Latex
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "latex_bucket"), LATEX_BUCKET);

		// Bucket of Petroleum
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "petroleum_bucket"), PETROLEUM_BUCKET);

		// Jack
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "jack"), JACK);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "jack"), new BlockItem(JACK, new FabricItemSettings()));

		// Redstone Battery
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "redstone_battery"), REDSTONE_BATTERY);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "redstone_battery"), new BlockItem(REDSTONE_BATTERY, new FabricItemSettings()));

		// Refinery
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "refinery"), REFINERY);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "refinery"), new BlockItem(REFINERY, new FabricItemSettings()));

		// Rubber
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "rubber"), RUBBER);

		// Rubber Block
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "rubber_block"), RUBBER_BLOCK);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "rubber_block"), new BlockItem(RUBBER_BLOCK, new FabricItemSettings()));

		// Train Coupler
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "train_coupler"), TRAIN_COUPLER);

		// Train Track With Coupler
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "track_with_coupler"), TRACK_WITH_COUPLER);

		// Train Track With Undercarriage
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "track_with_undercarriage"), TRACK_WITH_UNDERCARRIAGE);

		// Train Track With Wheels
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "track_with_wheels"), TRACK_WITH_WHEELS);

		// Train Junction
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "train_junction"), TRAIN_JUNCTION);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "train_junction"), new BlockItem(TRAIN_JUNCTION, new FabricItemSettings()));

		// Train Switch
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "train_switch"), TRAIN_SWITCH);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "train_switch"), new BlockItem(TRAIN_SWITCH, new FabricItemSettings()));

		// Train Structure Block
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "train_structure_block"), TRAIN_STRUCTURE_BLOCK);

		// Train Tracks
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "train_tracks"), TRAIN_TRACKS);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "train_tracks"), new TrainTracksItem(TRAIN_TRACKS, new FabricItemSettings()));

		// Train Undercarriage
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "train_undercarriage"), TRAIN_UNDERCARRIAGE);

		// Train Wheels
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "train_wheels"), TRAIN_WHEELS);

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
				TRAIN_WHEELS,
				TRAIN_UNDERCARRIAGE,
				TRAIN_COUPLER,
				BOXCAR_HULL,
				BOXCAR_DOOR,
				BOXCAR_ROOF
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