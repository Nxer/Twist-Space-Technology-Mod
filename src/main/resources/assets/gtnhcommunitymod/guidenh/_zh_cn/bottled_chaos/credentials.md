---
navigation:
  title: 凭证与路线
  parent: index.md
  position: 20
  icon: TwistSpaceTechnology:BottledChaosCredential:0
item_ids:
  - "TwistSpaceTechnology:BottledChaosCredential:*"
---

# 制作与管理凭证

<ItemImage id="TwistSpaceTechnology:BottledChaosCredential:0" scale="3" />
<ItemImage id="TwistSpaceTechnology:BottledChaosCredential:5" scale="3" />

空框架还没有路线。按 NEI 的凭证注入配方，在主机凭证工作区放入框架与材料，取出绑定路线的凭证，再放入凭证仓。

## 框架容量与路线占用

框架上限依次为 **1、4、16、64、256、1024**。路线各有占用量；框架必须装得下所选路线。

框架有空余容量，不会自动授权更多路线，也不会补足缺失的结构模组。请同时查看凭证的路线说明和主机的机器能力。

## 合并与拆分

在凭证工作区切换对应操作，按照槽位与预览放入凭证、框架，核对结果后再取出。合并后的路线仍须满足目标框架容量；拆分以预览所示结果为准。

想让不同目标独立加工，给它们各自配置凭证仓。**完全相同的凭证会共用专用输入，同时只能启用其中一组**，不是简单复制凭证就增加独立并行。

## 有凭证却不运行？

依次检查：路线所需的结构模组是否齐全、机器等级是否足够、[容量](structure.md)是否足够、输入是否送到正确组、输出是否有空间、供电是否充足。主机与凭证仓内的状态会指出当前阻塞原因。

继续查看[加工组思索](groups.md)，区分共享原料与组内材料。
