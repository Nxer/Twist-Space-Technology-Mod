---
navigation:
  title: Recipe Chain Validation Prototype
  parent: index.md
  position: 45
  icon: TwistSpaceTechnology:MetaItem01:47
item_ids:
  - TwistSpaceTechnology:MetaItem01:47
---

# Recipe Chain Validation Prototype

<ItemImage id="TwistSpaceTechnology:MetaItem01:47" scale="3" />

Craft an **Early Technical Validation Protocol** in an Assembler, then right-click a Bottled Chaos controller. This consumes the protocol and **permanently converts it into a prototype**.

<GameScene width="400" height="240" interactive={true} allowLayerSlider={true} showGrid={false}>
  <ImportStructureLib controller="gregtech:gt.blockmachines:19082">
    <Tier value="1" locked={true} />
    <Channel name="bottledchaosprototype" value="1" locked={true} />
    <Channel name="bottledchaoscapacity" value="1" locked={true} />
    <Channel name="glass" value="1" locked={true} />
    <Channel name="energymatrix" value="1" locked={true} />
    <Channel name="fieldgeneratortier" value="1" locked={true} />
  </ImportStructureLib>
</GameScene>

The prototype supports recipes up to **UV** and energy hatches up to **64A**, excluding laser hatches. It allows **3 credential hatches** by default; ZPM and UV Parallel Controllers raise this to **4 and 5**.

Adjust the projector stack size to select the number of capacity segments.

Query the converted controller in NEI to view its prototype structure. Other recipes, [structure packaging](modules.md), and [credential usage](credentials.md) work as on the standard controller.
