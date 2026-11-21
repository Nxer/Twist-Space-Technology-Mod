---
navigation:
  title: Operating formulas
  parent: index.md
  position: 8
  icon: gregtech:gt.blockmachines:19804
---

# Operating formulas

Use your server's recipe settings. Times are in ticks, with 20 ticks per second. See [worked examples](examples.md) for numerical results.

## Radius, parallel and starting supplies

Let `r₀` be the original radius, `r` the final logical radius, and `p` the actual parallel. `V(r)` counts the integer points satisfying the condition below. Miner work `W(r)` is the number of points for T1, the number of nonempty rows (distinct y,z pairs) for T2, and the number of horizontal layers `2r+1` for T3.

$$
x^2+y^2+z^2 \le r(r+1),\quad |x|,|y|,|z|\le r
$$

Supply `p` focuses and `p` of each active meteor reagent per batch. Mining at the original radius costs zero Mineral Seed Cores. To expand to radius `r`, use the formulas below to stock the required cores, Octuple Compressed Cobblestone and Life Essence. `L₀` is the original meteor's essence cost.

$$
N_{\rm seed}=p\,\left\lceil64\left((r/r_0)^2-1\right)\right\rceil
$$

$$
N_{\rm stone}=\left\lceil p\,V(r)/V(32)\right\rceil
$$

$$
L=\left\lceil\max(1,L_0)\,p\,V(r)/V(r_0)\right\rceil
$$

Count LuV as tier 6. Radius requires tier `6 + ceil(log₄ r)` at GT recipe power VP for that tier. Total input power `H` must cover both this threshold and mining power. The controller first sets the highest whole parallel that supplies, fuel and fleet support at the original radius. It then holds that parallel and expands to the largest whole radius it can support, up to 1024.

## Fleet parallel limit

Let `g` be the highest effective tier (LV=1, MAX=14) and `n` its drone count, up to 4096. Infusion adds one tier up to MAX. An empty fleet allows one parallel. Otherwise first find the full-fleet count `C` and unrounded peak `Q`.

$$
C_g=\max(1,\operatorname{round}(24\cdot 1.5^{g-8}))
$$

$$
Q_g=(1+0.48\cdot 1.5^{g-9})^{8+(g-9)/2}
$$

For a full or oversized fleet, set `h=0`. For a fleet below capacity, calculate the shortfall penalty `h` as follows.

$$
a_g=\frac{1-0.5(Q_{g-1}-1)/(Q_g-1)}{(1-C_{g-1}/C_g)^2}
$$

$$
h=a_g(1-n/C_g)^2\quad(n<C_g)
$$

Then find the surplus penalty `j` and fleet limit `P`. Supply constraints can reduce the actual parallel `p`.

$$
s=\max(0,n/C_g-1),\quad j=\frac{s^2}{2(1+s^2)}
$$

$$
P=\max(1,\lfloor1+(Q_g-1)(1-h-j)\rfloor)
$$

## Mining overclocks and duration

The base cycle per mining unit is `d₀` ticks (default 40) at 120 EU/t. The power multiplier `a` defaults to 4 and the time divisor `b` to √2. For voltage `U` and effective amperage `I` supplied to the miner's overclock calculator, the budget is `B = floor(U/16) × I`. Use one amp when amperage overclocking is off. A single ordinary energy hatch follows GT's amperage rules.

$$
d_k=\max(1,\lceil d_0/b^k\rceil),\qquad E_k=\lceil120\,a^k\rceil
$$

Start with zero overclocks. Each next overclock `k` must afford `120 × aᵏ`, with the preceding cycle still longer than one tick. GT also caps the count at `max(0, floor(log₄(B/120)))`. With amperage overclocking off the GT voltage-tier difference between the reduced voltage and base recipe also applies. Use the resulting `dₖ` and `Eₖ` for mining.

`G` is formation time including core nucleation. Beam connection takes another 20 ticks. `M` is mining time and `T` is the planned batch duration. Formation and beam connection draw total input power `H`.

$$
r_v=\min(r,48),\quad G=20+\max(20,\lceil200r_v/48\rceil)
$$

$$
M=\left\lceil W(r)\,d_k\,2^{\min(2,(p-1)/16)}\right\rceil
$$

$$
T=G+20+M,\quad {\rm EU}_{\rm run}=H(G+20)+E_kM
$$

## Helium and infusion

`n_ordinary` counts every ordinary drone at departure, including lower-tier passengers. Prepay helium plasma for the scheduled mining ticks. Each full second of mining gives every ordinary drone in the highest effective tier an independent 1/3600 chance of infusion. The second expression gives one eligible drone's chance of returning infused.

$$
L_{\rm He}=50\,n_{\rm ordinary}M
$$

$$
p_{\rm infused}=1-(3599/3600)^{\lfloor M/20\rfloor}
$$

## Yield and delivery

First calculate the filler chance. Let `c₀` be the recipe's original filler percentage. Add the largest direct increase and the most negative direct decrease among active reagents to get `A`. For the proportional adjustment `d`, take the largest positive modifier if present, otherwise the most negative one, or zero if neither exists. If the recipe starts with no filler or a reagent supplies a −100 proportional decrease, use `c=0`. Otherwise use `c = (c₀ + A + d) / (100 + d)` and clamp it to the interval from 0 to 1. Keep entries whose reagent requirements are met. A valid reagent filler list replaces the original filler list.

Apply reagents before forming the ore and filler groups. For filler chance `c`, use `f=1−c` for ore and `f=c` for filler. Normalize the positive weights `wᵢ` within each group. `Fᵢ` is the miner's Fortune level, or zero for filler and the recipe's default stone.

$$
q_i=f\,w_i/\sum_j w_j,\qquad A_i=V(r)\,p\,q_i\,(1+F_i/2)
$$

Award `floor(Aᵢ)` per entry, plus one with probability equal to its fractional part. Add matching items to get `Nᵢ`. Batch mode delivers that total at the end. In stepwise mode, `v₀` and `v₁` count the visible blocks removed before and after a cut. Use the display-radius volume to divide up the total. At elapsed mining tick `t`, the last checkpoint is `t_c = 20 × floor(t/20)`, or `t_c = M` at completion. At that checkpoint `floor(W(r_v) × t_c / M)` visible work units have been cut. Count the blocks in those units for the miner tier to obtain `v`.

$$
D_i=\lfloor N_i\,v_1/V(r_v)\rfloor-\lfloor N_i\,v_0/V(r_v)\rfloor
$$

The current step's rate is `20 × step yield / step ticks`. The theoretical continuous rate is `20 × batch yield / (T+40)`. The 40-tick interval begins when mining finishes. Pauses and blocked output affect measured throughput.
