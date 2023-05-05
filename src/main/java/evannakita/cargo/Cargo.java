package evannakita.cargo;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.recipe.CookingRecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import evannakita.cargo.block.entity.ContainerBlockEntity;
import evannakita.cargo.block.entity.JackBlockEntity;
import evannakita.cargo.block.entity.FireboxBlockEntity;
import evannakita.cargo.entity.BicycleEntity;
import evannakita.cargo.entity.BogieEntity;
import evannakita.cargo.entity.FlatbedEntity;
import evannakita.cargo.item.ContainerDoorItem;
import evannakita.cargo.item.HullItem;
import evannakita.cargo.item.TrainTracksItem;
import evannakita.cargo.recipe.RefiningRecipe;
import evannakita.cargo.screen.FireboxScreenHandler;

public class Cargo implements ModInitializer {
	public static final String MOD_ID = "cargo";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// Bicycle
	public static final EntityType<BicycleEntity> BICYCLE = Registry.register(
		Registries.ENTITY_TYPE,
		new Identifier(MOD_ID, "bicycle"),
		FabricEntityTypeBuilder.<BicycleEntity>create(SpawnGroup.MISC, BicycleEntity::new).build()
	);

	// Bogie
	public static final EntityType<BogieEntity> BOGIE = Registry.register(
		Registries.ENTITY_TYPE,
		new Identifier(MOD_ID, "bogie"),
		FabricEntityTypeBuilder.<BogieEntity>create(SpawnGroup.MISC, BogieEntity::new).dimensions(EntityDimensions.fixed(1.5f, 0.875f)).build()
	);

	// Container
	public static final BlockEntityType<ContainerBlockEntity> CONTAINER_BLOCK_ENTITY = Registry.register(
		Registries.BLOCK_ENTITY_TYPE,
		new Identifier(MOD_ID, "container"),
		FabricBlockEntityTypeBuilder.create(ContainerBlockEntity::new, ModBlocks.CONTAINER).build()
	);

	public static final ScreenHandlerType<Generic3x3ContainerScreenHandler> BOXCAR_SCREEN_HANDLER = Registry.register(
		Registries.SCREEN_HANDLER,
		new Identifier(MOD_ID, "container"),
		new ScreenHandlerType<Generic3x3ContainerScreenHandler>(Generic3x3ContainerScreenHandler::new)
	);

	// Firebox
	public static final BlockEntityType<FireboxBlockEntity> FIREBOX_ENTITY = Registry.register(
		Registries.BLOCK_ENTITY_TYPE,
		new Identifier(MOD_ID, "firebox"),
		FabricBlockEntityTypeBuilder.create(FireboxBlockEntity::new, ModBlocks.FIREBOX).build()
	);

	public static final ScreenHandlerType<FireboxScreenHandler> FIREBOX_SCREEN_HANDLER = Registry.register(
		Registries.SCREEN_HANDLER,
		new Identifier(MOD_ID, "firebox"),
		new ScreenHandlerType<FireboxScreenHandler>(FireboxScreenHandler::new)
	);

	public static final RecipeType<RefiningRecipe> REFINING = Registry.register(
		Registries.RECIPE_TYPE,
		new Identifier(MOD_ID, "refining"),
		new RecipeType<RefiningRecipe>() {
			@Override
			public String toString() {
				return "refining";
			}
		}
	);

	public static final CookingRecipeSerializer<RefiningRecipe> REFINING_SERIALIZER = Registry.register(
		Registries.RECIPE_SERIALIZER,
		new Identifier(MOD_ID, "refining"),
		new CookingRecipeSerializer<>(RefiningRecipe::new, 200));

	// Flatbed
	public static final EntityType<FlatbedEntity> FLATBED = Registry.register(
		Registries.ENTITY_TYPE,
		new Identifier(MOD_ID, "flatbed"),
		FabricEntityTypeBuilder.<FlatbedEntity>create(SpawnGroup.MISC, FlatbedEntity::new)
			.dimensions(EntityDimensions.changing(3.0f, 1.0f))
			.build()
	);

	// Jack
	public static final BlockEntityType<JackBlockEntity> JACK_ENTITY = Registry.register(
		Registries.BLOCK_ENTITY_TYPE,
		new Identifier(MOD_ID, "jack"),
		FabricBlockEntityTypeBuilder.create(JackBlockEntity::new, ModBlocks.JACK).build()
	);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
		
