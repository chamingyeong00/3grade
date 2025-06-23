library(ggplot2)

filtered_data = subset(mpg, cty > 15 & year > 2005)

p <- ggplot(filtered_data, aes(displ, cty, color=class, shape=factor(cyl)))+
  geom_point()

p