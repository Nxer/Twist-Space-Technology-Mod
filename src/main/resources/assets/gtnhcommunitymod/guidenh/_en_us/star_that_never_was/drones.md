---
navigation:
  title: Drone hub
  parent: index.md
  position: 10
  icon: TwistSpaceTechnology:BloodInfusedMiningDrone:7
item_ids:
  - "TwistSpaceTechnology:BloodInfusedMiningDrone:*"
---

# Drone hub

## Upgrade to tier 2

1. Replace all 37 Neutronium Glass blocks in the receiver with Infinity Glass.
2. Set the blueprint's main channel to 2 and build the hub and all four floating nodes. Use Megastructure 7 to preview the upper structure.

Once the tier 2 structure is formed, the controller dispatches drones through the hub to increase parallel.

## Drone dispatch and return

Face the front of the floating hub. All three ports sit in the lower row and face outwards.

<GameScene width="360" height="180" interactive={true} showGrid={false}>
  <ImportStructure src="gtnhcommunitymod:guidenh/assets/structures/star_drone_hub.snbt" />
  <ImportPonder src="gtnhcommunitymod:guidenh/assets/ponder/star_drone_hub_en_us.json" />
</GameScene>

| Port | Use | Bus or hatch |
| --- | --- | --- |
| Left, marker 3 | Dispatch drones | Item input bus with a physical inventory |
| Center, marker 4 | Receive returning drones | Standard or ME output bus |
| Right, marker 5 | Supply helium plasma | Standard, ME or storage-cell fluid input hatch |

Ore leaves through the ME Output Bus on the [star controller](index.md).

## Choose the fleet and supply fuel

Every mining drone in the input bus leaves when a batch starts. **The drones at the highest effective tier set the batch's parallel.** Lower-tier ordinary drones travel with them, burn fuel and need room to return. Sort returning drones by effective tier to keep the next fleet at the intended grade.

At departure, each ordinary drone draws 50 L of helium plasma per scheduled mining tick (1000 L per second). Blood-infused drones fly fuel-free and count one tier above their original grade, up to MAX.

After each full second of mining, every ordinary drone contributing to parallel has an independent 1 in 3600 chance of blood infusion. Its new effective tier applies on the next run.

[Fleet and fuel formulas](formulas.md) · [Worked examples](examples.md)
