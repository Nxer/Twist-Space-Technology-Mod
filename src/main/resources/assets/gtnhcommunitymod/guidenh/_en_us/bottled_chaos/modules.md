---
navigation:
  title: Structure packaging
  parent: index.md
  position: 30
  icon: TwistSpaceTechnology:BottledChaosMachineModule
item_ids:
  - TwistSpaceTechnology:BottledChaosMachineModule
---

# Packaging structure modules

<ItemImage id="TwistSpaceTechnology:BottledChaosMachineModule" scale="3" />

A structure module packages controllers and construction materials, supplying the corresponding recipes and performance. It does not replace a [credential](credentials.md)'s route selection.

## From NEI to a module

1. Open the Bottled Chaos machine structure packaging category in NEI. Select the machine and structure configuration; the result at the top shows the module to be packaged.
2. Place a blank structure module in the controller's packaging workspace. Add the controllers and construction materials to the input slots. Bulk materials can come from **one ME item storage cell**, optionally combined with loose inputs.
3. Supply exact quantities for the entire batch. Extra materials, mixed configurations or multiple cells make the package invalid.
4. Check the preview and take the result to complete packaging. Install the module in the controller's structure-module slots.

For a batch, the number of controllers sets the machine count; multiply all construction materials accordingly. Filled modules cannot be topped up or merged directly.

## Circuit parameters: once per batch

Structures with a numeric setting accept integrated circuits during packaging. The value is the **sum of circuit number × stack count**:

**Circuit 11 × 20 + circuit 3 × 1 = 223**

Every machine in the batch shares that value. Circuits are not consumed and are not counted again for each machine. Omitting circuits uses the default; values outside the structure's supported range prevent packaging. Godforge fuel factors currently use this system.

The NEI preview uses the default parameter and has no adjustable circuit copy. Check the packaged module's tooltip for its actual value and supported range.

## Unpacking materials

Return a filled module to the packaging workspace. Put only one ME item cell, with enough free capacity and type slots, in the material slots. Taking the result returns a blank module and the cell containing the construction materials.

Insufficient cell capacity prevents the transfer. Stop jobs using the module before removing it.

Next: [create a credential](credentials.md), then [connect group inputs](groups.md).
