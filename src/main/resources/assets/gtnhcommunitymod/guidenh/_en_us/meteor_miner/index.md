---
navigation:
  title: Laser Meteor Miner
  parent: /index.md
  position: 95
  icon: gregtech:gt.blockmachines:19072
item_ids:
  - gregtech:gt.blockmachines:19072
---

# Laser Meteor Miner

Blood Magic's meteor ritual calls a meteor down above the rig. The Laser Meteor Miner cuts into it from below and sends the drops to its output bus.

<GameScene width="400" height="240" interactive={true} allowLayerSlider={true} showGrid={false}>
  <ImportStructureLib controller="gregtech:gt.blockmachines:19072" />
</GameScene>

Switch tiers in the preview to compare the three rigs and their hatch positions.

## Locate the meteor center

Build the rig to match your chosen [miner schematic](schematics.md). Use a Hologram Projector on the controller and place the Master Ritual Stone and Warded Glass where shown. **The Warded Glass marks the center of the meteor.** It sits directly above the laser beacon, 48 blocks higher than the rig's highest block. Measured from the beacon, that is 51 blocks at T1 and 48 at T2 or T3.

## Mine your first meteor

1. Insert the schematic in the controller slot. Fit a maintenance hatch, an energy hatch and an item output bus. Keep the chunks around the miner and the meteor loaded.
2. Activate the meteor ritual and switch on the miner. It sets the mining radius to fit the meteor, up to 40 blocks from the center. The laser mines from the bottom up, and the controller shows the progress and drops for this meteor.

Leave the miner on when it finishes. It checks the space one block above the Warded Glass. When it finds a block there, it measures the radius again and starts the next run.

## Link to The Star That Never Was

The miner cuts the virtual star formed by [The Star That Never Was](../star_that_never_was/index.md). The star controller sends the ore to ME.
