# Cargo

*Cargo* is an upcoming Minecraft mod for Fabric 1.19.3+, adding multi-block trains in a vanilla-friendly style. Currently, the mod is in early stages of development, and many features are still incomplete. The planned features for the first release are listed here.

## Train Tracks

Train tracks resemble a larger version of minecart rails, with three differences:
- Placement is on an eight-direction grid as opposed to the four possible directions of minecart rails, meaning that tracks can be placed diagonally relative to the block grid
- Slopes are relatively gradual, covering three blocks of horizontal distance for every block of vertical distance
- Junctions and redstone-controlled switches can be placed to control complicated track intersections

The system is designed with the goal of not making minecart rails obsolete, and as such, the two have semi-distinct use cases. Minecarts will still have the advantage for short-range transportation of small amounts of goods, due to the cheapness of placement and the ease of navigating tight spaces and sharp bends. Trains, however will have a distinct advantage for long-range transportation, due to higher capacity and higher speed.

**Status:** *Track placement works fairly well, apart from a few significant bugs with switches. Entity movement following tracks is currently unimplemented.*

## Train Cars

The base of any train car is an **undercarriage**, which is placed along a straight track with wheels and couplers on each end, anywhere from four to twelve blocks long. The undercarriage provides a two-block-wide building surface centered above the tracks, meaning a half-block offset from the usual placement grid.

**Status:** *The offset building surface mostly works, and a test of the entity conversion method was successful. Some textures are placeholders.*

### Steam Locomotive
A basic locomotive that consumes coal, similar to a furnace minecart. The number of boiler blocks making up the locomotive defines the maximum speed.

**Status:** unimplemented.

### Boxcar
A basic freight car that acts as a mobile storage unit. Each hull block making up the boxcar provides nine slots of storage. Ideal for base relocations and for carrying the less common products of large mining trips.

**Status:** the hull blocks are in the game but do not have inventories.

### Hopper Car
A freight car with three times the storage density of a boxcar, with the tradeoff that it can only carry one item. Ideal for carrying the most common products of large mining trips, or for whenever a huge amount of a specific item is needed. A roof block can be used for making covered hopper cars.

**Status:** the basic hull blocks are in the game but do not have inventories and do not correctly set their blockstates when placed. Textures are placeholders. The roof blocks are unimplemented.

### Tank Car
A freight car for transporting fluids. Each hull block making up the tank car provides 27 buckets of capacity.

**Status:** the hull blocks are in the game but do not have inventories. Textures are placeholders.

## Possible future features
- **Diesel locomotives** and **redstone locomotives** are planned, along with corresponding power sources.
- **Passenger cars** are planned but not guaranteed.
- Other types of vehicle are also planned, including **bicycles**, **motorcycles**, **cars**, and **trucks**.
- **Construction cranes** are planned as a means of moving blocks, including **shipping containers** to be moved on and off trucks and flatbed trains.