		// Asphalt
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "asphalt"), ModBlocks.ASPHALT);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "asphalt"), new BlockItem(ModBlocks.ASPHALT, new FabricItemSettings()));

		// Bicycle Frame
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "bicycle_item"), ModItems.BICYCLE);

		// Bicycle Wheel
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "bicycle_wheel"), ModItems.BICYCLE_WHEEL);

		// Bitumen
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "bitumen"), ModItems.BITUMEN);

		// Boxcar Roof
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "boxcar_roof"), ModBlocks.BOXCAR_ROOF);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "boxcar_roof"), new BlockItem(ModBlocks.BOXCAR_ROOF, new FabricItemSettings()));

		// Bucket of Diesel
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "diesel_bucket"), ModItems.DIESEL_BUCKET);

		// Bucket of Latex
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "latex_bucket"), ModItems.LATEX_BUCKET);

		// Bucket of Petroleum
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "petroleum_bucket"), ModItems.PETROLEUM_BUCKET);

		// Container
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "container"), ModBlocks.CONTAINER);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "container"), new HullItem(ModBlocks.CONTAINER, new FabricItemSettings()));

		// Container Door
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "container_door"), ModBlocks.CONTAINER_DOOR);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "container_door"), new ContainerDoorItem(ModBlocks.CONTAINER_DOOR, new FabricItemSettings()));

		// Firebox
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "firebox"), ModBlocks.FIREBOX);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "firebox"), new BlockItem(ModBlocks.FIREBOX, new FabricItemSettings()));

		// Gondola
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "gondola"), ModBlocks.GONDOLA);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "gondola"), new HullItem(ModBlocks.GONDOLA, new FabricItemSettings()));

		// Headlamp
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "headlamp"), ModBlocks.HEADLAMP);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "headlamp"), new HullItem(ModBlocks.HEADLAMP, new FabricItemSettings()));

		// Jack
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "jack"), ModBlocks.JACK);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "jack"), new BlockItem(ModBlocks.JACK, new FabricItemSettings()));

		// Redstone Battery
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "redstone_battery"), ModBlocks.REDSTONE_BATTERY);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "redstone_battery"), new BlockItem(ModBlocks.REDSTONE_BATTERY, new FabricItemSettings()));

		// Rubber
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "rubber"), ModItems.RUBBER);

		// Rubber Block
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "rubber_block"), ModBlocks.RUBBER_BLOCK);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "rubber_block"), new BlockItem(ModBlocks.RUBBER_BLOCK, new FabricItemSettings()));

		// Smokestack
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "smokestack"), ModBlocks.SMOKESTACK);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "smokestack"), new BlockItem(ModBlocks.SMOKESTACK, new FabricItemSettings()));

		// Steam Whistle
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "steam_whistle"), ModBlocks.STEAM_WHISTLE);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "steam_whistle"), new TrainTracksItem(ModBlocks.STEAM_WHISTLE, new FabricItemSettings()));

		// Tank
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "tank"), ModBlocks.TANK);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "tank"), new HullItem(ModBlocks.TANK, new FabricItemSettings()));

		// Tank Hatch
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "tank_hatch"), ModBlocks.TANK_HATCH);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "tank_hatch"), new BlockItem(ModBlocks.TANK_HATCH, new FabricItemSettings()));

		// Train Coupler
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "train_coupler"), ModItems.TRAIN_COUPLER);

		// Train Junction
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "train_junction"), ModBlocks.TRAIN_JUNCTION);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "train_junction"), new BlockItem(ModBlocks.TRAIN_JUNCTION, new FabricItemSettings()));

		// Train Switch
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "train_switch"), ModBlocks.TRAIN_SWITCH);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "train_switch"), new BlockItem(ModBlocks.TRAIN_SWITCH, new FabricItemSettings()));

		// Train Structure Block
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "train_structure_block"), ModBlocks.TRAIN_STRUCTURE_BLOCK);

		// Train Tracks
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "train_tracks"), ModBlocks.TRAIN_TRACKS);
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "train_tracks"), new TrainTracksItem(ModBlocks.TRAIN_TRACKS, new FabricItemSettings()));

		// Train Track With Coupler
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "track_with_coupler"), ModBlocks.TRACK_WITH_COUPLER);

		// Train Track With Undercarriage
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "track_with_undercarriage"), ModBlocks.TRACK_WITH_UNDERCARRIAGE);

		// Train Track With Wheels
		Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "track_with_wheels"), ModBlocks.TRACK_WITH_WHEELS);

		// Train Undercarriage
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "train_undercarriage"), ModItems.TRAIN_UNDERCARRIAGE);

		// Train Wheels
		Registry.register(Registries.ITEM, new Identifier(MOD_ID, "train_wheels"), ModItems.TRAIN_WHEELS);

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(content -> {
			content.addAfter(Items.HONEY_BLOCK,
				ModBlocks.RUBBER_BLOCK.asItem()
			);
		});

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> {
			content.addAfter(Items.HONEY_BLOCK,
				ModBlocks.RUBBER_BLOCK.asItem()
			);
		});

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(content -> {
			content.addAfter(Items.MINECART,
				ModItems.TRAIN_WHEELS,
				ModItems.TRAIN_UNDERCARRIAGE,
				ModItems.TRAIN_COUPLER,
				ModBlocks.TANK.asItem(),
				ModBlocks.FIREBOX.asItem(),
				ModBlocks.SMOKESTACK.asItem(),
				ModBlocks.STEAM_WHISTLE.asItem(),
				ModBlocks.HEADLAMP.asItem(),
				ModBlocks.CONTAINER.asItem(),
				ModBlocks.CONTAINER_DOOR.asItem(),
				ModBlocks.BOXCAR_ROOF.asItem(),
				ModBlocks.GONDOLA.asItem(),
				ModBlocks.TANK_HATCH.asItem()
			);
			content.addAfter(Items.ACTIVATOR_RAIL,
				ModBlocks.TRAIN_TRACKS.asItem(),
				ModBlocks.TRAIN_SWITCH.asItem(),
				ModBlocks.TRAIN_JUNCTION.asItem()
			);
		});

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
			content.addAfter(Items.SLIME_BALL,
				ModItems.RUBBER
			);
			content.addAfter(Items.PHANTOM_MEMBRANE,
				ModItems.LATEX_BUCKET
			);
		});
	}
}