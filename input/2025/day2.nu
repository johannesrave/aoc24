#!/usr/bin/env nu
use std repeat

def main [
  --input-file-path: path = "./input2.txt"
  --part: string = "a"
] {
  if ($part == "a") {
    a $input_file_path
  } else if ($part == "b") {
    b $input_file_path
  } | inspect
}

def a [
  input_file_path: path = "./input2.txt"
] {
  $input_file_path
  | open
  | str replace --all -r "," "\n"
  | lines
  | parse -r "(.*)-(.*)"
  | rename start end
  | update start { $in | into int }
  | update end   { $in | into int }
  | each {
    $in.start..$in.end
    | par-each { |n| $n | into string }
    | where    { |s| ($s | str length) mod 2 == 0 }
    | par-each { |s| {id:$s, half-len: (($s | str length) // 2 )}}
    | where    { |row| $row.id | str ends-with ( $row.id | str substring ..<$row.half-len )}
    | get id
    | into int
  }
  | flatten
  | math sum
}


# TODO: only count up from a half, a third, a fourth, a fifth of the original string
# until the combined string into int is greater than the second number
def b [
  input_file_path: path = "./input2.txt"
] {
  $input_file_path
  | open
  | str replace --all -r "," "\n"
  | lines --skip-empty
  | parse -r "(.*)-(.*)"
  | rename start end
  | inspect
  | each { |pair|
    $pair | inspect
    let start_length = $pair.start | str length
    let end_length   = $pair.end   | str length
    let start_int    = $pair.start | into int
    let end_int      = $pair.end   | into int

    # 121-1200
    # ~111
    # 222
    # 333
    # 444
    # 555
    # 666
    # 777
    # 888
    # 999
    # 1010
    # 1111
    # 1212
    # ~1313

    mut invalid_ids = []

    for pattern in 1..99999 {

      let pattern_str = $pattern | into string
      let test_id     = $pattern_str | repeat-until $start_length
      let test_id_int: int = $test_id | into int

      { test_id_int: $test_id_int, pattern_str: $pattern_str } | inspect

      if ($test_id_int >= $start_int and $test_id_int <= $end_int) {
        $invalid_ids = $invalid_ids | append $test_id_int
      }
      if ($pattern_str | str length) > ($start_length // 2) {
        break
      }
    }

    for pattern in 1..99999 {

      let pattern_str = $pattern | into string
      let test_id     = $pattern_str | repeat-until $end_length
      let test_id_int: int = $test_id | into int

      { test_id_int: $test_id_int, pattern_str: $pattern_str } | inspect

      if ($test_id_int >= $start_int and $test_id_int <= $end_int) {
        $invalid_ids = $invalid_ids | append $test_id_int
      }
      if ($pattern_str | str length) > ($end_length // 2) {
        break
      }
    }

   $invalid_ids | uniq | inspect
  }
  | flatten
  | inspect
  | math sum
}
# 31578210022 (correct)
# 31576940726 (only start_length)
# 31578210033 (start_length and end_length... an 11 is duplicated?)

def repeat-until [ length: int] {
  let pattern = $in
  let patternlength = $pattern | str length
  let times = $length // $patternlength

  $pattern | repeat $times | str join
}
