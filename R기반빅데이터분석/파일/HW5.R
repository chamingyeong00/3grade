x <- matrix(sample(1:25), nrow=5, ncol=5)
transposed_x <- matrix(0, nrow=ncol(x), ncol=nrow(x))

for (i in 1:nrow(x)) {
  for (j in 1:ncol(x)){
    transposed_x[j, i] <- x[i, j]
  }
}

print(x)
print(transposed_x)