---
navigation:
  title: Structure and capacity
  parent: index.md
  position: 40
  icon: TwistSpaceTechnology:BottledChaosCapacity:0
item_ids:
  - "TwistSpaceTechnology:BottledChaosCapacity:*"
  - "TwistSpaceTechnology:StabilisationFieldGenerator:*"
  - "TwistSpaceTechnology:EnergySustainmentMatrix:*"
---

# Structure, tiers and capacity

## Choose the core tier

<ItemImage id="TwistSpaceTechnology:StabilisationFieldGenerator:0" scale="3" />
<ItemImage id="TwistSpaceTechnology:EnergySustainmentMatrix:0" scale="3" />

The Stabilisation Field Generators and Energy Sustainment Matrices must have the same tier. Together they set the machine tier: UHV, UEV, UIV, UMV or UXV.

Glass in the central shaft and capacity regions must share a tier at least as high as the machine tier. **Energy hatches cannot exceed the glass tier.** Better glass permits better energy hatches; it does not upgrade the core.

Rotate and inspect layers of the full structure on the [controller page](index.md), or use the blueprint and projector while building.

## Four regions, one capacity budget

<GameScene height="140" interactive={true} showGrid={false}>
  <Block id="TwistSpaceTechnology:BottledChaosCapacity:0" x="0" />
  <Block id="TwistSpaceTechnology:BottledChaosCapacity:1" x="2" />
  <Block id="TwistSpaceTechnology:BottledChaosCapacity:2" x="4" />
  <Block id="TwistSpaceTechnology:BottledChaosCapacity:3" x="6" />
  <Block id="TwistSpaceTechnology:BottledChaosCapacity:4" x="8" />
</GameScene>

Upgrade each region independently, or leave the whole region empty. The scene shows capacity blocks I through V.

| One region | Empty | I | II | III | IV | V |
| --- | --- | --- | --- | --- | --- | --- |
| Capacity | 1 | 4 | 16 | 64 | 256 | 1024 |

Add all four regions: **4** capacity when all are empty, or **4096** with four tier V regions. Capacity funds active routes; it is not item or fluid storage.

Enabled groups use the occupied weight of their credential's routes, **not the frame's capacity limit**. When identical credentials share dedicated inputs, multiply that cost by the number of linked credential hatches.

For example, a credential with route weight 4 in two linked hatches costs 8 capacity, even though only one of those groups runs.

## Modules and expansion

Install at most one of each controller module type. A parallel controller increases both group and structure-module limits: **1 group / 6 slots** without one, then **+2 groups / +6 slots** per level, up to **15 groups / 48 slots**.

Each additional group still needs a credential, machine capabilities and capacity. Adding a credential hatch alone does not provide these.

## Power

Processing pauses when power is insufficient. At UXV, removing all energy hatches enables wireless mode. Each group prepays its energy and runs at its minimum overclocked duration.

Stop the machine and deal with existing jobs before changing the core, power structure or modules.
