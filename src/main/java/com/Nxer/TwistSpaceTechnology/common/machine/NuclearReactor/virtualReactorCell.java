package com.Nxer.TwistSpaceTechnology.common.machine.NuclearReactor;

import ic2.api.reactor.IReactorComponent;

// 组件类型
// 1：燃料棒，增添无尽燃料棒（特点是烧不完，但是效率很低）/永恒燃料棒（同样烧不完，效率也很低，但是辐射值极高）
// 2：热容，增添钨钢/耐热钢/中子/宇宙素中子/流体本源/宇宙素
// 3：反射板，增添中子反射板（反射100%收集到的辐射值到周围6物品）/拉普拉斯反射板（反射150%收集到的所有辐射值到周围26物品）/悖论反射引擎（反射1000%收集到的辐射值到所有位置，并增加所有位置损坏速率到1000%（可叠加），这甚至能损坏不可损坏的燃料棒或者热容或者燃料棒）
// 4：生物安全突变仓，收集辐射值来进行突变，主要是给养蜂使用的。
// 5：减速剂仓，减少经过的辐射数量，可以防止辐射过高导致部分位置爆炸（无法防止悖论引擎干坏事）（需要注入流体）
// 6：辐射隔板，包括防辐射板，高级防辐射板，防辐射硅岩板...星门板，是防御，就算是悖论也能防御。但是除了星门板都会被慢慢损坏
// 7：热交换组件，增添一些新的热交换组件
// 8：流体热交换仓,可以在流体堆中使用，一旦存在这种仓即视为流体堆。自身不一定有热交换能力，需要和热交换组件配合使用
// 9：空间压缩仓，理论成立的一种压缩空间技术。可以在一个仓内部再放置一个核电进去。但是这个核电无法发电，只能提供热量与辐射值。试验阶段，不要使用
// 10：EU仓，直接获取周围一定范围内燃料棒对应热量应当产生的电力数量并且发送给核电本身。这不会减少仓的热量。这也不能叠加获取eu。等级仅会影响获取范围，不产生电压等级。
public class virtualReactorCell {

    // 决定反应堆中的位置
    public long x, y, z;
    // 当前热量
    public long heat;
    // 最大热量
    public long maxHeat;
    // 辐射值
    public long radiation;
    // 耐久度
    public long damaged;
    // 最大耐久度
    public long maxDamage;
    // 对应的物品meta
    public IReactorComponent component;
    // 是否激活
    public boolean activated;
    // 是否失控
    public boolean underControll;
    // 倍率！大家最喜欢的倍率！
    public long Multiplier;

    virtualReactorCell() {

    }

    // 计算最终发电/发热量加成
    public long calculateMultiplier(TST_NuclearReactor reactor) {
        long multiplier = 0;

        return multiplier;
    }

    // 返回是否爆炸
    public boolean update(TST_NuclearReactor reactor, boolean updateAllData) {
        return false;
    }

}
