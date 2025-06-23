suicide_original_data <- read.csv(file="suicide_rates.csv", fileEncoding="UTF-8")
head(suicide_original_data, 3)

suicide_sorted_data <- suicide_original_data[order(suicide_original_data$Males, decreasing=TRUE),]
# suicide_sorted_data <- suicide_original_data[order(-suicide_original_data$Males), ]
head(suicide_sorted_data, 3)

second_highest_suicide <- suicide_sorted_data[2, "Country"]
second_highest_suicide