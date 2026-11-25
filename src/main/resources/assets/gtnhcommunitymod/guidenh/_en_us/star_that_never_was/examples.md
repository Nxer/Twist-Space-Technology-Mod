---
navigation:
  title: Worked examples
  parent: index.md
  position: 6
  icon: gregtech:gt.blockmachines:19804
---

# Worked examples

All three runs use the same meteor. Its original radius is 32 and its base Life Essence cost is 10,000 L. The filler chance is 0%, so the entire yield is a single ore affected by Fortune. Use a T3 miner with Fortune IV and default overclock settings, with 1 A of UHV supplied to the mining overclock calculation. Each run has enough materials and ME storage.

| Run | Logical radius | Ordinary UV drones | Actual parallel |
| --- | --- | --- | --- |
| A. First run | 32 | 0 | 1 |
| B. Add a fleet | 32 | 24 | 8 |
| C. Expand with the same fleet | 64 | 24 | 8 |

## Supplies at startup

| Batch cost | A | B | C |
| --- | --- | --- | --- |
| Meteor focuses | 1 | 8 | 8 |
| Mineral Seed Cores | 0 | 0 | 1,536 |
| Octuple Compressed Cobblestone | 1 | 8 | 63 |
| Life Essence (L) | 10,000 | 80,000 | 625,357 |
| Helium plasma (L) | 0 | 846,000 | 1,677,600 |

Keep B's 24 ordinary UV drones and stock the supplies in column C to expand from radius 32 to 64 at 8 parallel. Each parallel costs 192 cores, for a total of 1,536. Stone and essence are rounded up once for the entire batch using its exact voxel volume.

## Time and yield

Each mining unit takes 8 ticks at 122,880 EU/t in all three runs. The average ore rates assume continuous operation and clear output after the containment field opens. They include the 40-tick interval between meteors.

| Time or yield | A | B | C |
| --- | --- | --- | --- |
| Mining (ticks) | 520 | 705 | 1,398 |
| Formation to last cut (ticks) | 694 | 879 | 1,638 |
| Total ore (items) | 431,283 | 3,450,264 | 26,970,552 |
| Average ore (items/s) | 11,752 | 75,087 | 321,461 |
| Relative to Starcore Miner | 2.39% | 15.28% | 65.40% |

The reference is a default Starcore Miner without an Astral Array Fabricator, producing 491,520 items/s across all ores. For a meteor with several ores, apply their individual weights and account for filler separately. [Back to operating formulas](formulas.md).
