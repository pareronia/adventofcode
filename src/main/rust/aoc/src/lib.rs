pub trait Puzzle {
    fn year(&self) -> u16;
    fn day(&self) -> u8;
    fn get_input_data(&self) -> Vec<String> {
        aocd::get_input_data(self.year(), self.day())
    }
    fn samples(&self) {}
    fn part_1(&self, lines: &Vec<String>) -> String;
    fn part_2(&self, lines: &Vec<String>) -> String;
    fn run(&self) {
        if cfg!(debug_assertions) {
            self.samples();
        }
        let lines = self.get_input_data();
        println!("Part 1: {}", self.part_1(&lines));
        println!("Part 2: {}", self.part_2(&lines));
    }
}

pub fn to_blocks(lines: &Vec<String>) -> Vec<Vec<&String>> {
    let mut blocks = vec![];
    let mut idx: usize = 0;
    blocks.push(vec![]);
    lines.iter().for_each(|line| {
        if line.len() == 0 {
            blocks.push(vec![]);
            idx += 1;
        } else {
            blocks[idx].push(line);
        }
    });
    blocks
}

pub fn split_lines(s: &str) -> Vec<String> {
    s.lines().map(|line| String::from(line)).collect()
}
