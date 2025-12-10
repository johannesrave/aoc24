#!/usr/bin/env nu
use std repeat

def main [
  --input-file-path: path = "./input3.txt"
  --part: string = "a"
] {
  if ($part == "a") {
    a $input_file_path
  }
  if ($part == "b") {
    b $input_file_path
  }
}

def a [ input_file_path: path ] {
  $input_file_path
  | open
  | lines
  | inspect
  | each { |line|
    let max = $line | split chars | drop | into int | math max
    let index_of_max = $line | str index-of ($max | into string)
    let second_max = $line | str substring ($index_of_max + 1).. | split chars | into int | math max
    [ $max $second_max ] | into string | str join | into int 
  }
  | inspect
  | math sum
  | inspect
}


def b [ input_file_path: path ] {
  $input_file_path
  | open
  | lines
  | inspect
  | each { |line|
    mut ints = $line
    mut result = []
    for n in 11..0 {
      
      let max = $ints | split chars | drop $n | into int | math max
      let index_of_max = $ints | str index-of ($max | into string)

      $result = $result | append $max
      
      [ $n $ints $max $index_of_max $result ] | inspect
      
      $ints = $ints | str substring ($index_of_max + 1)..
    }
    $result | into string | str join | into int 
  }
  | inspect
  | math sum
  | inspect
}
# too low:   167405696846035
# too high:  167523425665348
# correct:   169559000223645
# too high: 1674056968461075
