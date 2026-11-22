---
navigation:
  title: The Star That Never Was
  parent: /index.md
  position: 90
  icon: gregtech:gt.blockmachines:19804
item_ids:
  - gregtech:gt.blockmachines:19804
---

# The Star That Never Was

A Mineable **Lie**

A meteor focus selects the virtual star. The controller forms it above the shaft, the Laser Meteor Miner cuts the ore, and the controller sends it to your ME network.

<GameScene width="400" height="240" interactive={true} allowLayerSlider={true} showGrid={false}>
  <ImportStructureLib controller="gregtech:gt.blockmachines:19804" />
  <StarStructureControls />
</GameScene>

The preview's **Tier** corresponds to the blueprint's main channel. **Megastructure** opens on the central shaft. Select 7 for the receiver and upper structure, or 8 for the whole structure.

| Main channel | Receiver glass | Structure and parallel |
| --- | --- | --- |
| 1 (default) | Neutronium Glass | Base structure with one parallel |
| 2 | Infinity Glass | Requires the drone hub and all four nodes, then allows drones to increase parallel |

All 37 tiered glass blocks in the receiver must match. The controller determines the tier from the blocks actually built.

## Install the miner and connect the ports

1. Build the [Laser Meteor Miner](../meteor_miner/index.md) in the shaft and insert its matching schematic in the miner controller. Align the laser beacon directly below the receiver. For a tier 2 or 3 miner, the star controller sits one block above the miner controller.
2. Fit an item input bus, a fluid input hatch and an **ME Output Bus** for ore at **marker 1** on the controller panel. The energy hatch fits at **marker 1** on the panel or **marker 2** on the shaft floor.

## Mine your first star

Choose a focus on Blood Magic's meteor page in NEI. Supply the controller with the focus, Octuple Compressed Cobblestone and Life Essence. Start at the focus's original radius. You can add seed cores later to expand it.

Switch on the star controller and let the containment field open. Once linked, the miner follows the controller's work switch. Watch the display progress from star formation to laser mining. The default batch mode delivers the ore to ME when the run finishes.

After the first run, adjust [ore selection, radius and delivery](mining.md), or upgrade to tier 2 and connect the [drone hub](drones.md) to increase parallel. See the [operating formulas](formulas.md) and [worked examples](examples.md) when sizing your supplies.
