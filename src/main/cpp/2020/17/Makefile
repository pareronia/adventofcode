CC := clang++
CFLAGS := -Wall -std=c++17 -Ofast
OBJ_DIR := ../../../../../build/cpp/obj
GOL_OBJ_DIR := $(OBJ_DIR)/aoc/game_of_life
AOCD_OBJ_DIR := $(OBJ_DIR)/aocd
TARGET_DIR := ../../../../../build/cpp

GOL_SRCS = ../../aoc/game_of_life/game_of_life.cpp
AOCD_SRCS = ../../aocd/aocd.cpp
SRCS = $(MAIN).cpp
GOL_OBJS = $(addprefix $(GOL_OBJ_DIR)/,$(notdir $(patsubst %.cpp,%.o,$(GOL_SRCS))))
AOCD_OBJS = $(addprefix $(AOCD_OBJ_DIR)/,$(notdir $(patsubst %.cpp,%.o,$(AOCD_SRCS))))
OBJS = $(addprefix $(OBJ_DIR)/,$(notdir $(patsubst %.cpp,%.o,$(SRCS))))
TARGET = $(TARGET_DIR)/$(MAIN)

all: $(TARGET)

$(TARGET): $(GOL_OBJS) $(AOCD_OBJS) $(OBJS)
	$(CC) $(CFLAGS) -o $(TARGET) $(GOL_OBJS) $(AOCD_OBJS) $(OBJS)

$(GOL_OBJS): $(GOL_SRCS)
	@mkdir -p $(GOL_OBJ_DIR)
	$(CC) $(CFLAGS) -c $< -o $@

$(AOCD_OBJS): $(AOCD_SRCS)
	@mkdir -p $(AOCD_OBJ_DIR)
	$(CC) $(CFLAGS) -c $< -o $@

$(OBJS): $(SRCS)
	@mkdir -p $(OBJ_DIR)
	$(CC) $(CFLAGS) -c $< -o $@

clean:
	$(RM) $(GOL_OBJS) $(AOCD_OBJS) $(OBJS) $(TARGET)

.PHONY: all clean
