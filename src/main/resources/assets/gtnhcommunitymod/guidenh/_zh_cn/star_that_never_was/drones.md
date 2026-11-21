---
navigation:
  title: 无人机中枢
  parent: index.md
  position: 10
  icon: TwistSpaceTechnology:BloodInfusedMiningDrone:7
item_ids:
  - "TwistSpaceTechnology:BloodInfusedMiningDrone:*"
---

# 无人机中枢

## 无人机收发

面向悬浮中枢正面, 三个仓位在下排, 仓口朝外.

<GameScene width="360" height="180" interactive={true} showGrid={false}>
  <ImportStructure src="gtnhcommunitymod:guidenh/assets/structures/star_drone_hub.snbt" />
  <ImportPonder src="gtnhcommunitymod:guidenh/assets/ponder/star_drone_hub_zh_cn.json" />
</GameScene>

| 仓位 | 用途 | 可用仓室 |
| --- | --- | --- |
| 左侧 3 号 | 派出无人机 | 带实体库存的物品输入总线 |
| 中央 4 号 | 接回无人机 | 普通或 ME 输出总线 |
| 右侧 5 号 | 供应氦等离子 | 普通、ME 或存储元件流体输入仓 |

矿石从[井口主机](index.md)的 ME 输出总线交付.

## 组队与供氦

每次启动时, 输入总线里的全部采矿无人机一起出发. **有效等级最高的一组决定本轮并行**. 随队的低阶普通无人机也消耗燃料、占用返还空间. 回送时按有效等级分流, 即可保持机群等级一致.

每架普通无人机出发时按计划采掘时长预付氦等离子, 每 tick 50 L, 即每秒 1000 L. 已浸血的无人机出勤免燃料, 有效等级比原型号高一档, 最高为 MAX.

每采掘满一秒, 参与并行的普通无人机逐架独立判定, 每架有 1/3600 的机会浸血. 新的有效等级从下次出勤起生效.

[机群和燃料公式](formulas.md) · [计算示例](examples.md)
