---
navigation:
  title: 结构与容量
  parent: index.md
  position: 40
  icon: TwistSpaceTechnology:BottledChaosCapacity:0
item_ids:
  - "TwistSpaceTechnology:BottledChaosCapacity:*"
  - "TwistSpaceTechnology:StabilisationFieldGenerator:*"
  - "TwistSpaceTechnology:EnergySustainmentMatrix:*"
---

# 结构、等级与容量

## 先确定核心等级

<ItemImage id="TwistSpaceTechnology:StabilisationFieldGenerator:0" scale="3" />
<ItemImage id="TwistSpaceTechnology:EnergySustainmentMatrix:0" scale="3" />

稳定力场发生器与能量维持矩阵必须同级，共同决定机器等级：UHV、UEV、UIV、UMV、UXV。

主轴与容量区玻璃必须同级，且不得低于机器等级。**能源仓等级不能高于玻璃等级**。提升玻璃只放宽能源仓限制，不会提升机器核心等级。

完整结构可在[主机页面](index.md)旋转、分层查看；建造时也可使用蓝图与投影器。

## 四个容量区，共享一份容量

<GameScene height="140" interactive={true} showGrid={false}>
  <Block id="TwistSpaceTechnology:BottledChaosCapacity:0" x="0" />
  <Block id="TwistSpaceTechnology:BottledChaosCapacity:1" x="2" />
  <Block id="TwistSpaceTechnology:BottledChaosCapacity:2" x="4" />
  <Block id="TwistSpaceTechnology:BottledChaosCapacity:3" x="6" />
  <Block id="TwistSpaceTechnology:BottledChaosCapacity:4" x="8" />
</GameScene>

容量区可分别升级，也可整区留空。上图依次为 I～V 级容量方块示意。

| 单个容量区 | 留空 | I | II | III | IV | V |
| --- | --- | --- | --- | --- | --- | --- |
| 提供容量 | 1 | 4 | 16 | 64 | 256 | 1024 |

四区相加得到全机容量：全部留空为 **4**，全部 V 级为 **4096**。这是运行路线的额度，不是物品或流体存储空间。

启用组按凭证所含路线的占用量扣除额度，**不按凭证框架的上限扣除**。完全相同的凭证共用专用输入时，占用还要乘以对应凭证仓数量。

例如，路线占用为 4 的凭证放在两个相互绑定的凭证仓中，即使只启用其中一组，也需要 8 容量。

## 模块与扩展

各类控制器模块最多安装一个。并行控制器同时增加加工组上限与结构模组槽位：无模块时为 **1 组 / 6 槽**，每提升一级增加 **2 组 / 6 槽**，最高 **15 组 / 48 槽**。

增加加工组后，仍需为每组提供凭证、机器能力与容量；单纯增加凭证仓不会跳过这些要求。

## 供电

供电不足时暂停加工。机器达到 UXV 级且未安装能源仓时，启用无线模式；各加工组预付电量，并按最低超频时间运行。

更换核心、供电结构或模块前先停机，处理完现有任务后再调整。
