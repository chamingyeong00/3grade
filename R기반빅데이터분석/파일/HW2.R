score <- 94

if (score >= 95){
  grade <- 'A+'
} else if (score >= 90){
  grade <- 'A'
} else if (score >= 85){
  grade <- 'B+'
} else if (score >= 80){
  grade <- 'B'
} else if (score >= 75){
  grade <- 'C+'
} else if (score >= 70){
  grade <- 'C'
} else if (score >= 65){
  grade <- 'D+'
} else if (score >= 60){
  grade <- 'D'
} else {
  grade <- 'F'
}

print(grade)

