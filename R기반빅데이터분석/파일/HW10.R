library(ggplot2)

filtered_data <- subset(mpg,
                        year > 2000 &
                          substr(trans, 1, 4) == "auto" &
                          cty > 15)

ggplot(filtered_data) +
  geom_bar(aes(x = manufacturer), stat = "count")