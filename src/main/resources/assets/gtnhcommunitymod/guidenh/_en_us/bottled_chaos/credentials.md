---
navigation:
  title: Credentials and routes
  parent: index.md
  position: 20
  icon: TwistSpaceTechnology:BottledChaosCredential:0
item_ids:
  - "TwistSpaceTechnology:BottledChaosCredential:*"
---

# Creating and managing credentials

<ItemImage id="TwistSpaceTechnology:BottledChaosCredential:0" scale="3" />
<ItemImage id="TwistSpaceTechnology:BottledChaosCredential:5" scale="3" />

A blank frame has no routes. Follow the credential injection recipe in NEI: put the frame and materials in the controller's credential workspace, take the bound credential, then install it in a credential hatch.

## Frame capacity and route weight

Frames hold **1, 4, 16, 64, 256 or 1024** capacity. Each route has an occupied weight; the frame must hold all selected routes.

Unused frame capacity does not authorize extra routes or supply missing structure modules. Check both the credential's routes and the controller's machine capabilities.

## Merging and splitting

Select the relevant operation in the credential workspace. Place credentials and frames as indicated by the slots and preview, then check the results before taking them. Merged routes must fit the destination frame; splitting follows the displayed results.

Use separate credential hatches for independently processed targets. **Exactly identical credentials share dedicated inputs and only one of their groups can be enabled at a time.** Copying a credential does not simply add independent parallel processing.

## A credential is installed, but nothing runs?

Check the required structure modules, machine tier, available [capacity](structure.md), correct input group, output space and power. The controller and credential hatch show the current blocking reason.

Continue to [processing-group Ponder](groups.md) to distinguish shared supplies from group-local materials.
