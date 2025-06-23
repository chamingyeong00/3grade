a<-matrix(sample(1:25), nrow=5, ncol=5)
sum<-0
for(i in 1:5)
  sum <- sum + a[i, i]
sum

