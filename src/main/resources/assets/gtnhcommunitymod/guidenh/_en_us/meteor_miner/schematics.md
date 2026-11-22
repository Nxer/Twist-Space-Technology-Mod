---
navigation:
  title: Miner schematics
  parent: index.md
  position: 10
  icon: TwistSpaceTechnology:MetaItem01:45
item_ids:
  - TwistSpaceTechnology:MetaItem01:45
  - TwistSpaceTechnology:MetaItem01:46
  - TwistSpaceTechnology:MetaItem01:47
---

# Miner schematics

Put the schematic that matches the structure tier in the [miner controller](index.md). The tier determines how much the laser cuts per cycle and which Fortune level it applies.

<ItemImage id="TwistSpaceTechnology:MetaItem01:45" scale="2" />
<ItemImage id="TwistSpaceTechnology:MetaItem01:46" scale="2" />
<ItemImage id="TwistSpaceTechnology:MetaItem01:47" scale="2" />

| Schematic | Mined per cycle | Fortune |
| --- | --- | --- |
| T1 | One block | None by default |
| T2 | One row | III |
| T3 | One horizontal layer | IV |

## Add Fortune to a tier 1 miner

Fit an **ULV Input Bus** below the controller and place a pickaxe in it. The Pickaxe of the Core grants Fortune I, the Bound Pickaxe grants Fortune II, and the Terra Shatterer grants Fortune III. Tier 2 and tier 3 miners apply their listed Fortune automatically.
