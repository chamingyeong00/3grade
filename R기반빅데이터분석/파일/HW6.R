a <- matrix(1:16, 4, 4)
a

matrix_diagonal_sum <- function(a) {
  result <- 0
  n <- nrow(a)
  for (i in 1:n) {
    result <- result + a[i, i]
  }
  return(result)
}

matrix_diagonal_sum(a)

