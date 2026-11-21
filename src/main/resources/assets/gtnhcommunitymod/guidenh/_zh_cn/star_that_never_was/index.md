---
navigation:
  title: 未曾存在之星
  parent: /index.md
  position: 90
  icon: gregtech:gt.blockmachines:19804
item_ids:
  - gregtech:gt.blockmachines:19804
---

# 未曾存在之星

**一个可开采的谎言**

陨星标志物决定要生成哪一种虚拟星体. 主机在井口凝聚星体, 井中的激光陨星采矿场负责开采, 矿石经主机送入 ME 网络. 按预览搭好中枢, 首次试机可用基础 1 并行开采.

<GameScene width="400" height="240" interactive={true} allowLayerSlider={true} showGrid={false}>
  <ImportStructureLib controller="gregtech:gt.blockmachines:19804" />
  <StarStructureControls />
</GameScene>

**Megastructure** 默认显示中央矿井, 选 8 查看整机.

## 安装与接线

1. 在井中按所用等级搭好[激光陨星采矿场](../meteor_miner/index.md), 将对应设计图装入矿机控制器. 让矿机的激光激发信标正对上方接收器. 使用二级或三级矿机时, 未曾存在之星的主机位于矿机控制器正上方一格.
2. 在主机面板 **1 号位**接入物品输入总线、流体输入仓和矿石用的 **ME 输出总线**. 能源仓可装在面板 **1 号位**或井底 **2 号位**.

## 开采第一颗星

在 NEI 的血魔法坠星页面选一种标志物. 向主机供入标志物、八重压缩圆石和生命本源. 先按标志物的原始半径试机, 扩径所用的种核可以稍后再加.

打开主机, 等隔离场展开. 联动成功后, 矿机跟随主机开关. 主机界面会从星体生成转入激光采掘. 默认整批开采, 本轮完成后可在 ME 网络中查看矿石.

运行后可调整[矿种、半径与交付](mining.md), 或接入[无人机中枢](drones.md)提高并行. 准备物资时可查[运行公式](formulas.md)和[计算示例](examples.md).
