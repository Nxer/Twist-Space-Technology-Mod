---
navigation:
  title: Bottled Chaos
  parent: /index.md
  position: 100
  icon: gregtech:gt.blockmachines:19082
item_ids:
  - gregtech:gt.blockmachines:19082
---

# Bottled Chaos

*Countless processes, all in a bottle.*

Bottled Chaos connects processing steps internally, turning raw inputs into a target product. **Structure modules supply machine capabilities; credentials select processing routes.** You need both.

<GameScene width="400" height="240" interactive={true} allowLayerSlider={true} showGrid={false}>
  <ImportStructureLib controller="gregtech:gt.blockmachines:19082" />
</GameScene>

Drag to rotate and scroll to zoom. Use the layer slider to look inside and the structure channels to change the preview. See [structure and capacity](structure.md) for construction rules.

## Technical validation

To try production-chain integration early, permanently convert the controller into a [Recipe Chain Validation Prototype](prototype.md).

## Your first route

1. Choose a target route in NEI. Check its credential and the machines it needs.
2. Use the controller's packaging workspace to create the required [structure modules](modules.md), then install them.
3. [Inject a credential](credentials.md) in the credential workspace and place it in a credential hatch.
4. Supply the [group inputs](groups.md), connect output storage and power, then enable the group.

Start with one route before adding groups and capacity. Final products leave through the controller's outputs; intermediates stay inside their processing group for further processing.

## Continue here

- [Structure, tiers and capacity](structure.md): core tiers, glass, modules and capacity allocation.
- [Packaging structure modules](modules.md): materials, ME item cells and circuit parameters.
- [Creating and managing credentials](credentials.md): route authorization and frame capacity.
- [Ponder: processing groups and inputs](groups.md): dedicated inputs, shared inputs and isolation.
