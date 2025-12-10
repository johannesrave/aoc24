#!/usr/bin/env nu

def main [
  --input-file-path: path = "./input4.txt"
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
  let board: list<list<string>> = $input_file_path
  | open
  | lines
  | split chars

  let x_length = $board.0 | length
  let y_length = $board   | length

  let coords = 0..<$y_length
  | each { |y| 0..<$x_length | each {|x| [$y $x]} }
  | flatten
  | where { |n| $board | get ($n | into cell-path) | do { $in == "@" } }

  let reachable_coords = $coords
  | par-each { |cs|
    let blocked_neighbours = $cs
    | find_neighbours $x_length $y_length
    | where { |n| $board | get ($n | into cell-path) | do { $in == "@" } }

    { coords: $cs, blocked_neighbours: $blocked_neighbours }
  } | inspect
  | where { ($in.blocked_neighbours | length) < 4 }
  # | find_reachable_coords $board
  
  $reachable_coords
  | length
  | inspect
}
# 1564

def b [ input_file_path: path ] {
  let board: list<list<string>> = $input_file_path
  | open
  | lines
  | split chars

  let x_length = $board.0 | length
  let y_length = $board   | length

  let coords = 0..<$y_length
  | each { |y| 0..<$x_length | each {|x| [$y $x]} }
  | flatten
  | where { |n| $board | get ($n | into cell-path) | do { $in == "@" } }

  mut remaining_coords = $coords

  loop {
   
    let reachable_coords = $remaining_coords
    | par-each { |cs|
      let blocked_neighbours = $cs
      | find_neighbours $x_length $y_length
      | where { |n| $board | get ($n | into cell-path) | do { $in == "@" } }

      { coords: $cs, blocked_neighbours: $blocked_neighbours }
    } | inspect
    | where { |cs| (($cs | get blocked_neighbours | length) < 4) }
    | tee { $in | debug }
    | get coords
    | inspect
      
    if ($reachable_coords | is-empty) { break }

    $remaining_coords = $remaining_coords | where { |c| $c not-in $reachable_coords } 
  }

  ($coords | length) - ($remaining_coords | length) | inspect
  }

def find_neighbours [ x_length y_length ] {
    let coords = $in

    let x = $coords.1
    let y = $coords.0

    let x_l = $x - 1
    let x_r = $x + 1
    let y_u = $y - 1
    let y_d = $y + 1

  let neighbour_coords = [
    [ $y_u, $x_l ] [ $y_u, $x ] [ $y_u, $x_r ]
    [ $y,   $x_l ]              [ $y,   $x_r ]
    [ $y_d, $x_l ] [ $y_d, $x ] [ $y_d, $x_r ]
  ]

  # $neighbour_coords | inspect

  $neighbour_coords | where { |c|
    $c.0 >= 0 and $c.0 < $y_length and $c.1 >= 0 and $c.1 < $x_length
  }
}

def is_full [ board: list<list<string>>, match: string] {
  let coords = $in
  $board | get ($coords | into cell-path) | do { $in == $match }
}


