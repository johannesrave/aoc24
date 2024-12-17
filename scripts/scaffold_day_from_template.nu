#!/usr/bin/env nu
use std assert

def main [ --day_number: string, --force (-f)] {
  let template_file = glob "src/**/templates/DayXX.kt" | first
  let target_file = $"src/main/kotlin/codes/jrave/Day($day_number).kt"
  let input_file = $"input/day($day_number)_input"
  let test_file = $"input/day($day_number)_test"

  print $"creating ($input_file) for Day($day_number)"
  touch $input_file
  print $"creating ($test_file) for Day($day_number)"
  touch $test_file

  print $"creating ($target_file) for Day($day_number)"
  open $template_file | str replace --all "XX" $day_number 
  | if $force { save --force $target_file } else { save $target_file }

  print "done."
}
