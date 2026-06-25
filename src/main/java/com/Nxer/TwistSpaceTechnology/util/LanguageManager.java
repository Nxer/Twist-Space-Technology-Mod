package com.Nxer.TwistSpaceTechnology.util;

import static gregtech.api.util.GTLanguageManager.addStringLocalization;

import com.Nxer.TwistSpaceTechnology.common.material.MaterialPool;

import bartworks.system.material.Werkstoff;
import cpw.mods.fml.common.FMLCommonHandler;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;

public class LanguageManager {

    public static void init() {
        if (!FMLCommonHandler.instance()
            .getCurrentLanguage()
            .equals("zh_CN")) return;

        // Bartwork material

        addWerkstoffLocalization(MaterialPool.HolmiumGarnet, "钬石榴石");
        addWerkstoffLocalization(MaterialPool.PureMana, "至纯魔力");
        addWerkstoffLocalization(MaterialPool.LiquidMana, "液态魔力");
        addWerkstoffLocalization(MaterialPool.PurifiedMana, "纯净魔力");
        addWerkstoffLocalization(MaterialPool.StabiliseVoidMatter, "稳定虚空物质");
        addWerkstoffLocalization(MaterialPool.LiquidStargate, "液态星门");
        addWerkstoffLocalization(MaterialPool.ConcentratedUUMatter, "浓缩UU物质");
        addWerkstoffLocalization(MaterialPool.EntropicFlux, "高能维度熵流体");
        addWerkstoffTooltipLocalization(MaterialPool.EntropicFlux, "熵的起源");
        addWerkstoffLocalization(MaterialPool.ChronoentropicFlux, "时空湮灭维度熵流体");
        addWerkstoffTooltipLocalization(MaterialPool.ChronoentropicFlux, "熵增");
    }

    public static void initGTMaterials() {
        if (!FMLCommonHandler.instance()
            .getCurrentLanguage()
            .equals("zh_CN")) return;

        // Gregtech Material

        // addGTMaterialLocalization(MaterialsTST.NeutroniumAlloy, "中子合金");
        // addGTMaterialLocalization(MaterialsTST.AxonisAlloy, "灵韵合金");
        // addGTMaterialLocalization(MaterialsTST.Axonium, "焕律璨金");
        // addGTMaterialLocalization(MaterialsTST.Dubnium, "钅杜");
        // addGTMaterialNanitesLocalization(MaterialsTST.Axonium);

        addStringLocalization("tst.blockcasings.multi.32767.name", "任意本方块的子方块");
        addStringLocalization("tst.blockmetal01.0.name", "%material块");
        addStringLocalization("tst.blockmetal01.1.name", "%material块");
        addStringLocalization("tst.blockmetal01.2.name", "%material块");
        addStringLocalization("tst.blockmetal01.3.name", "%material块");
        addStringLocalization("tst.blockmetal01.32767.name", "任意本方块的子方块");
    }

    public static void addWerkstoffLocalization(Werkstoff aWerkstoff, String localizedName) {
        String unlocalizedName = aWerkstoff.getDefaultName()
            .toLowerCase();
        String mName = unlocalizedName.replace(" ", "");

        addStringLocalization("Material." + mName, localizedName);
        addStringLocalization("bw.werkstoff." + aWerkstoff.getmID() + ".name", localizedName);

        if (aWerkstoff.hasItemType(OrePrefixes.cellMolten)) {
            addStringLocalization("fluid.molten." + unlocalizedName, "熔融" + localizedName);
        }
        if (aWerkstoff.hasItemType(OrePrefixes.cell)) {
            addStringLocalization("fluid." + unlocalizedName, localizedName);
        }
    }

    public static void addWerkstoffTooltipLocalization(Werkstoff aWerkstoff, String localizedTooltip) {
        addStringLocalization("bw.werkstoff." + aWerkstoff.getmID() + ".tooltip", localizedTooltip);
    }

