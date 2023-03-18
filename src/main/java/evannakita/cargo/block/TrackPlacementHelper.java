package evannakita.cargo.block;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;

import evannakita.cargo.block.enums.JunctionShape;
import evannakita.cargo.block.enums.SwitchShape;
import evannakita.cargo.block.enums.TrackDirection;
import evannakita.cargo.block.enums.TrackShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TrackPlacementHelper {
    protected final World world;
    protected final BlockPos pos;
    protected BlockState state;
    protected final AbstractTrackBlock block;
    protected final List<BlockPos> neighbors = Lists.newArrayList();

    public TrackPlacementHelper(World world, BlockPos pos, BlockState state) {
        this.world = world;
        this.pos = pos;
        this.state = state;
        this.block = (AbstractTrackBlock)state.getBlock();
        if (this.isSwitch()) {
            SwitchShape switchShape = state.get(this.block.getSwitchShapeProperty());
            this.computeNeighbors(switchShape);
        } else if (this.isJunction()) {
            JunctionShape junctionShape = state.get(this.block.getJunctionShapeProperty());
            this.computeNeighbors(junctionShape);
        } else {
            TrackShape trackShape = state.get(this.block.getTrackShapeProperty());
            this.computeNeighbors(trackShape);
        }
    }

    protected boolean isSwitch() {
        return state.getBlock() instanceof TrainSwitchBlock;
    }

    protected boolean isJunction() {
        return state.getBlock() instanceof TrainJunctionBlock;
    }

    public List<BlockPos> getNeighbors() {
        return this.neighbors;
    }

    private void computeNeighbors(TrackShape shape) {
        this.neighbors.clear();
        switch (shape) {
            case NORTH_SOUTH, NORTH_MIDDLE, NORTH_BOTTOM, SOUTH_MIDDLE, SOUTH_BOTTOM: {
                this.neighbors.add(this.pos.north());
                this.neighbors.add(this.pos.south());
                break;
            }
            case EAST_WEST, EAST_MIDDLE, EAST_BOTTOM, WEST_MIDDLE, WEST_BOTTOM: {
                this.neighbors.add(this.pos.west());
                this.neighbors.add(this.pos.east());
                break;
            }
            case NORTHEAST_SOUTHWEST: {
                this.neighbors.add(this.pos.north().east());
                this.neighbors.add(this.pos.south().west());
                break;
            }
            case NORTHWEST_SOUTHEAST: {
                this.neighbors.add(this.pos.north().west());
                this.neighbors.add(this.pos.south().east());
                break;
            }
            case NORTH_SOUTHEAST: {
                this.neighbors.add(this.pos.north());
                this.neighbors.add(this.pos.south().east());
                break;
            }
            case NORTH_SOUTHWEST: {
                this.neighbors.add(this.pos.north());
                this.neighbors.add(this.pos.south().west());
                break;
            }
            case EAST_SOUTHWEST: {
                this.neighbors.add(this.pos.east());
                this.neighbors.add(this.pos.south().west());
                break;
            }
            case EAST_NORTHWEST: {
                this.neighbors.add(this.pos.east());
                this.neighbors.add(this.pos.north().west());
                break;
            }
            case SOUTH_NORTHWEST: {
                this.neighbors.add(this.pos.south());
                this.neighbors.add(this.pos.north().west());
                break;
            }
            case SOUTH_NORTHEAST: {
                this.neighbors.add(this.pos.south());
                this.neighbors.add(this.pos.north().east());
            }
            case WEST_NORTHEAST: {
                this.neighbors.add(this.pos.west());
                this.neighbors.add(this.pos.north().east());
            }
            case WEST_SOUTHEAST: {
                this.neighbors.add(this.pos.north());
                this.neighbors.add(this.pos.south().east());
                break;
            }
            case NORTH_TOP: {
                this.neighbors.add(this.pos.north().up());
                this.neighbors.add(this.pos.south());
                break;
            }
            case EAST_TOP: {
                this.neighbors.add(this.pos.east().up());
                this.neighbors.add(this.pos.west());
                break;
            }
            case SOUTH_TOP: {
                this.neighbors.add(this.pos.north());
                this.neighbors.add(this.pos.south().up());
                break;
            }
            case WEST_TOP: {
                this.neighbors.add(this.pos.east());
                this.neighbors.add(this.pos.west().up());
                break;
            }
        }
    }

    private void computeNeighbors(SwitchShape shape) {
        this.neighbors.clear();
        switch (shape) {
            case NORTH_LEFT: {
                this.neighbors.add(this.pos.north());
                this.neighbors.add(this.pos.south().east());
                this.neighbors.add(this.pos.south());
                break;
            }
            case NORTH_WYE: {
                this.neighbors.add(this.pos.north());
                this.neighbors.add(this.pos.south().east());
                this.neighbors.add(this.pos.south().west());
                break;
            }
            case NORTH_RIGHT: {
                this.neighbors.add(this.pos.north());
                this.neighbors.add(this.pos.south());
                this.neighbors.add(this.pos.south().west());
                break;
            }
            case NORTHEAST_LEFT: {
                this.neighbors.add(this.pos.north().east());
                this.neighbors.add(this.pos.south());
                this.neighbors.add(this.pos.south().west());
                break;
            }
            case NORTHEAST_WYE: {
                this.neighbors.add(this.pos.north().east());
                this.neighbors.add(this.pos.south());
                this.neighbors.add(this.pos.west());
                break;
            }
            case NORTHEAST_RIGHT: {
                this.neighbors.add(this.pos.north().east());
                this.neighbors.add(this.pos.south().west());
                this.neighbors.add(this.pos.west());
                break;
            }
            case EAST_LEFT: {
                this.neighbors.add(this.pos.east());
                this.neighbors.add(this.pos.south().west());
                this.neighbors.add(this.pos.west());
                break;
            }
            case EAST_WYE: {
                this.neighbors.add(this.pos.east());
                this.neighbors.add(this.pos.south().west());
                this.neighbors.add(this.pos.north().west());
                break;
            }
            case EAST_RIGHT: {
                this.neighbors.add(this.pos.east());
                this.neighbors.add(this.pos.west());
                this.neighbors.add(this.pos.north().west());
                break;
            }
            case SOUTHEAST_LEFT: {
                this.neighbors.add(this.pos.south().east());
                this.neighbors.add(this.pos.west());
                this.neighbors.add(this.pos.north().west());
                break;
            }
            case SOUTHEAST_WYE: {
                this.neighbors.add(this.pos.south().east());
                this.neighbors.add(this.pos.west());
                this.neighbors.add(this.pos.north());
                break;
            }
            case SOUTHEAST_RIGHT: {
                this.neighbors.add(this.pos.south().east());
                this.neighbors.add(this.pos.north().west());
                this.neighbors.add(this.pos.north());
                break;
            }
            case SOUTH_LEFT: {
                this.neighbors.add(this.pos.south());
                this.neighbors.add(this.pos.north().west());
                this.neighbors.add(this.pos.north());
                break;
            }
            case SOUTH_WYE: {
                this.neighbors.add(this.pos.south());
                this.neighbors.add(this.pos.north().west());
                this.neighbors.add(this.pos.north().east());
                break;
            }
            case SOUTH_RIGHT: {
                this.neighbors.add(this.pos.south());
                this.neighbors.add(this.pos.north());
                this.neighbors.add(this.pos.north().east());
                break;
            }
            case SOUTHWEST_LEFT: {
                this.neighbors.add(this.pos.south().west());
                this.neighbors.add(this.pos.north());
                this.neighbors.add(this.pos.north().east());
                break;
            }
            case SOUTHWEST_WYE: {
                this.neighbors.add(this.pos.south().west());
                this.neighbors.add(this.pos.north());
                this.neighbors.add(this.pos.east());
                break;
            }
            case SOUTHWEST_RIGHT: {
                this.neighbors.add(this.pos.south().west());
                this.neighbors.add(this.pos.north().east());
                this.neighbors.add(this.pos.east());
                break;
            }
            case WEST_LEFT: {
                this.neighbors.add(this.pos.west());
                this.neighbors.add(this.pos.north().east());
                this.neighbors.add(this.pos.east());
                break;
            }
            case WEST_WYE: {
                this.neighbors.add(this.pos.west());
                this.neighbors.add(this.pos.north().east());
                this.neighbors.add(this.pos.south().east());
                break;
            }
            case WEST_RIGHT: {
                this.neighbors.add(this.pos.west());
                this.neighbors.add(this.pos.east());
                this.neighbors.add(this.pos.south().east());
                break;
            }
            case NORTHWEST_LEFT: {
                this.neighbors.add(this.pos.north().west());
                this.neighbors.add(this.pos.east());
                this.neighbors.add(this.pos.south().east());
                break;
            }
            case NORTHWEST_WYE: {
                this.neighbors.add(this.pos.north().west());
                this.neighbors.add(this.pos.east());
                this.neighbors.add(this.pos.south());
                break;
            }
            case NORTHWEST_RIGHT: {
                this.neighbors.add(this.pos.north().west());
                this.neighbors.add(this.pos.south().east());
                this.neighbors.add(this.pos.south());
                break;
            }
        }
    }

    private void computeNeighbors(JunctionShape shape) {
        this.neighbors.clear();
        switch (shape) {
            case T: {
                this.neighbors.add(this.pos.north());
                this.neighbors.add(this.pos.east());
                this.neighbors.add(this.pos.south());
                this.neighbors.add(this.pos.west());
                break;
            }
            case X: {
                this.neighbors.add(this.pos.north().east());
                this.neighbors.add(this.pos.south().east());
                this.neighbors.add(this.pos.south().west());
                this.neighbors.add(this.pos.north().west());
                break;
            }
            case NS_LEFT: {
                this.neighbors.add(this.pos.north());
                this.neighbors.add(this.pos.south().east());
                this.neighbors.add(this.pos.south());
                this.neighbors.add(this.pos.north().west());
                break;
            }
            case NS_RIGHT: {
                this.neighbors.add(this.pos.north());
                this.neighbors.add(this.pos.north().east());
                this.neighbors.add(this.pos.south());
                this.neighbors.add(this.pos.south().west());
                break;
            }
            case EW_LEFT: {
                this.neighbors.add(this.pos.north().east());
                this.neighbors.add(this.pos.east());
                this.neighbors.add(this.pos.south().west());
                this.neighbors.add(this.pos.west());
                break;
            }
            case EW_RIGHT: {
                this.neighbors.add(this.pos.east());
                this.neighbors.add(this.pos.south().east());
                this.neighbors.add(this.pos.west());
                this.neighbors.add(this.pos.north().west());
                break;
            }
        }
    }

    protected void updateNeighborPositions() {
        for (int i = 0; i < this.neighbors.size(); ++i) {
            TrackPlacementHelper trackPlacementHelper = this.getNeighboringTrack(this.neighbors.get(i));
            if (trackPlacementHelper == null || !trackPlacementHelper.isNeighbor(this)) {
                this.neighbors.remove(i--);
                continue;
            }
            this.neighbors.set(i, trackPlacementHelper.pos);
        }
    }

    @Nullable
    protected TrackPlacementHelper getNeighboringTrack(BlockPos pos) {
        BlockState blockState = this.world.getBlockState(pos);
        if (AbstractTrackBlock.isTrack(blockState)) {
            return new TrackPlacementHelper(this.world, pos, blockState);
        }
        blockState = this.world.getBlockState(pos.up());
        if (AbstractTrackBlock.isTrack(blockState)) {
            return new TrackPlacementHelper(this.world, pos.up(), blockState);
        }
        blockState = this.world.getBlockState(pos.down());
        if (AbstractTrackBlock.isTrack(blockState)) {
            return new TrackPlacementHelper(this.world, pos.down(), blockState);
        }
        return null;
    }

    protected boolean isNeighbor(TrackPlacementHelper other) {
        return this.isNeighbor(other.pos);
    }

    protected boolean isNeighbor(BlockPos pos) {
        for (int i = 0; i < this.neighbors.size(); ++i) {
            BlockPos blockPos = this.neighbors.get(i);
            if (blockPos.getX() != pos.getX() || blockPos.getZ() != pos.getZ()) continue;
            return true;
        }
        return false;
    }

    protected boolean canConnect(TrackPlacementHelper placementHelper) {
        if (this.isSwitch() || this.isJunction()) {
            return this.isNeighbor(placementHelper) || this.neighbors.size() < 3;
        } else {
            return this.isNeighbor(placementHelper) || this.neighbors.size() < 2;
        }
    }

    protected boolean canConnect(BlockPos pos) {
        TrackPlacementHelper placementHelper = this.getNeighboringTrack(pos);
        if (placementHelper == null) {
            return false;
        }
        placementHelper.updateNeighborPositions();
        return placementHelper.canConnect(this);
    }

    protected void computeShape(TrackPlacementHelper placementHelper) {
        boolean powered = world.isReceivingRedstonePower(placementHelper.pos);
        if (placementHelper.isSwitch()) {
            SwitchShape switchShape = placementHelper.state.get(placementHelper.block.getSwitchShapeProperty());
            placementHelper.updateTrackState(powered, false, switchShape);
        } else if (placementHelper.isJunction()) {
            JunctionShape junctionShape = placementHelper.state.get(placementHelper.block.getJunctionShapeProperty());
            placementHelper.updateTrackState(powered, false, junctionShape);
        } else {
            TrackShape trackShape = placementHelper.state.get(placementHelper.block.getTrackShapeProperty());
            placementHelper.updateTrackState(powered, false, trackShape);
        }
    }

    public void updateTrackState(boolean powered, boolean forceUpdate, TrackShape trackShape) {
        boolean N = this.canConnect(this.pos.north());
        boolean NE = this.canConnect(this.pos.north().east());
        boolean E = this.canConnect(this.pos.east());
        boolean SE = this.canConnect(this.pos.south().east());
        boolean S = this.canConnect(this.pos.south());
        boolean SW = this.canConnect(this.pos.south().west());
        boolean W = this.canConnect(this.pos.west());
        boolean NW = this.canConnect(this.pos.north().west());
        byte canConnectCount = 0;
        for (Boolean direction : Lists.newArrayList(N, NE, E, SE, S, SW, W, NW)) {
            if (direction) {
                canConnectCount ++;
            }
        }
        TrackShape trackShape2 = null;
        this.updateNeighborPositions();
        if (this.neighbors.size() < 2 && (
            (N & !trackShape.isPointing(TrackDirection.NORTH)) ||
            (NE & !trackShape.isPointing(TrackDirection.NORTHEAST)) ||
            (E & !trackShape.isPointing(TrackDirection.EAST)) ||
            (SE & !trackShape.isPointing(TrackDirection.SOUTHEAST)) ||
            (S & !trackShape.isPointing(TrackDirection.SOUTH)) ||
            (SW & !trackShape.isPointing(TrackDirection.SOUTHWEST)) ||
            (W & !trackShape.isPointing(TrackDirection.WEST)) ||
            (NW & !trackShape.isPointing(TrackDirection.NORTHWEST))
        )) {
            if (canConnectCount == 1) {
                if (N || S) {
                    trackShape2 = TrackShape.NORTH_SOUTH;
                }
                if (E || W) {
                    trackShape2 = TrackShape.EAST_WEST;
                }
                if (NE || SW) {
                    trackShape2 = TrackShape.NORTHEAST_SOUTHWEST;
                }
                if (NW || SE) {
                    trackShape2 = TrackShape.NORTHWEST_SOUTHEAST;
                }
            } else if (canConnectCount == 2) {
                if (N && S) {
                    trackShape2 = TrackShape.NORTH_SOUTH;
                }
                if (E && W) {
                    trackShape2 = TrackShape.EAST_WEST;
                }
                if (NE && SW) {
                    trackShape2 = TrackShape.NORTHEAST_SOUTHWEST;
                }
                if (NW && SE) {
                    trackShape2 = TrackShape.NORTHWEST_SOUTHEAST;
                }
                if (N && SE) {
                    trackShape2 = TrackShape.NORTH_SOUTHEAST;
                }
                if (N && SW) {
                    trackShape2 = TrackShape.NORTH_SOUTHWEST;
                }
                if (E && SW) {
                    trackShape2 = TrackShape.EAST_SOUTHWEST;
                }
                if (E && NW) {
                    trackShape2 = TrackShape.EAST_NORTHWEST;
                }
                if (S && NW) {
                    trackShape2 = TrackShape.SOUTH_NORTHWEST;
                }
                if (S && NE) {
                    trackShape2 = TrackShape.SOUTH_NORTHEAST;
                }
                if (W && NE) {
                    trackShape2 = TrackShape.WEST_NORTHEAST;
                }
                if (W && SE) {
                    trackShape2 = TrackShape.WEST_SOUTHEAST;
                }
            }
        }
        if (trackShape2 == null) {
            trackShape2 = trackShape;
        } else {
            world.playSound(null, pos, SoundEvents.BLOCK_LADDER_STEP, SoundCategory.BLOCKS, 0.5f, 1.0f);
        }
        if (trackShape2 == TrackShape.NORTH_SOUTH) {
            if (AbstractTrackBlock.isTrack(this.world, this.pos.north())) {
                TrackShape northShape = this.world.getBlockState(this.pos.north()).get(this.block.getTrackShapeProperty());
                if (northShape == TrackShape.NORTH_TOP) {
                    trackShape2 = TrackShape.NORTH_MIDDLE;
                } else if (northShape == TrackShape.NORTH_MIDDLE) {
                    trackShape2 = TrackShape.NORTH_BOTTOM;
                }
            } else if (AbstractTrackBlock.isTrack(this.world, this.pos.north().up())) {
                trackShape2 = TrackShape.NORTH_TOP;
            }
            if (AbstractTrackBlock.isTrack(this.world, this.pos.south())) {
                TrackShape southShape = this.world.getBlockState(this.pos.south()).get(this.block.getTrackShapeProperty());
                if (southShape == TrackShape.SOUTH_TOP) {
                    trackShape2 = TrackShape.SOUTH_MIDDLE;
                } else if (southShape == TrackShape.SOUTH_MIDDLE) {
                    trackShape2 = TrackShape.SOUTH_BOTTOM;
                }    
            } else if (AbstractTrackBlock.isTrack(this.world, this.pos.south().up())) {
                trackShape2 = TrackShape.SOUTH_TOP;
            }
        }
        if (trackShape2 == TrackShape.EAST_WEST) {
            if (AbstractTrackBlock.isTrack(this.world, this.pos.east())) {
                TrackShape eastShape = this.world.getBlockState(this.pos.east()).get(this.block.getTrackShapeProperty());
                if (eastShape == TrackShape.EAST_TOP) {
                    trackShape2 = TrackShape.EAST_MIDDLE;
                } else if (eastShape == TrackShape.EAST_MIDDLE) {
                    trackShape2 = TrackShape.EAST_BOTTOM;
                }
            } else if (AbstractTrackBlock.isTrack(this.world, this.pos.east().up())) {
                trackShape2 = TrackShape.EAST_TOP;
            }
            if (AbstractTrackBlock.isTrack(this.world, this.pos.west())) {
                TrackShape westShape = this.world.getBlockState(this.pos.west()).get(this.block.getTrackShapeProperty());
                if (westShape == TrackShape.WEST_TOP) {
                    trackShape2 = TrackShape.WEST_MIDDLE;
                } else if (westShape == TrackShape.WEST_MIDDLE) {
                    trackShape2 = TrackShape.WEST_BOTTOM;
                }    
            } else if (AbstractTrackBlock.isTrack(this.world, this.pos.west().up())) {
                trackShape2 = TrackShape.WEST_TOP;
            }
        }
        this.computeNeighbors(trackShape2);
        this.state = (BlockState)this.state.with(this.block.getTrackShapeProperty(), trackShape2);
        if (forceUpdate || this.world.getBlockState(this.pos) != this.state) {
            this.world.setBlockState(this.pos, this.state, Block.NOTIFY_ALL);
            for (int i = 0; i < this.neighbors.size(); ++i) {
                TrackPlacementHelper trackPlacementHelper = this.getNeighboringTrack(this.neighbors.get(i));
                if (trackPlacementHelper == null) continue;
                trackPlacementHelper.updateNeighborPositions();
                if (!trackPlacementHelper.canConnect(this)) continue;
                this.computeShape(trackPlacementHelper);
            }
        }
    }

    public void updateTrackState(boolean powered, boolean forceUpdate, SwitchShape switchShape) {
        boolean N = this.canConnect(this.pos.north());
        boolean NE = this.canConnect(this.pos.north().east());
        boolean E = this.canConnect(this.pos.east());
        boolean SE = this.canConnect(this.pos.south().east());
        boolean S = this.canConnect(this.pos.south());
        boolean SW = this.canConnect(this.pos.south().west());
        boolean W = this.canConnect(this.pos.west());
        boolean NW = this.canConnect(this.pos.north().west());
        byte canConnectCount = 0;
        for (Boolean direction : Lists.newArrayList(N, NE, E, SE, S, SW, W, NW)) {
            if (direction) {
                canConnectCount ++;
            }
        }
        SwitchShape switchShape2 = null;
        this.updateNeighborPositions();
        if (this.neighbors.size() < 3 && (
            (N & !switchShape.isPointing(TrackDirection.NORTH)) ||
            (NE & !switchShape.isPointing(TrackDirection.NORTHEAST)) ||
            (E & !switchShape.isPointing(TrackDirection.EAST)) ||
            (SE & !switchShape.isPointing(TrackDirection.SOUTHEAST)) ||
            (S & !switchShape.isPointing(TrackDirection.SOUTH)) ||
            (SW & !switchShape.isPointing(TrackDirection.SOUTHWEST)) ||
            (W & !switchShape.isPointing(TrackDirection.WEST)) ||
            (NW & !switchShape.isPointing(TrackDirection.NORTHWEST))
        )) {
            if (canConnectCount == 1) {
                switch (switchShape.getRoot()) {
                    case NORTH:
                        if (S) {
                            switchShape2 = SwitchShape.NORTH_LEFT;
                        } else if (SE || SW) {
                            switchShape2 = SwitchShape.NORTH_WYE;
                        }
                    case NORTHEAST:
                        if (SW) {
                            switchShape2 = SwitchShape.NORTHEAST_LEFT;
                        } else if (S || W) {
                            switchShape2 = SwitchShape.NORTHEAST_WYE;
                        }
                    case EAST:
                        if (W) {
                            switchShape2 = SwitchShape.EAST_LEFT;
                        } else if (SW || NW) {
                            switchShape2 = SwitchShape.EAST_WYE;
                        }
                    case SOUTHEAST:
                        if (NW) {
                            switchShape2 = SwitchShape.SOUTHEAST_LEFT;
                        } else if (N || W) {
                            switchShape2 = SwitchShape.SOUTHEAST_WYE;
                        }
                    case SOUTH:
                        if (N) {
                            switchShape2 = SwitchShape.SOUTH_LEFT;
                        } else if (NE || NW) {
                            switchShape2 = SwitchShape.SOUTH_WYE;
                        }
                    case SOUTHWEST:
                        if (NE) {
                            switchShape2 = SwitchShape.SOUTHWEST_LEFT;
                        } else if (N || E) {
                            switchShape2 = SwitchShape.SOUTHWEST_WYE;
                        }
                    case WEST:
                        if (E) {
                            switchShape2 = SwitchShape.WEST_LEFT;
                        } else if (NE || SE) {
                            switchShape2 = SwitchShape.WEST_WYE;
                        }
                    case NORTHWEST:
                        if (SE) {
                            switchShape2 = SwitchShape.NORTHWEST_LEFT;
                        } else if (S || E) {
                            switchShape2 = SwitchShape.NORTHWEST_WYE;
                        }
                    default:
                        break;
                }
                if (switchShape2 == null) {
                    if (N) {
                        switchShape2 = SwitchShape.NORTH_WYE;
                    }
                    if (NE) {
                        switchShape2 = SwitchShape.NORTHEAST_WYE;
                    }
                    if (E) {
                        switchShape2 = SwitchShape.EAST_WYE;
                    }
                    if (SE) {
                        switchShape2 = SwitchShape.SOUTHEAST_WYE;
                    }
                    if (S) {
                        switchShape2 = SwitchShape.SOUTH_WYE;
                    }
                    if (SW) {
                        switchShape2 = SwitchShape.SOUTHWEST_WYE;
                    }
                    if (W) {
                        switchShape2 = SwitchShape.WEST_WYE;
                    }
                    if (NW) {
                        switchShape2 = SwitchShape.NORTHWEST_WYE;
                    }
                }
            } else if (canConnectCount == 2) {
                switch (switchShape.getRoot()) {
                    case NORTH: {
                        if (N && NE) {
                            switchShape2 = SwitchShape.SOUTHWEST_LEFT;
                        } else if ((N && SE) || (N && SW)) {
                            switchShape2 = SwitchShape.NORTH_WYE;
                        } else if ((N && S) || (SE && S)) {
                            switchShape2 = SwitchShape.NORTH_LEFT;
                        } else if (N && NW) {
                            switchShape2 = SwitchShape.SOUTHEAST_RIGHT;
                        } else if (E && W) {
                            switchShape2 = SwitchShape.EAST_LEFT;
                        } else if (S && SW) {
                            switchShape2 = SwitchShape.NORTH_RIGHT;
                        }
                    }
                    case NORTHEAST: {
                        if (N && NE) {
                            switchShape2 = SwitchShape.SOUTH_RIGHT;
                        } else if (NE && E) {
                            switchShape2 = SwitchShape.WEST_LEFT;
                        } else if ((NE && S) || (NE && W)) {
                            switchShape2 = SwitchShape.NORTHEAST_WYE;
                        } else if ((NE && SW) || (S && SW)) {
                            switchShape2 = SwitchShape.NORTHEAST_LEFT;
                        } else if (SE && NW) {
                            switchShape2 = SwitchShape.SOUTHEAST_LEFT;
                        } else if (SW && W) {
                            switchShape2 = SwitchShape.NORTHEAST_RIGHT;
                        }
                    }
                    case EAST: {
                        if (N && S) {
                            switchShape2 = SwitchShape.SOUTH_LEFT;
                        } else if (NE && E) {
                            switchShape2 = SwitchShape.SOUTHWEST_RIGHT;
                        } else if (E && SE) {
                            switchShape2 = SwitchShape.NORTHWEST_LEFT;
                        } else if ((E && SW) || (E && NW)) {
                            switchShape2 = SwitchShape.EAST_WYE;
                        } else if ((E && W) || (SW && W)) {
                            switchShape2 = SwitchShape.EAST_LEFT;
                        } else if (W && NW) {
                            switchShape2 = SwitchShape.EAST_RIGHT;
                        }
                    }
                    case SOUTHEAST: {
                        if ((N && SE) || (SE && W)) {
                            switchShape2 = SwitchShape.SOUTHEAST_WYE;
                        } else if (N && NW) {
                            switchShape2 = SwitchShape.SOUTHEAST_RIGHT;
                        } else if (NE && SW) {
                            switchShape2 = SwitchShape.SOUTHWEST_LEFT;
                        } else if (E && SE) {
                            switchShape2 = SwitchShape.WEST_RIGHT;
                        } else if (SE && S) {
                            switchShape2 = SwitchShape.NORTH_LEFT;
                        } else if ((SE && NW) || (W && NW)) {
                            switchShape2 = SwitchShape.SOUTHEAST_LEFT;
                        }
                    }
                    case SOUTH: {
                        if (N && NE) {
                            switchShape2 = SwitchShape.SOUTH_RIGHT;
                        } else if ((N && S) || (N && NW)) {
                            switchShape2 = SwitchShape.SOUTH_LEFT;
                        } else if ((NE && S) || (S && NW)) {
                            switchShape2 = SwitchShape.SOUTH_WYE;
                        } else if (E && W) {
                            switchShape2 = SwitchShape.WEST_LEFT;
                        } else if (SE && S) {
                            switchShape2 = SwitchShape.NORTHWEST_RIGHT;
                        } else if (S && SW) {
                            switchShape2 = SwitchShape.NORTHEAST_LEFT;
                        }
                    }
                    case SOUTHWEST: {
                        if ((N && NE) || (NE && SW)) {
                            switchShape2 = SwitchShape.SOUTHWEST_LEFT;
                        } else if ((N && SW) || (E && SW)) {
                            switchShape2 = SwitchShape.SOUTHWEST_WYE;
                        } else if (NE && E) {
                            switchShape2 = SwitchShape.SOUTHWEST_RIGHT;
                        } else if (SE && NW) {
                            switchShape2 = SwitchShape.NORTHWEST_LEFT;
                        } else if (S && SW) {
                            switchShape2 = SwitchShape.NORTH_RIGHT;
                        } else if (SW && W) {
                            switchShape2 = SwitchShape.EAST_LEFT;
                        }
                    }
                    case WEST: {
                        if (N && S) {
                            switchShape2 = SwitchShape.NORTH_LEFT;
                        } else if ((NE && E) || (E && W)) {
                            switchShape2 = SwitchShape.WEST_LEFT;
                        } else if ((NE && W) || (SE && W)) {
                            switchShape2 = SwitchShape.WEST_WYE;
                        } else if (E && SE) {
                            switchShape2 = SwitchShape.WEST_RIGHT;
                        } else if (SW && W) {
                            switchShape2 = SwitchShape.NORTHEAST_RIGHT;
                        } else if (W && NW) {
                            switchShape2 = SwitchShape.SOUTHEAST_LEFT;
                        }
                    }
                    case NORTHWEST: {
                        if (N && NW) {
                            switchShape2 = SwitchShape.SOUTH_LEFT;
                        } else if (NE && SW) {
                            switchShape2 = SwitchShape.NORTHEAST_LEFT;
                        } else if ((E && SE) || (SE && NW)) {
                            switchShape2 = SwitchShape.NORTHWEST_LEFT;
                        } else if ((E && SW) || (S && NW)) {
                            switchShape2 = SwitchShape.NORTHWEST_WYE;
                        } else if (SE && S) {
                            switchShape2 = SwitchShape.NORTHWEST_RIGHT;
                        } else if (W && NW) {
                            switchShape2 = SwitchShape.EAST_RIGHT;
                        }
                    }
                    default:
                        break;
                }
                if (switchShape2 == null) {
                    if (N && NE) {
                        switchShape2 = SwitchShape.SOUTHWEST_LEFT;
                    } else if (N && E) {
                        switchShape2 = SwitchShape.SOUTHWEST_WYE;
                    } else if (N && W) {
                        switchShape2 = SwitchShape.SOUTHEAST_WYE;
                    } else if (N && NW) {
                        switchShape2 = SwitchShape.SOUTH_LEFT;
                    } else if (NE && E) {
                        switchShape2 = SwitchShape.WEST_LEFT;
                    } else if (NE && SE) {
                        switchShape2 = SwitchShape.WEST_WYE;
                    } else if (NE && NW) {
                        switchShape2 = SwitchShape.SOUTH_WYE;
                    } else if (E && SE) {
                        switchShape2 = SwitchShape.NORTHWEST_LEFT;
                    } else if (E && S) {
                        switchShape2 = SwitchShape.NORTHWEST_WYE;
                    } else if (SE && S) {
                        switchShape2 = SwitchShape.NORTH_LEFT;
                    } else if (SE && SW) {
                        switchShape2 = SwitchShape.NORTH_WYE;
                    } else if (S && SW) {
                        switchShape2 = SwitchShape.NORTHEAST_LEFT;
                    } else if (S && W) {
                        switchShape2 = SwitchShape.NORTHEAST_WYE;
                    } else if (SW && W) {
                        switchShape2 = SwitchShape.EAST_LEFT;
                    } else if (SW && NW) {
                        switchShape2 = SwitchShape.EAST_WYE;
                    } else if (W && NW) {
                        switchShape2 = SwitchShape.SOUTHEAST_LEFT;
                    }
                }
            } else if (canConnectCount == 3) {
                if (N && SE && S) {
                    switchShape2 = SwitchShape.NORTH_LEFT;
                }
                if (N && SE && SW) {
                    switchShape2 = SwitchShape.NORTH_WYE;
                }
                if (N && S && SW) {
                    switchShape2 = SwitchShape.NORTH_RIGHT;
                }
                if (NE && S && SW) {
                    switchShape2 = SwitchShape.NORTHEAST_LEFT;
                }
                if (NE && S && W) {
                    switchShape2 = SwitchShape.NORTHEAST_WYE;
                }
                if (NE && SW && W) {
                    switchShape2 = SwitchShape.NORTHEAST_RIGHT;
                }
                if (E && SW && W) {
                    switchShape2 = SwitchShape.EAST_LEFT;
                }
                if (E && SW && NW) {
                    switchShape2 = SwitchShape.EAST_WYE;
                }
                if (E && W && NW) {
                    switchShape2 = SwitchShape.EAST_RIGHT;
                }
                if (SE && W && NW) {
                    switchShape2 = SwitchShape.SOUTHEAST_LEFT;
                }
                if (N && SE && W) {
                    switchShape2 = SwitchShape.SOUTHEAST_WYE;
                }
                if (N && SE && NW) {
                    switchShape2 = SwitchShape.SOUTHEAST_RIGHT;
                }
                if (N && S && NW) {
                    switchShape2 = SwitchShape.SOUTH_LEFT;
                }
                if (NE && S && NW) {
                    switchShape2 = SwitchShape.SOUTH_WYE;
                }
                if (N && NE && S) {
                    switchShape2 = SwitchShape.SOUTH_RIGHT;
                }
                if (N && NE && SW) {
                    switchShape2 = SwitchShape.SOUTHWEST_LEFT;
                }
                if (N && E && SW) {
                    switchShape2 = SwitchShape.SOUTHWEST_WYE;
                }
                if (NE && E && SW) {
                    switchShape2 = SwitchShape.SOUTHWEST_RIGHT;
                }
                if (NE && E && W) {
                    switchShape2 = SwitchShape.WEST_LEFT;
                }
                if (NE && SE && W) {
                    switchShape2 = SwitchShape.WEST_WYE;
                }
                if (E && SE && W) {
                    switchShape2 = SwitchShape.WEST_RIGHT;
                }
                if (E && SE && NW) {
                    switchShape2 = SwitchShape.NORTHWEST_LEFT;
                }
                if (E && S && NW) {
                    switchShape2 = SwitchShape.NORTHWEST_WYE;
                }
                if (SE && S && NW) {
                    switchShape2 = SwitchShape.NORTHWEST_RIGHT;
                }
            }
        }
        if (switchShape2 == null) {
            switchShape2 = switchShape;
        } else {
            world.playSound(null, pos, SoundEvents.BLOCK_LADDER_STEP, SoundCategory.BLOCKS, 0.5f, 1.0f);
        }
        this.computeNeighbors(switchShape2);
        this.state = (BlockState)this.state.with(this.block.getSwitchShapeProperty(), switchShape2);
        if (forceUpdate || this.world.getBlockState(this.pos) != this.state) {
            this.world.setBlockState(this.pos, this.state, Block.NOTIFY_ALL);
            for (int i = 0; i < this.neighbors.size(); ++i) {
                TrackPlacementHelper trackPlacementHelper = this.getNeighboringTrack(this.neighbors.get(i));
                if (trackPlacementHelper == null) continue;
                trackPlacementHelper.updateNeighborPositions();
                if (!trackPlacementHelper.canConnect(this)) continue;
                this.computeShape(trackPlacementHelper);
            }
        }
    }

    public void updateTrackState(boolean powered, boolean forceUpdate, JunctionShape junctionShape) {
        boolean N = this.canConnect(this.pos.north());
        boolean NE = this.canConnect(this.pos.north().east());
        boolean E = this.canConnect(this.pos.east());
        boolean SE = this.canConnect(this.pos.south().east());
        boolean S = this.canConnect(this.pos.south());
        boolean SW = this.canConnect(this.pos.south().west());
        boolean W = this.canConnect(this.pos.west());
        boolean NW = this.canConnect(this.pos.north().west());
        byte canConnectCount = 0;
        for (Boolean direction : Lists.newArrayList(N, NE, E, SE, S, SW, W, NW)) {
            if (direction) {
                canConnectCount ++;
            }
        }
        JunctionShape junctionShape2 = null;
        this.updateNeighborPositions();
        if (this.neighbors.size() < 3 && (
            (N & !junctionShape.isPointing(TrackDirection.NORTH)) ||
            (NE & !junctionShape.isPointing(TrackDirection.NORTHEAST)) ||
            (E & !junctionShape.isPointing(TrackDirection.EAST)) ||
            (SE & !junctionShape.isPointing(TrackDirection.SOUTHEAST)) ||
            (S & !junctionShape.isPointing(TrackDirection.SOUTH)) ||
            (SW & !junctionShape.isPointing(TrackDirection.SOUTHWEST)) ||
            (W & !junctionShape.isPointing(TrackDirection.WEST)) ||
            (NW & !junctionShape.isPointing(TrackDirection.NORTHWEST))
        )) {
            if (canConnectCount == 1) {
                if (N || E || S || W) {
                    junctionShape2 = JunctionShape.T;
                }
                if (NE || SE || SW || NW) {
                    junctionShape2 = JunctionShape.X;
                }
            } else {
                if ((N && E) || (N && S) || (N && W) || (E && S) || (E && W) || (S && W)) {
                    junctionShape2 = JunctionShape.T;
                }
                if ((NE && SE) || (NE && SW) || (NE && NW) || (SE && SW) || (SE && NW) || (SW && NW)) {
                    junctionShape2 = JunctionShape.X;
                }
                if ((N && NE) || (N && SW) || (NE && S) || (S && SW)) {
                    junctionShape2 = JunctionShape.NS_RIGHT;
                }
                if ((N && SE) || (N && NW) || (SE && S) || (S && NW)) {
                    junctionShape2 = JunctionShape.NS_LEFT;
                }
                if ((NE && E) || (NE && W) || (E && SW) || (SW && W)) {
                    junctionShape2 = JunctionShape.EW_LEFT;
                }
                if ((E && SE) || (E && NW) || (SE && W) || (W && NW)) {
                    junctionShape2 = JunctionShape.EW_RIGHT;
                }
            }
        }
        if (junctionShape2 == null) {
            junctionShape2 = junctionShape;
        } else {
            world.playSound(null, pos, SoundEvents.BLOCK_LADDER_STEP, SoundCategory.BLOCKS, 0.5f, 1.0f);
        }
        this.computeNeighbors(junctionShape2);
        this.state = (BlockState)this.state.with(this.block.getJunctionShapeProperty(), junctionShape2);
        if (forceUpdate || this.world.getBlockState(this.pos) != this.state) {
            this.world.setBlockState(this.pos, this.state, Block.NOTIFY_ALL);
            for (int i = 0; i < this.neighbors.size(); ++i) {
                TrackPlacementHelper trackPlacementHelper = this.getNeighboringTrack(this.neighbors.get(i));
                if (trackPlacementHelper == null) continue;
                trackPlacementHelper.updateNeighborPositions();
                if (!trackPlacementHelper.canConnect(this)) continue;
                this.computeShape(trackPlacementHelper);
            }
        }
    }

    public BlockState getBlockState() {
        return this.state;
    }
}