---
navigation:
  title: 瓶中混沌
  parent: /index.md
  position: 100
  icon: gregtech:gt.blockmachines:19082
item_ids:
  - gregtech:gt.blockmachines:19082
---

# 瓶中混沌

万千工序，尽在瓶中。

瓶中混沌在内部衔接多道工序，把原料加工成目标产物。**结构模组提供机器能力，凭证选择加工路线**，两者缺一不可。

<GameScene width="400" height="240" interactive={true} allowLayerSlider={true} showGrid={false}>
  <ImportStructureLib controller="gregtech:gt.blockmachines:19082" />
</GameScene>

拖动模型旋转，滚轮缩放；用分层滑块查看内部，用结构通道切换预览配置。完整建造要求见[结构与容量](structure.md)。

## 技术验证

想提前验证产线整合方案, 可将主机永久转换为[配方链验证原型机](prototype.md)。

## 第一条路线

1. 在 NEI 中选一条目标路线，查看它的凭证及所需机器。
2. 用主机的封装工作区制作对应的[结构模组](modules.md)，装入主机。
3. 在凭证工作区完成[凭证注入](credentials.md)，把凭证放入凭证仓。
4. 向[加工组输入](groups.md)供料，接好输出与供电，再启用加工组。

先运行一条路线，再逐步增加加工组和容量。最终产物从主机输出，中间产物留在所属加工组内继续加工。

## 从这里继续

- [结构、等级与容量](structure.md)：核心等级、玻璃、模块与容量分配。
- [封装结构模组](modules.md)：材料、ME 物品磁盘与电路参数。
- [制作与管理凭证](credentials.md)：路线授权与框架容量。
- [思索：加工组与输入](groups.md)：专用输入、共享输入和组间隔离。
