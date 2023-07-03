package evannakita.cargo;

import evannakita.cargo.block.ContainerBlock;
import evannakita.cargo.block.ContainerDoorBlock;
import evannakita.cargo.block.FireboxBlock;
import evannakita.cargo.block.HeadlampBlock;
import evannakita.cargo.block.HullBlock;
import evannakita.cargo.block.JackBlock;
import evannakita.cargo.block.RedstoneBatteryBlock;
import evannakita.cargo.block.RoofBlock;
import evannakita.cargo.block.TrackWithCouplerBlock;
import evannakita.cargo.block.TrackWithUndercarriageBlock;
import evannakita.cargo.block.TrackWithWheelsBlock;
import evannakita.cargo.block.TrainJunctionBlock;
import evannakita.cargo.block.TrainStructureBlock;
import evannakita.cargo.block.TrainSwitchBlock;
import evannakita.cargo.block.TrainTrackBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.sound.BlockSoundGroup;

public class ModBlocks {

	public static final Block ASPHALT = new Block(
		AbstractBlock.Settings.create()
			.requiresTool()
			.strength(1.8f, 1.8f)
			.sounds(BlockSoundGroup.TUFF)
	);

	public static final Block BOXCAR_ROOF = new RoofBlock(
		AbstractBlock.Settings.create()
			.strength(3.5F, 3.5F)
			.sounds(BlockSoundGroup.METAL)
			.nonOpaque()
	);

	public static final Block CONTAINER = new ContainerBlock(
		AbstractBlock.Settings.copy(Blocks.CHEST)
			.strength(3.5F, 3.5F)
			.sounds(BlockSoundGroup.METAL)
	);

    public static final Block CONTAINER_DOOR = new ContainerDoorBlock(
		AbstractBlock.Settings.create()
			.strength(3.5F, 3.5F)
			.sounds(BlockSoundGroup.METAL)
			.nonOpaque()
	);

    public static final Block FIREBOX = new FireboxBlock(
		AbstractBlock.Settings.create()
			.strength(3.5F, 3.5F)
			.sounds(BlockSoundGroup.METAL)
	);

	public static final Block GONDOLA = new HullBlock(
		AbstractBlock.Settings.create()
			.strength(3.5F, 3.5F)
			.sounds(BlockSoundGroup.METAL)
			.nonOpaque()
	);

	public static final Block HEADLAMP = new HeadlampBlock(
		AbstractBlock.Settings.create()
			.strength(3.5F, 3.5F)
			.sounds(BlockSoundGroup.METAL)
			.nonOpaque()
	);

	public static final Block JACK = new JackBlock(
		AbstractBlock.Settings.create()
			.strength(3.5F,3.5F)
			.sounds(BlockSoundGroup.METAL)
			.nonOpaque()
	);

	public static final Block REDSTONE_BATTERY = new RedstoneBatteryBlock(
		AbstractBlock.Settings.create()
			.strength(3.5F,3.5F)
			.sounds(BlockSoundGroup.STONE)
	);

	public static final Block RUBBER_BLOCK = new Block(
		AbstractBlock.Settings.create()
			.strength(0.0F,0.0F)
			.sounds(BlockSoundGroup.FROGLIGHT)
	);

	public static final Block SMOKESTACK = new RoofBlock(
		AbstractBlock.Settings.create()
			.strength(3.5F, 3.5F)
			.sounds(BlockSoundGroup.METAL)
	);

	public static final Block STEAM_WHISTLE = new RoofBlock(
		AbstractBlock.Settings.create()
			.strength(2.0F, 2.0F)
			.sounds(BlockSoundGroup.METAL)
			.nonOpaque()
	);

	public static final Block TANK = new HullBlock(
		AbstractBlock.Settings.create()
			.strength(3.5F, 3.5F)
			.sounds(BlockSoundGroup.METAL)
			.nonOpaque()
	);

	public static final Block TANK_HATCH = new RoofBlock(
		AbstractBlock.Settings.create()
			.strength(3.5F, 3.5F)
			.sounds(BlockSoundGroup.METAL)
			.nonOpaque()
	);

	public static final Block TRAIN_JUNCTION = new TrainJunctionBlock(
		AbstractBlock.Settings.create()
			.strength(2.0F,2.0F)
			.sounds(BlockSoundGroup.WOOD)
			.nonOpaque()
	);

	public static final Block TRAIN_STRUCTURE_BLOCK = new TrainStructureBlock(
		AbstractBlock.Settings.create()
			.strength(2.0F,2.0F)
			.nonOpaque()
	);

	public static final Block TRAIN_SWITCH = new TrainSwitchBlock(
		AbstractBlock.Settings.create()
			.strength(2.0F,2.0F)
			.sounds(BlockSoundGroup.WOOD)
			.nonOpaque()
	);

	public static final Block TRAIN_TRACKS = new TrainTrackBlock(
		AbstractBlock.Settings.create()
			.strength(2.0F,2.0F)
			.sounds(BlockSoundGroup.WOOD)
			.nonOpaque()
	);

	public static final Block TRACK_WITH_COUPLER = new TrackWithCouplerBlock(
		AbstractBlock.Settings.create()
			.strength(2.0F, 2.0F)
			.sounds(BlockSoundGroup.METAL)
			.nonOpaque()
	);

	public static final Block TRACK_WITH_UNDERCARRIAGE = new TrackWithUndercarriageBlock(
		AbstractBlock.Settings.create()
			.strength(2.0F, 2.0F)
			.sounds(BlockSoundGroup.METAL)
			.nonOpaque()
	);

	public static final Block TRACK_WITH_WHEELS = new TrackWithWheelsBlock(
		AbstractBlock.Settings.create()
			.strength(2.0F, 2.0F)
			.sounds(BlockSoundGroup.METAL)
			.nonOpaque()
	);
}
