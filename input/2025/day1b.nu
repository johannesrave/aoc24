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

      mut i = $diff
      mut new_state = $acc.state
      mut new_zeroes = $acc.zeroes

      if ($dir == "R") {
        while $i != 0 {

          $i = $i - 1
          $new_state = $new_state + 1

            if ($new_state == 100) {
              $new_state = 0
            }

            if ($new_state == 0) {
              $new_zeroes = $new_zeroes + 1
            }
          }
      }

      if ($dir == "L") {
        while $i != 0 {

          $i = $i - 1
          $new_state = $new_state - 1

            if ($new_state == -1) {
              $new_state = 99
            }

            if ($new_state == 0) {
              $new_zeroes = $new_zeroes + 1
            }
          }
      }
      
      {zeroes: $new_zeroes, state: $new_state} 
    } 
}
