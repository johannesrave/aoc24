#!/usr/bin/env nu

def main [
  --input-file-path: path = "./input1.txt"
] {
  $input_file_path
  | open
  | lines
  | reduce --fold {zeroes: 0, state: 50} { |it, acc|
      let dir = $it | split chars | first
      let diff = $it | split chars | skip 1 | str join | into int

      let new_state = if ($dir == "R") {
        ($acc.state + $diff) mod 100
        } else {
        ($acc.state - $diff) mod 100
      }
      
      let new_zeroes = if ($new_state == 0) {
        $acc.zeroes + 1
        } else {
        $acc.zeroes
      }
    
      {zeroes: $new_zeroes, state: $new_state} 
    } 
}
