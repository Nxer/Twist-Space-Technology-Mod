---
navigation:
  title: 思索：加工组与输入
  parent: index.md
  position: 10
  icon: gregtech:gt.blockmachines:18790
item_ids:
  - gregtech:gt.blockmachines:18790
---

# 思索：加工组与输入

这是局部关系示意，不是完整建造结构。两列分别表示两个加工组，后方的一对输入表示中心共享输入。

<GameScene width="360" height="230" interactive={true} showGrid={false}>
  <Block id="gregtech:gt.blockmachines:18790" x="0" y="2" nbt='{mFacing:2s}' />
  <Block id="gregtech:gt.blockmachines:79" x="0" y="1" nbt='{mFacing:2s}' />
  <Block id="gregtech:gt.blockmachines:59" x="0" y="0" nbt='{mFacing:2s}' />
  <Block id="gregtech:gt.blockmachines:18790" x="4" y="2" nbt='{mFacing:2s}' />
  <Block id="gregtech:gt.blockmachines:59" x="4" y="1" nbt='{mFacing:2s}' />
  <Block id="gregtech:gt.blockmachines:79" x="4" y="0" nbt='{mFacing:2s}' />
  <Block id="gregtech:gt.blockmachines:79" x="1" y="0" z="3" nbt='{mFacing:2s}' />
  <Block id="gregtech:gt.blockmachines:59" x="2" y="0" z="3" nbt='{mFacing:2s}' />
  <ImportPonder src="gtnhcommunitymod:guidenh/assets/ponder/bottled_chaos_groups_zh_cn.json" />
</GameScene>

播放动画，或点选时间轴上的步骤查看高亮位置。

## 三种材料位置

| 位置 | 谁能使用 | 用途 |
| --- | --- | --- |
| 凭证仓下方两格 | 本组；完全相同凭证的绑定组共享 | 给指定路线供料；每格可放输入总线或输入仓 |
| 中心输入 | 全部加工组 | 提供通用原料 |
| 凭证仓内部缓存 | 所属加工组 | 暂存加工中的材料与中间产物，不与其他组共享 |

图中两列采用相反的输入排列，表示这两个位置没有固定的物品／流体分工。不安装输入的预留位置填回结构外壳。

## 相同凭证的特殊规则

凭证的框架与绑定数据完全相同时，各仓的专用输入合在一起使用，同时仅允许一组启用。用于扩大一条路线的输入接口时，也要按凭证仓数量预留[容量](structure.md)。

## 停滞时先检查

原料放在别组的专用输入里，本组无法取用。需要共享的原料应放在中心输入中。

输出堵塞会阻止其他组接新任务，先处理输出存储。供电不足会暂停；更换凭证前先核对原料、容量与供电。
