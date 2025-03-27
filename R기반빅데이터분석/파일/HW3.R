total <- 0
x <- 1:1000
for (i in x){
  if (i %% 3 == 0){
    total <- total + i
  }
}
print(total)