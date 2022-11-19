CC := clang++
CFLAGS := -Wall -std=c++17 -Ofast
OBJ_DIR := ../../../../../build/cpp/obj
AOC_OBJ_DIR := $(OBJ_DIR)/aoc
GRID_OBJ_DIR := $(OBJ_DIR)/aoc
GEOM_OBJ_DIR := $(OBJ_DIR)/aoc
OCR_OBJ_DIR := $(OBJ_DIR)/aoc
AOCD_OBJ_DIR := $(OBJ_DIR)/aocd
TARGET_DIR := ../../../../../build/cpp

AOC_SRCS = ../../aoc/aoc.cpp
GRID_SRCS = ../../aoc/grid/grid.cpp
GEOM_SRCS = ../../aoc/geometry/geometry.cpp
OCR_SRCS = ../../aoc/ocr/ocr.cpp
AOCD_SRCS = ../../aocd/aocd.cpp
SRCS = $(MAIN).cpp
AOC_OBJS = $(addprefix $(AOC_OBJ_DIR)/,$(notdir $(patsubst %.cpp,%.o,$(AOC_SRCS))))
GRID_OBJS = $(addprefix $(GRID_OBJ_DIR)/,$(notdir $(patsubst %.cpp,%.o,$(GRID_SRCS))))
GEOM_OBJS = $(addprefix $(GEOM_OBJ_DIR)/,$(notdir $(patsubst %.cpp,%.o,$(GEOM_SRCS))))
OCR_OBJS = $(addprefix $(OCR_OBJ_DIR)/,$(notdir $(patsubst %.cpp,%.o,$(OCR_SRCS))))
AOCD_OBJS = $(addprefix $(AOCD_OBJ_DIR)/,$(notdir $(patsubst %.cpp,%.o,$(AOCD_SRCS))))
OBJS = $(addprefix $(OBJ_DIR)/,$(notdir $(patsubst %.cpp,%.o,$(SRCS))))
TARGET = $(TARGET_DIR)/$(MAIN)

all: $(TARGET)

$(TARGET): $(AOC_OBJS) $(GRID_OBJS) $(GEOM_OBJS) $(OCR_OBJS) $(AOCD_OBJS) $(OBJS)
	$(CC) $(CFLAGS) -o $(TARGET) $(AOC_OBJS) $(GRID_OBJS) $(GEOM_OBJS) $(OCR_OBJS) $(AOCD_OBJS) $(OBJS)

$(AOC_OBJS): $(AOC_SRCS)
	@mkdir -p $(AOC_OBJ_DIR)
	$(CC) $(CFLAGS) -c $< -o $@

$(GRID_OBJS): $(GRID_SRCS)
	@mkdir -p $(GRID_OBJ_DIR)
	$(CC) $(CFLAGS) -c $< -o $@

$(GEOM_OBJS): $(GEOM_SRCS)
	@mkdir -p $(GEOM_OBJ_DIR)
	$(CC) $(CFLAGS) -c $< -o $@

$(OCR_OBJS): $(OCR_SRCS)
	@mkdir -p $(OCR_OBJ_DIR)
	$(CC) $(CFLAGS) -c $< -o $@

$(AOCD_OBJS): $(AOCD_SRCS)
	@mkdir -p $(AOCD_OBJ_DIR)
	$(CC) $(CFLAGS) -c $< -o $@

$(OBJS): $(SRCS)
	@mkdir -p $(OBJ_DIR)
	$(CC) $(CFLAGS) -c $< -o $@

clean:
	$(RM) $(AOC_OBJS) $(GRID_OBJS) $(GEOM_OBJS) $(OCR_OBJS) $(AOCD_OBJS) $(OBJS) $(TARGET)

.PHONY: all clean
