package com.Nxer.TwistSpaceTechnology.common.machine.NuclearReactor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

import net.minecraft.item.ItemStack;

import ic2.api.reactor.IReactorComponent;
import ic2.core.Ic2Items;
import ic2.core.item.reactor.ItemReactorCondensator;
import ic2.core.item.reactor.ItemReactorHeatStorage;
import ic2.core.item.reactor.ItemReactorHeatSwitch;
import ic2.core.item.reactor.ItemReactorLithiumCell;
import ic2.core.item.reactor.ItemReactorMOX;
import ic2.core.item.reactor.ItemReactorPlating;
import ic2.core.item.reactor.ItemReactorReflector;
import ic2.core.item.reactor.ItemReactorUranium;
import ic2.core.item.reactor.ItemReactorVent;
import ic2.core.item.reactor.ItemReactorVentSpread;

public class TST_NuclearReactorCalculate {

    private static class ItemStackCoord {

        public ItemStack stack;
        public int x;
        public int y;

        public int z;

        public ItemStackCoord(ItemStack stack1, int x1, int y1, int z1) {
            this.stack = stack1;
            this.x = x1;
            this.y = y1;
            this.z = z1;
        }
    }

    private static void checkHeatAcceptor(TST_NuclearReactor reactor, int x, int y, int z,
        Collection<ItemStackCoord> heatAcceptors) {
        ItemStack thing = reactor.getItemAt(x, y, z);
        if (thing != null && thing.getItem() instanceof IReactorComponent comp) {
            if (canStoreHeat(comp, reactor, thing, x, y, z)) {
                heatAcceptors.add(new ItemStackCoord(thing, x, y, z));
            }
        }
    }

    private static void cool(ItemReactorVentSpread cp, TST_NuclearReactor reactor, int x, int y, int z) {
        ItemStack stack = reactor.getItemAt(x, y, z);
        if (stack != null && stack.getItem() instanceof IReactorComponent comp) {
            if (canStoreHeat(comp, reactor, stack, x, y, z)) {
                int self = alterHeat(comp, reactor, stack, x, y, z, -cp.sideVent);
                if (self <= 0) {
                    reactor.addEmitHeat(self + cp.sideVent);
                }
            }
        }
    }

    private static int checkPulseable(ItemReactorUranium cp, TST_NuclearReactor reactor, int x, int y, int z,
        ItemStack me, int mex, int mey, int mez, boolean heatrun) {
        ItemStack other = reactor.getItemAt(x, y, z);
        return other != null && other.getItem() instanceof IReactorComponent
            && acceptUraniumPulse(
                ((IReactorComponent) other.getItem()),
                reactor,
                other,
                me,
                x,
                y,
                z,
                mex,
                mey,
                mez,
                heatrun) ? 1 : 0;
    }

    private static ItemStack getDepletedStack(ItemReactorUranium stack) {
        if (stack instanceof ItemReactorMOX) {
            ItemStack ret = switch (stack.numberOfCells) {
                case 1 -> Ic2Items.reactorDepletedMOXSimple;
                case 2 -> Ic2Items.reactorDepletedMOXDual;
                default -> throw new RuntimeException("invalid cell count: " + stack.numberOfCells);
                case 4 -> Ic2Items.reactorDepletedMOXQuad;
            };
            return new ItemStack(ret.getItem(), 1);
        }
        ItemStack ret = switch (stack.numberOfCells) {
            case 1 -> Ic2Items.reactorDepletedUraniumSimple;
            case 2 -> Ic2Items.reactorDepletedUraniumDual;
            default -> throw new RuntimeException("invalid cell count: " + stack.numberOfCells);
            case 4 -> Ic2Items.reactorDepletedUraniumQuad;
        };

        return new ItemStack(ret.getItem(), 1);
    }

