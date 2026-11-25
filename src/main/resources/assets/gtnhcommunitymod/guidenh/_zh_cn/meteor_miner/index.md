---
navigation:
  title: 激光陨星采矿场
  parent: /index.md
  position: 95
  icon: gregtech:gt.blockmachines:19072
item_ids:
  - gregtech:gt.blockmachines:19072
---

# 激光陨星采矿场

血魔法的坠星仪式把陨星召到井架上方. 激光陨星采矿场从下方开采这颗陨星, 将产物送入矿机的输出总线.

<GameScene width="400" height="240" interactive={true} allowLayerSlider={true} showGrid={false}>
  <ImportStructureLib controller="gregtech:gt.blockmachines:19072" />
</GameScene>

在预览中切换等级, 可以对照三种井架的形状和仓位.

## 定位陨星中心

按选定的[矿机设计图](schematics.md)搭好井架, 对控制器使用全息投影仪, 按投影放置主仪式石和守卫者玻璃. **守卫者玻璃所在的一格就是陨星中心**. 它位于激光激发信标正上方, 比井架最高的方块高 48 格. 从信标往上数, T1 为 51 格, T2 和 T3 为 48 格.

## 开采第一颗陨星

1. 将设计图放入控制器槽, 装好维护仓、能源仓和物品输出总线. 保持矿机和陨星所在区域的区块加载.
2. 激活坠星仪式并打开矿机. 矿机会自动探测陨星半径, 最大开采半径为 40 格. 激光从下往上开采, 控制器显示进度和本颗陨星的产出.

采完后让矿机保持开机. 矿机会检查守卫者玻璃正上方一格. 该处出现方块时, 矿机就会重新探测半径, 开始下一轮开采.

## 与未曾存在之星联动

矿机开采[未曾存在之星](../star_that_never_was/index.md)生成的虚拟星体, 主机将矿石送入 ME 网络.
