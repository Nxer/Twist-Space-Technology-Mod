---
navigation:
  title: Ores, radius and delivery
  parent: index.md
  position: 20
  icon: TwistSpaceTechnology:MetaItem01:50
item_ids:
  - TwistSpaceTechnology:MetaItem01:48
  - TwistSpaceTechnology:MetaItem01:49
  - TwistSpaceTechnology:MetaItem01:50
---

# Ores, radius and delivery

## Choose what the ritual mines

Use Blood Magic's meteor page in NEI to choose a focus. It determines the starting radius and ore distribution. The controller selects the first recognized focus and consumes one per parallel. Supply a single focus type to repeat the same meteor. To change the ore mix or filler, add the corresponding reagent to the recipe inputs. Each reagent takes effect when you supply one per parallel for the whole batch.

Put **Mineral Seed Cores** in the controller's item input bus to increase the virtual star's mining radius.

<ItemImage id="TwistSpaceTechnology:MetaItem01:50" scale="2" />

The controller checks the starting power for the original radius, then selects the highest parallel the fleet and supplies can support at that radius. With parallel fixed, the core count, total input power and remaining Octuple Compressed Cobblestone, Life Essence and drone fuel determine the largest radius it can mine. Ore yield uses the actual mining radius. The animation has its own size limit.

## When ore arrives in ME

| Mode | Delivery |
| --- | --- |
| Batch mining (default) | Delivers all ore at the end. The controller checks room for the whole batch before starting. |
| Stepwise mining | After the star forms and the beam connects, each visible cut delivers its share. The controller shows the current step's yield. |

Both modes give the same total yield and planned duration for a given batch. A mode change takes effect on the next run. If a delivery is waiting for ME storage space, make room for that delivery to continue.

## Pause or abort

Switch off the controller to pause a paid batch, then switch it on to resume. When replacing the miner, link one of the same or higher tier. A tier 1 miner must also meet the batch's original Fortune level.

**Abort current task** sends the fleet to the [hub return bus](drones.md). You keep the ore already delivered, and the remaining yield is lost. The batch consumed its inputs and helium plasma at startup. Leave room in the return bus or network for the fleet. The next batch can start once all drones are back.

[Operating formulas](formulas.md) · [Worked examples](examples.md)
