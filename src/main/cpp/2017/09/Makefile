CC := g++
CFLAGS := -Wall -std=c++17 -Ofast
OBJ_DIR := ../../../../../build/cpp/obj
AOCD_OBJ_DIR := $(OBJ_DIR)/aocd
TARGET_DIR := ../../../../../build/cpp

AOCD_SRCS = ../../aocd/aocd.cpp
SRCS = $(MAIN).cpp
AOCD_OBJS = $(addprefix $(AOCD_OBJ_DIR)/,$(notdir $(patsubst %.cpp,%.o,$(AOCD_SRCS))))
OBJS = $(addprefix $(OBJ_DIR)/,$(notdir $(patsubst %.cpp,%.o,$(SRCS))))
TARGET = $(TARGET_DIR)/$(MAIN)

all: $(TARGET)

$(TARGET): $(AOCD_OBJS) $(OBJS)
	$(CC) $(CFLAGS) -o $(TARGET) $(AOC_OBJS) $(AOCD_OBJS) $(OBJS)

$(AOCD_OBJS): $(AOCD_SRCS)
	@mkdir -p $(AOCD_OBJ_DIR)
	$(CC) $(CFLAGS) -c $< -o $@

$(OBJS): $(SRCS)
	@mkdir -p $(OBJ_DIR)
	$(CC) $(CFLAGS) -c $< -o $@

clean:
	$(RM) $(AOCD_OBJS) $(OBJS) $(TARGET)

.PHONY: all clean
