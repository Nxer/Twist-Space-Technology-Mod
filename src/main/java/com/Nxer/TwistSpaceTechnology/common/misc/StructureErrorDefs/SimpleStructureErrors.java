package com.Nxer.TwistSpaceTechnology.common.misc.StructureErrorDefs;

import gregtech.api.structure.error.StructureErrors;
import gregtech.api.structure.error.TranslatableStructureError;

public class SimpleStructureErrors {

    // #tr tst.common.shared.structure_error.internal_structure_issue
    // # Something wrong in structure definition codes.
    // #zh_CN 代码存在结构定义错误
    public static TranslatableStructureError internal_structure_issue = StructureErrors
        .of("tst.common.shared.structure_error.internal_structure_issue");

    // #tr tst.common.shared.structure_error.simple_structure_issue
    // # Structure is wrong.
    // #zh_CN 机器结构存在错误
    public static TranslatableStructureError simple_structure_issue = StructureErrors
        .of("tst.common.shared.structure_error.simple_structure_issue");

    // #tr tst.common.shared.structure_error.tiered_structure_issue
    // # Something wrong with tiered blocks.
    // #zh_CN 等级方块存在放置错误
    public static TranslatableStructureError tiered_structure_issue = StructureErrors
        .of("tst.common.shared.structure_error.tiered_structure_issue");

    // #tr tst.common.shared.structure_error.special_block_structure_issue
    // # Something wrong with the special blocks.
    // #zh_CN 特殊方块结构错误
    public static TranslatableStructureError special_block_structure_issue = StructureErrors
        .of("tst.common.shared.structure_error.special_block_structure_issue");

    // #tr tst.common.shared.structure_error.special_hatch_amount_wrong
    // # Amount of special hatches is wrong.
    // #zh_CN 特殊仓室数量不正确
    public static TranslatableStructureError special_hatch_amount_wrong = StructureErrors
        .of("tst.common.shared.structure_error.special_hatch_amount_wrong");

    // #tr tst.common.shared.structure_error.too_more_hatches
    // # Too many hatches!
    // #zh_CN 仓室数量过多
    public static TranslatableStructureError too_more_hatches = StructureErrors
        .of("tst.common.shared.structure_error.too_more_hatches");

    // #tr tst.common.shared.structure_error.hatch_tier_incompatible
    // # Some hatches' tier is not available.
    // #zh_CN 仓室等级不符合要求
    public static TranslatableStructureError hatch_tier_incompatible = StructureErrors
        .of("tst.common.shared.structure_error.hatch_tier_incompatible");

    // #tr tst.common.shared.structure_error.laser_hatch_incompatible
    // # Do not use laser hatch.
    // #zh_CN 不可使用激光仓
    public static TranslatableStructureError laser_hatch_incompatible = StructureErrors
        .of("tst.common.shared.structure_error.laser_hatch_incompatible");

    // #tr tst.common.shared.structure_error.multi_amp_hatch_incompatible
    // # Do not use multi Amp hatch.
    // #zh_CN 不可使用高电流仓
    public static TranslatableStructureError multi_Amp_hatch_incompatible = StructureErrors
        .of("tst.common.shared.structure_error.multi_amp_hatch_incompatible");

    // #tr tst.common.shared.structure_error.one_hatch_each_module
    // # Only one hatch of each module is allowed to be installed.
    // #zh_CN 每种模块仓室只允许安装一个
    public static TranslatableStructureError one_hatch_each_module = StructureErrors
        .of("tst.common.shared.structure_error.one_hatch_each_module");

    // #tr tst.common.shared.structure_error.missing_oscillator_ring
    // # Missing SpaceTime Oscillator ring(s).
    // #zh_CN 缺少时空振荡器环
    public static TranslatableStructureError missing_oscillator_ring = StructureErrors
        .of("tst.common.shared.structure_error.missing_oscillator_ring");

    // #tr tst.common.shared.structure_error.missing_constraintor_ring
    // # Missing SpaceTime Constraintor ring(s).
    // #zh_CN 缺少时空约束器环
    public static TranslatableStructureError missing_constraintor_ring = StructureErrors
        .of("tst.common.shared.structure_error.missing_constraintor_ring");

    // #tr tst.common.shared.structure_error.missing_merger_ring
    // # Missing SpaceTime Merger ring(s).
    // #zh_CN 缺少时空归并器环
    public static TranslatableStructureError missing_merger_ring = StructureErrors
        .of("tst.common.shared.structure_error.missing_merger_ring");

}