    public static void processChamber(IReactorComponent component, TST_NuclearReactor reactor, ItemStack yourStack,
        int x, int y, int z, boolean heatrun) {
        if (component instanceof ItemReactorHeatSwitch cp) {
            if (heatrun) {
                int myHeat = 0;
                ArrayList<ItemStackCoord> heatAcceptors = new ArrayList<>();
                if (cp.switchSide > 0) {
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            for (int k = -1; k <= 1; k++) {
                                checkHeatAcceptor(reactor, x + i, y + i, z + i, heatAcceptors);
                            }
                        }
                    }
                }
                int add;
                if (cp.switchSide > 0) {
                    for (Iterator<ItemStackCoord> var8 = heatAcceptors.iterator(); var8.hasNext(); myHeat += add) {
                        ItemStackCoord stackcoord = var8.next();
                        IReactorComponent heatable = (IReactorComponent) stackcoord.stack.getItem();
                        double mymed = (double) getCurrentHeat(cp, reactor, yourStack, x, y, z) * 100.0
                            / (double) getMaxHeat(cp, reactor, yourStack, x, y, z);
                        double heatablemed = (double) getCurrentHeat(
                            heatable,
                            reactor,
                            stackcoord.stack,
                            stackcoord.x,
                            stackcoord.y,
                            stackcoord.z)
                            * 100.0
                            / (double) getMaxHeat(
                                heatable,
                                reactor,
                                stackcoord.stack,
                                stackcoord.x,
                                stackcoord.y,
                                stackcoord.z);
                        add = (int) ((double) getMaxHeat(
                            heatable,
                            reactor,
                            stackcoord.stack,
                            stackcoord.x,
                            stackcoord.y,
                            stackcoord.z) / 100.0 * (heatablemed + mymed / 2.0));
                        if (add > cp.switchSide) {
                            add = cp.switchSide;
                        }

                        if (heatablemed + mymed / 2.0 < 1.0) {
                            add = cp.switchSide / 2;
                        }

                        if (heatablemed + mymed / 2.0 < 0.75) {
                            add = cp.switchSide / 4;
                        }

                        if (heatablemed + mymed / 2.0 < 0.5) {
                            add = cp.switchSide / 8;
                        }

                        if (heatablemed + mymed / 2.0 < 0.25) {
                            add = 1;
                        }

                        if ((double) Math.round(heatablemed * 10.0) / 10.0 > (double) Math.round(mymed * 10.0) / 10.0) {
                            add -= 2 * add;
                        } else if ((double) Math.round(heatablemed * 10.0) / 10.0
                            == (double) Math.round(mymed * 10.0) / 10.0) {
                                add = 0;
                            }

                        myHeat -= add;
                        add = alterHeat(
                            heatable,
                            reactor,
                            stackcoord.stack,
                            stackcoord.x,
                            stackcoord.y,
                            stackcoord.z,
                            add);
                    }
                }

                if (cp.switchReactor > 0) {
                    double mymed = (double) getCurrentHeat(cp, reactor, yourStack, x, y, z) * 100.0
                        / (double) getMaxHeat(cp, reactor, yourStack, x, y, z);
                    double Reactormed = reactor.getHeat() * 100.0 / reactor.getMaxHeat();
                    add = (int) Math.round(reactor.getMaxHeat() / 100.0 * (Reactormed + mymed / 2.0));
                    if (add > cp.switchReactor) {
                        add = cp.switchReactor;
                    }

                    if (Reactormed + mymed / 2.0 < 1.0) {
                        add = cp.switchSide / 2;
                    }

                    if (Reactormed + mymed / 2.0 < 0.75) {
                        add = cp.switchSide / 4;
                    }

                    if (Reactormed + mymed / 2.0 < 0.5) {
                        add = cp.switchSide / 8;
                    }

                    if (Reactormed + mymed / 2.0 < 0.25) {
                        add = 1;
                    }

                    if ((double) Math.round(Reactormed * 10.0) / 10.0 > (double) Math.round(mymed * 10.0) / 10.0) {
                        add -= 2 * add;
                    } else
                        if ((double) Math.round(Reactormed * 10.0) / 10.0 == (double) Math.round(mymed * 10.0) / 10.0) {
                            add = 0;
                        }

                    myHeat -= add;
                    reactor.setHeat(reactor.getHeat() + add);
                }

                alterHeat(cp, reactor, yourStack, x, y, z, myHeat);
            }
        } else if (component instanceof ItemReactorVent cp) {
            if (heatrun) {
                int rheat;
                if (cp.reactorVent > 0) {
                    rheat = (int) reactor.getHeat();
                    int reactorDrain = Math.min(rheat, cp.reactorVent);

                    rheat -= reactorDrain;
                    if (alterHeat(cp, reactor, yourStack, x, y, z, reactorDrain) > 0) {
                        return;
                    }

                    reactor.setHeat(rheat);
                }

                rheat = alterHeat(cp, reactor, yourStack, x, y, z, -cp.selfVent);
                if (rheat <= 0) {
                    reactor.addEmitHeat(rheat + cp.selfVent);
                }
            }
        } else if (component instanceof ItemReactorVentSpread cp) {
            if (heatrun) {
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        for (int k = -1; k <= 1; k++) {
                            cool(cp, reactor, x + i, y + i, z + i);
                        }
                    }
                }
            }
        } else if (component instanceof ItemReactorPlating cp) {
            if (heatrun) {
                reactor.setMaxHeat(reactor.getMaxHeat() + cp.maxHeatAdd);
                reactor.setHeatEffectModifier(reactor.getHeatEffectModifier() * cp.effectModifier);
            }
        } else if (component instanceof ItemReactorUranium cp) {
            if (reactor.produceEnergy()) {
                int basePulses = 1 + cp.numberOfCells / 2;

                for (int iteration = 0; iteration < cp.numberOfCells; ++iteration) {
                    int pulses = basePulses;
                    int heat;
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            for (int k = -1; k <= 1; k++) {
                                pulses += checkPulseable(cp, reactor, x + i, y + j, z + k, yourStack, x, y, z, heatrun);
                            }
                        }
                    }
                    if (!heatrun) {
                        for (heat = 0; heat < pulses; ++heat) {
                            acceptUraniumPulse(cp, reactor, yourStack, yourStack, x, y, z, x, y, z, heatrun);
                        }
                    } else {
                        heat = (pulses * pulses + pulses) * 2;
                        Queue<ItemStackCoord> heatAcceptors = new ArrayDeque<>();
                        for (int i = -1; i <= 1; i++) {
                            for (int j = -1; j <= 1; j++) {
                                for (int k = -1; k <= 1; k++) {
                                    checkHeatAcceptor(reactor, x + i, y + i, z + i, heatAcceptors);
                                }
                            }
                        }
                        while (!heatAcceptors.isEmpty() && heat > 0) {
                            int dheat = heat / heatAcceptors.size();
                            heat -= dheat;
                            ItemStackCoord acceptor = heatAcceptors.remove();
                            IReactorComponent acceptorComp = (IReactorComponent) acceptor.stack.getItem();
                            dheat = alterHeat(
                                acceptorComp,
                                reactor,
                                acceptor.stack,
                                acceptor.x,
                                acceptor.y,
                                acceptor.z,
                                dheat);
                            heat += dheat;
                        }

                        if (heat > 0) {
                            reactor.addHeat(heat);
                        }
                    }

                }
            }

            if (cp.getCustomDamage(yourStack) >= cp.getMaxCustomDamage(yourStack) - 1) {
                if (!reactor.outputDepletedStack(getDepletedStack(cp))) {
                    reactor.setItemAt(x, y, z, getDepletedStack(cp));
                }

            } else if (heatrun) {
                cp.applyCustomDamage(yourStack, 1, null);
            }

        }
    }

    public static boolean acceptUraniumPulse(IReactorComponent component, TST_NuclearReactor reactor,
        ItemStack yourStack, ItemStack pulsingStack, int youX, int youY, int youZ, int pulseX, int pulseY, int pulseZ,
        boolean heatrun) {
        if (component instanceof ItemReactorLithiumCell cp) {
            if (heatrun) {
                int myLevel = (int) (yourStack.getItemDamage() + 1 + reactor.getHeat() / 3000);
                if (myLevel >= cp.getMaxDamage()) {
                    reactor.setItemAt(youX, youY, youZ, new ItemStack(Ic2Items.TritiumCell.getItem()));
                } else {
                    yourStack.setItemDamage(myLevel);
                }
            }
            return true;
        } else if (component instanceof ItemReactorMOX cp) {
            if (!heatrun) {
                float breedereffectiveness = (float) reactor.getHeat() / (float) reactor.getMaxHeat();
                float ReaktorOutput = 4.0F * breedereffectiveness + 1.0F;
                reactor.addOutput(ReaktorOutput);
            }
            return true;
        } else if (component instanceof ItemReactorReflector cp) {
            if (!heatrun) {
                IReactorComponent source = (IReactorComponent) pulsingStack.getItem();
                acceptUraniumPulse(
                    source,
                    reactor,
                    pulsingStack,
                    yourStack,
                    pulseX,
                    pulseY,
                    pulseZ,
                    youX,
                    youY,
                    youZ,
                    heatrun);
            } else if (cp.getCustomDamage(yourStack) + 1 >= cp.getMaxCustomDamage(yourStack)) {
                reactor.setItemAt(youX, youY, youZ, null);
            } else {
                cp.setCustomDamage(yourStack, cp.getCustomDamage(yourStack) + 1);
            }
            return true;
        } else if (component instanceof ItemReactorUranium cp) {
            if (!heatrun) {
                reactor.addOutput(1.0F);
            }
            return true;
        }
        return false;
    }

    public static boolean canStoreHeat(IReactorComponent component, TST_NuclearReactor reactor, ItemStack stack, int x,
        int y, int z) {
        if (component instanceof ItemReactorCondensator cp) {
            return cp.getCustomDamage(stack) + 1 < cp.getMaxCustomDamage(stack);
        } else return component instanceof ItemReactorHeatStorage cp;
    }

    public static int getMaxHeat(IReactorComponent component, TST_NuclearReactor reactor, ItemStack stack, int x, int y,
        int z) {
        return component.getMaxHeat(null, stack, x, y);
    }

    public static int getCurrentHeat(IReactorComponent component, TST_NuclearReactor reactor, ItemStack stack, int x,
        int y, int z) {
        return component.getCurrentHeat(null, stack, x, y);
    }

    public static int alterHeat(IReactorComponent component, TST_NuclearReactor reactor, ItemStack stack, int x, int y,
        int z, int heat) {
        if (component instanceof ItemReactorHeatStorage cp) {
            int myHeat = cp.getCurrentHeat(null, stack, x, y);
            myHeat += heat;
            int max = cp.getMaxCustomDamage(stack);
            if (myHeat > max) {
                reactor.setItemAt(x, y, z, null);
                heat = max - myHeat + 1;
            } else {
                if (myHeat < 0) {
                    heat = myHeat;
                    myHeat = 0;
                } else {
                    heat = 0;
                }
                cp.setCustomDamage(stack, myHeat);
            }
            return heat;
        }
        return component.alterHeat(null, stack, x, y, heat);
    }

    public static float influenceExplosion(IReactorComponent component, TST_NuclearReactor reactor, ItemStack stack) {
        return component.influenceExplosion(null, null);
    }
}
