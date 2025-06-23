library(dplyr)
library(ggplot2)

mpg_filtered <- mpg %>%
  filter(year < 2000,
         mpg$trans %in% c("manual(m5)", "manual(m6)"),
         hwy > 20) %>%
  group_by(manufacturer) %>%
  summarise(count = n()) 

print(mpg_filtered)