    public static void addGTMaterialLocalization(Materials aMaterial, String localizedName) {
        String mName = aMaterial.mDefaultLocalName.toLowerCase()
            .replace(" ", "");
        int mID = aMaterial.mMetaItemSubID;

        addStringLocalization("gt.blockframes." + mID + ".name", "%material框架");
        addStringLocalization("gt.blockores.1" + mID + ".name", "%material矿石");
        addStringLocalization("gt.blockores.2" + mID + ".name", "%material矿石");
        addStringLocalization("gt.blockores.3" + mID + ".name", "%material矿石");
        addStringLocalization("gt.blockores.4" + mID + ".name", "%material矿石");
        addStringLocalization("gt.blockores.5" + mID + ".name", "%material矿石");
        addStringLocalization("gt.blockores.6" + mID + ".name", "%material矿石");
        addStringLocalization("gt.blockores.16" + mID + ".name", "贫瘠%material矿石");
        addStringLocalization("gt.blockores.17" + mID + ".name", "贫瘠%material矿石");
        addStringLocalization("gt.blockores.18" + mID + ".name", "贫瘠%material矿石");
        addStringLocalization("gt.blockores.19" + mID + ".name", "贫瘠%material矿石");
        addStringLocalization("gt.blockores.20" + mID + ".name", "贫瘠%material矿石");
        addStringLocalization("gt.blockores.21" + mID + ".name", "贫瘠%material矿石");
        addStringLocalization("gt.blockores.22" + mID + ".name", "贫瘠%material矿石");

        addStringLocalization("gt.metaitem.01." + mID + ".name", "小撮%material粉");
        addStringLocalization("gt.metaitem.01.1" + mID + ".name", "小堆%material粉");
        addStringLocalization("gt.metaitem.01.2" + mID + ".name", "%material粉");
        addStringLocalization("gt.metaitem.01.9" + mID + ".name", "%material粒");

        addStringLocalization("gt.metaitem.01.11" + mID + ".name", "%material锭");
        addStringLocalization("gt.metaitem.01.12" + mID + ".name", "热%material锭");
        addStringLocalization("gt.metaitem.01.13" + mID + ".name", "双重%material锭");
        addStringLocalization("gt.metaitem.01.14" + mID + ".name", "三重%material锭");
        addStringLocalization("gt.metaitem.01.15" + mID + ".name", "四重%material锭");
        addStringLocalization("gt.metaitem.01.16" + mID + ".name", "五重%material锭");
        addStringLocalization("gt.metaitem.01.17" + mID + ".name", "%material板");
        addStringLocalization("gt.metaitem.01.18" + mID + ".name", "双重%material板");
        addStringLocalization("gt.metaitem.01.19" + mID + ".name", "三重%material板");
        addStringLocalization("gt.metaitem.01.20" + mID + ".name", "四重%material板");
        addStringLocalization("gt.metaitem.01.21" + mID + ".name", "五重%material板");
        addStringLocalization("gt.metaitem.01.22" + mID + ".name", "致密%material板");
        addStringLocalization("gt.metaitem.01.23" + mID + ".name", "%material杆");
        addStringLocalization("gt.metaitem.01.24" + mID + ".name", "%material弹簧");
        addStringLocalization("gt.metaitem.01.25" + mID + ".name", "%material滚珠");
        addStringLocalization("gt.metaitem.01.26" + mID + ".name", "%material螺栓");
        addStringLocalization("gt.metaitem.01.27" + mID + ".name", "%material螺丝");
        addStringLocalization("gt.metaitem.01.28" + mID + ".name", "%material环");
        addStringLocalization("gt.metaitem.01.29" + mID + ".name", "%material箔");
        addStringLocalization("gt.metaitem.01.31" + mID + ".name", "%material等离子单元");

        addStringLocalization("gt.metaitem.02." + mID + ".name", "%material剑刃");
        addStringLocalization("gt.metaitem.02.1" + mID + ".name", "%material镐头");
        addStringLocalization("gt.metaitem.02.2" + mID + ".name", "%material铲头");
        addStringLocalization("gt.metaitem.02.3" + mID + ".name", "%material斧头");
        addStringLocalization("gt.metaitem.02.4" + mID + ".name", "%material锄头");
        addStringLocalization("gt.metaitem.02.5" + mID + ".name", "%material锤头");
        addStringLocalization("gt.metaitem.02.6" + mID + ".name", "%material锉刀刃");
        addStringLocalization("gt.metaitem.02.7" + mID + ".name", "%material锯刃");
        addStringLocalization("gt.metaitem.02.8" + mID + ".name", "%material钻头");
        addStringLocalization("gt.metaitem.02.9" + mID + ".name", "%material链锯刃");
        addStringLocalization("gt.metaitem.02.10" + mID + ".name", "%material扳手顶");
        addStringLocalization("gt.metaitem.02.11" + mID + ".name", "%material万用铲头");
        addStringLocalization("gt.metaitem.02.12" + mID + ".name", "%material镰刀刃");
        addStringLocalization("gt.metaitem.02.13" + mID + ".name", "%material犁头");
        addStringLocalization("gt.metaitem.02.15" + mID + ".name", "%material圆锯锯刃");
        addStringLocalization("gt.metaitem.02.16" + mID + ".name", "%material涡轮扇叶");
        addStringLocalization("gt.metaitem.02.18" + mID + ".name", "%material外壳");
        addStringLocalization("gt.metaitem.02.19" + mID + ".name", "细%material导线");
        addStringLocalization("gt.metaitem.02.20" + mID + ".name", "小型%material齿轮");
        addStringLocalization("gt.metaitem.02.21" + mID + ".name", "%material转子");
        addStringLocalization("gt.metaitem.02.22" + mID + ".name", "长%material杆");
        addStringLocalization("gt.metaitem.02.23" + mID + ".name", "小型%material弹簧");
        addStringLocalization("gt.metaitem.02.24" + mID + ".name", "%material弹簧");
        addStringLocalization("gt.metaitem.02.31" + mID + ".name", "%material齿轮");

        addStringLocalization("gt.metaitem.03.6" + mID + ".name", "超致密%material板");

        addStringLocalization("gt.metaitem.99." + mID + ".name", "熔融%material单元");

        addStringLocalization("Material." + mName, localizedName);
        addStringLocalization("fluid.molten." + mName, "熔融" + localizedName);
        addStringLocalization("fluid.plasma." + mName, localizedName + "等离子体");
        addStringLocalization("gt.blockmachines.gt_frame_" + mName + ".name", "%material框架(方块实体)");
        addStringLocalization("gt.blockmachines.wire." + mName + ".01.name", "1x%material导线");
        addStringLocalization("gt.blockmachines.wire." + mName + ".02.name", "2x%material导线");
        addStringLocalization("gt.blockmachines.wire." + mName + ".04.name", "4x%material导线");
        addStringLocalization("gt.blockmachines.wire." + mName + ".08.name", "8x%material导线");
        addStringLocalization("gt.blockmachines.wire." + mName + ".12.name", "12x%material导线");
        addStringLocalization("gt.blockmachines.wire." + mName + ".16.name", "16x%material导线");
    }

    public static void addGTMaterialNanitesLocalization(Materials aMaterial) {
        addStringLocalization("gt.metaitem.03.4" + aMaterial.mMetaItemSubID + ".name", "%material纳米蜂群");
    }
}
