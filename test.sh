#! /bin/bash

commands=(
      "curl -i http://localhost"
       "curl -i http://localhost/index.html"
       "curl -i http://localhost/invalid.html"
)
parallel --jobs 3 ::: "${commands[@]}"

echo "All commands completed"
