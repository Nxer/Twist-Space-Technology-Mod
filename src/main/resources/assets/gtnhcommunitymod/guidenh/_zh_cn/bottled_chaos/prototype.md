---
navigation:
  title: 配方链验证原型机
  parent: index.md
  position: 45
  icon: TwistSpaceTechnology:MetaItem01:47
item_ids:
  - TwistSpaceTechnology:MetaItem01:47
---

# 配方链验证原型机

<ItemImage id="TwistSpaceTechnology:MetaItem01:47" scale="3" />

在组装机中制作**技术验证先行协议**, 右键瓶中混沌主机, 消耗协议并**永久转换为原型机**.

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

原型机最高支持 **UV 配方**, 能源仓最高支持 **64A**, 不支持激光仓. 默认允许 **3 个凭证仓**, 安装 ZPM/UV 并行控制器后分别提升至 **4/5 个**.

调整投影器堆叠数量可选择容量柱段数.

对转换后的主机查询 NEI 结构即可查看原型机. 其他配方, [结构封装](modules.md)和[凭证使用](credentials.md)方式与普通主机相同.
