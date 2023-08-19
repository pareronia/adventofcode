use serde_json::Value;
use std::{
    collections::HashMap,
    env, fs,
    io::{self, BufRead},
    path::{Path, PathBuf},
};

pub fn get_input_data(year: u16, day: u8) -> Vec<String> {
    let path = memo_dir().join(format!("{}_{:02}_input.txt", year, day));
    let file = fs::File::open(path).expect("!! INPUT DATA MISSING !");
    lines_from_file(&file)
}

pub fn answer(year: u16, day: u8, part: usize) -> Option<String> {
    let p = match part {
        1 => "a",
        2 => "b",
        _ => panic!("part should be 1 or 2"),
    };
    let path = memo_dir().join(format!("{}_{:02}{}_answer.txt", year, day, p));
    let file = fs::File::open(path);
    match file {
        Ok(file) => {
            let lines = lines_from_file(&file);
            match lines.is_empty() {
                true => None,
                false => Some(lines[0].to_string()),
            }
        }
        _ => None,
    }
}

fn aocd_dir() -> PathBuf {
    match env::var("AOCD_DIR") {
        Ok(val) => Path::new(&val).to_path_buf(),
        Err(_) => match env::consts::FAMILY {
            "unix" => match env::var("HOME") {
                Ok(val) => Path::new(&val).join(".config").join("aocd"),
                Err(_) => panic!("$HOME not defined"),
            },
            "windows" => match env::var("APPDATA") {
                Ok(val) => Path::new(&val).join("aocd"),
                Err(_) => panic!("$APPDATA not defined"),
            },
            _ => panic!("OS not supported."),
        },
    }
}

fn user_ids() -> HashMap<String, Value> {
    let filepath = aocd_dir().join("token2id.json");
    fs::File::open(&filepath).expect("Missing token2id.json");
    let json = fs::read_to_string(&filepath).unwrap();
    return serde_json::from_str(&json).unwrap();
}

fn token() -> String {
    match env::var("AOC_SESSION") {
        Ok(val) => val,
        Err(_) => {
            let filepath = aocd_dir().join("token");
            let file = fs::File::open(&filepath).expect("Missing token");
            let lines = lines_from_file(&file);
            match lines.len() {
                1 => lines[0].clone(),
                _ => panic!("Missing token"),
            }
        }
    }
}

fn memo_dir() -> PathBuf {
    let (_, user_id) = user_ids().remove_entry(&token()).unwrap();
    Path::new(&aocd_dir()).join(user_id.as_str().unwrap())
}

fn lines_from_file(file: &fs::File) -> Vec<String> {
    let buf = io::BufReader::new(file);
    buf.lines()
        .map(|l| l.expect("Could not parse line"))
        .collect()
}
