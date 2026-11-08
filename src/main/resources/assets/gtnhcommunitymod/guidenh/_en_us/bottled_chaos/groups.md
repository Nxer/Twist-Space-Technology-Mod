---
navigation:
  title: Ponder processing groups
  parent: index.md
  position: 10
  icon: gregtech:gt.blockmachines:18790
item_ids:
  - gregtech:gt.blockmachines:18790
---

# Ponder: processing groups and inputs

**This is a schematic, not the full build.** The two columns represent two processing groups. The pair of inputs behind them represents the shared central inputs.

<GameScene width="360" height="230" interactive={true} showGrid={false}>
  <Block id="gregtech:gt.blockmachines:18790" x="0" y="2" nbt='{mFacing:2s}' />
  <Block id="gregtech:gt.blockmachines:79" x="0" y="1" nbt='{mFacing:2s}' />
  <Block id="gregtech:gt.blockmachines:59" x="0" y="0" nbt='{mFacing:2s}' />
  <Block id="gregtech:gt.blockmachines:18790" x="4" y="2" nbt='{mFacing:2s}' />
  <Block id="gregtech:gt.blockmachines:59" x="4" y="1" nbt='{mFacing:2s}' />
  <Block id="gregtech:gt.blockmachines:79" x="4" y="0" nbt='{mFacing:2s}' />
  <Block id="gregtech:gt.blockmachines:79" x="1" y="0" z="3" nbt='{mFacing:2s}' />
  <Block id="gregtech:gt.blockmachines:59" x="2" y="0" z="3" nbt='{mFacing:2s}' />
  <ImportPonder src="gtnhcommunitymod:guidenh/assets/ponder/bottled_chaos_groups_en_us.json" />
</GameScene>

Play the animation or select a timeline step to inspect its highlighted positions.

## Three material locations

| Location | Who can use it | Purpose |
| --- | --- | --- |
| Two positions below a credential hatch | This group; linked groups with identical credentials share them | Route-specific inputs; either position accepts an input bus or hatch |
| Central inputs | All groups | Common supplies |
| Credential hatch's internal buffer | Its own group | In-process materials and intermediates; not shared with other groups |

The two columns deliberately reverse the input arrangement. Neither position is reserved for items or fluids. Fill unused input positions with the structure's casing.

## Identical credentials

When the frame and binding data match exactly, the hatches pool their dedicated inputs and only one group can be enabled. This can expand a route's input interfaces, but its [capacity cost](structure.md) also scales with the number of linked hatches.

## When processing stalls

A group cannot take materials from another group's dedicated inputs. Put common supplies in the central inputs.

Blocked outputs prevent other groups from starting new jobs; fix output storage first. Insufficient power pauses processing. Check materials, capacity and power before changing credentials.
