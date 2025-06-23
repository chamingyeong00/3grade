library(tidyr)

table4a %>%
  pivot_longer(c('1999', '2000'), names_to = "year", values_to = "cases") %>%
  ggplot(aes(country, cases, fill=year)) + geom_col() 