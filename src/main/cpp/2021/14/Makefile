CC := clang++
CFLAGS := -Wall -std=c++1z -Ofast
OBJ_DIR := ../../../../../build/cpp/obj
AOC_OBJ_DIR := $(OBJ_DIR)/aoc
AOCD_OBJ_DIR := $(OBJ_DIR)/aocd
TARGET_DIR := ../../../../../build/cpp

AOC_SRCS = ../../aoc/aoc.cpp
AOCD_SRCS = ../../aocd/aocd.cpp
SRCS = $(MAIN).cpp
AOC_OBJS = $(addprefix $(AOC_OBJ_DIR)/,$(notdir $(patsubst %.cpp,%.o,$(AOC_SRCS))))
AOCD_OBJS = $(addprefix $(AOCD_OBJ_DIR)/,$(notdir $(patsubst %.cpp,%.o,$(AOCD_SRCS))))
OBJS = $(addprefix $(OBJ_DIR)/,$(notdir $(patsubst %.cpp,%.o,$(SRCS))))
TARGET = $(TARGET_DIR)/$(MAIN)

all: $(TARGET)

$(TARGET): $(AOC_OBJS) $(AOCD_OBJS) $(OBJS)
	$(CC) $(CFLAGS) -o $(TARGET) $(AOC_OBJS) $(AOCD_OBJS) $(OBJS)

$(AOC_OBJS): $(AOC_SRCS)
	@mkdir -p $(AOC_OBJ_DIR)
	$(CC) $(CFLAGS) -c $< -o $@

$(AOCD_OBJS): $(AOCD_SRCS)
	@mkdir -p $(AOCD_OBJ_DIR)
	$(CC) $(CFLAGS) -c $< -o $@

$(OBJS): $(SRCS)
	@mkdir -p $(OBJ_DIR)
	$(CC) $(CFLAGS) -c $< -o $@

clean:
	$(RM) $(AOC_OBJS) $(AOCD_OBJS) $(OBJS) $(TARGET)

.PHONY: all clean
