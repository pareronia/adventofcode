#![allow(non_snake_case)]

use aoc::Puzzle;
use std::collections::HashMap;

const N: usize = 92;

struct Inputs {
    sequence: Vec<String>,
    decays: Vec<Vec<usize>>,
    start: String,
}

struct AoC2015_10;

impl AoC2015_10 {
    fn solve(&self, inputs: &Inputs, iterations: usize) -> usize {
        let mut current = [0_usize; N];
        let n = inputs
            .sequence
            .iter()
            .position(|s| *s == inputs.start)
            .unwrap();
        current[n] = 1;
        (0..iterations).for_each(|_| {
            let mut nxt = [0_usize; N];
            current.iter().enumerate().for_each(|(i, c)| {
                if *c > 0 {
                    for d in inputs.decays[i].iter() {
                        nxt[*d] += c;
                    }
                }
            });
            current = nxt;
        });
        current
            .iter()
            .zip(inputs.sequence.iter())
            .map(|(c, s)| c * s.len())
            .sum()
    }
}

impl aoc::Puzzle for AoC2015_10 {
    type Input = Inputs;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2015, 10);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        let elements: Vec<Vec<&str>> = ELEMENTS
            .lines()
            .map(|line| line.split_whitespace().collect::<Vec<_>>())
            .collect();
        let mut indices = HashMap::with_capacity(N);
        for (i, sp) in elements.iter().enumerate() {
            indices.insert(sp[2], i);
        }
        let mut sequence = Vec::with_capacity(N);
        let mut decays: Vec<Vec<usize>> = Vec::with_capacity(N);
        for sp in elements {
            sequence.push(sp[0].to_string());
            let mut d: Vec<usize> = Vec::with_capacity(6);
            sp[4..]
                .iter()
                .for_each(|s| d.push(*indices.get(*s).unwrap()));
            decays.push(d);
        }
        Inputs {
            sequence,
            decays,
            start: lines[0].to_string(),
        }
    }

    fn part_1(&self, inputs: &Self::Input) -> Self::Output1 {
        self.solve(inputs, 40)
    }

    fn part_2(&self, inputs: &Self::Input) -> Self::Output2 {
        self.solve(inputs, 50)
    }

    fn samples(&self) {}
}

fn main() {
    AoC2015_10 {}.run(std::env::args());
}

const ELEMENTS: &str = "\
22 -> H -> H
13112221133211322112211213322112 -> He -> Hf Pa H Ca Li
312211322212221121123222112 -> Li -> He
111312211312113221133211322112211213322112 -> Be -> Ge Ca Li
1321132122211322212221121123222112 -> B -> Be
3113112211322112211213322112 -> C -> B
111312212221121123222112 -> N -> C
132112211213322112 -> O -> N
31121123222112 -> F -> O
111213322112 -> Ne -> F
123222112 -> Na -> Ne
3113322112 -> Mg -> Pm Na
1113222112 -> Al -> Mg
1322112 -> Si -> Al
311311222112 -> P -> Ho Si
1113122112 -> S -> P
132112 -> Cl -> S
3112 -> Ar -> Cl
1112 -> K -> Ar
12 -> Ca -> K
3113112221133112 -> Sc -> Ho Pa H Ca Co
11131221131112 -> Ti -> Sc
13211312 -> V -> Ti
31132 -> Cr -> V
111311222112 -> Mn -> Cr Si
13122112 -> Fe -> Mn
32112 -> Co -> Fe
11133112 -> Ni -> Zn Co
131112 -> Cu -> Ni
312 -> Zn -> Cu
13221133122211332 -> Ga -> Eu Ca Ac H Ca Zn
31131122211311122113222 -> Ge -> Ho Ga
11131221131211322113322112 -> As -> Ge Na
13211321222113222112 -> Se -> As
3113112211322112 -> Br -> Se
11131221222112 -> Kr -> Br
1321122112 -> Rb -> Kr
3112112 -> Sr -> Rb
1112133 -> Y -> Sr U
12322211331222113112211 -> Zr -> Y H Ca Tc
1113122113322113111221131221 -> Nb -> Er Zr
13211322211312113211 -> Mo -> Nb
311322113212221 -> Tc -> Mo
132211331222113112211 -> Ru -> Eu Ca Tc
311311222113111221131221 -> Rh -> Ho Ru
111312211312113211 -> Pd -> Rh
132113212221 -> Ag -> Pd
3113112211 -> Cd -> Ag
11131221 -> In -> Cd
13211 -> Sn -> In
3112221 -> Sb -> Pm Sn
1322113312211 -> Te -> Eu Ca Sb
311311222113111221 -> I -> Ho Te
11131221131211 -> Xe -> I
13211321 -> Cs -> Xe
311311 -> Ba -> Cs
11131 -> La -> Ba
1321133112 -> Ce -> La H Ca Co
31131112 -> Pr -> Ce
111312 -> Nd -> Pr
132 -> Pm -> Nd
311332 -> Sm -> Pm Ca Zn
1113222 -> Eu -> Sm
13221133112 -> Gd -> Eu Ca Co
3113112221131112 -> Tb -> Ho Gd
111312211312 -> Dy -> Tb
1321132 -> Ho -> Dy
311311222 -> Er -> Ho Pm
11131221133112 -> Tm -> Er Ca Co
1321131112 -> Yb -> Tm
311312 -> Lu -> Yb
11132 -> Hf -> Lu
13112221133211322112211213322113 -> Ta -> Hf Pa H Ca W
312211322212221121123222113 -> W -> Ta
111312211312113221133211322112211213322113 -> Re -> Ge Ca W
1321132122211322212221121123222113 -> Os -> Re
3113112211322112211213322113 -> Ir -> Os
111312212221121123222113 -> Pt -> Ir
132112211213322113 -> Au -> Pt
31121123222113 -> Hg -> Au
111213322113 -> Tl -> Hg
123222113 -> Pb -> Tl
3113322113 -> Bi -> Pm Pb
1113222113 -> Po -> Bi
1322113 -> At -> Po
311311222113 -> Rn -> Ho At
1113122113 -> Fr -> Rn
132113 -> Ra -> Fr
3113 -> Ac -> Ra
1113 -> Th -> Ac
13 -> Pa -> Th
3 -> U -> Pa
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2015_10 {}.samples();
    }
}
