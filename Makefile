# statics
SRC_ROOT := src
SRC_ROOT_MAIN := $(SRC_ROOT)/main
SRC_ROOT_TEST := $(SRC_ROOT)/test
PYTHON_ROOT := $(SRC_ROOT_MAIN)/python
PYTHON_TEST_ROOT := $(SRC_ROOT_TEST)/python
CLITEST_ROOT := $(SRC_ROOT_TEST)/clitest
PYTHON_PATH := PYTHONPATH=$(PYTHON_ROOT)
JAVA_ROOT := $(SRC_ROOT_MAIN)/java
JAVA_TEST_ROOT := $(SRC_ROOT_TEST)/java
JAVA_DST_ROOT := target
JAVA_DST := $(JAVA_DST_ROOT)/classes
CFG := setup.cfg
BANDIT := bandit --silent --ini $(CFG)
FLAKE := flake8
VULTURE := vulture --min-confidence 80
GREP := grep
GAWK := awk
SORT := sort
SED := sed
CLITEST := clitest
PYTHON_CMD := python -O
UNITTEST_CMD := -m unittest discover -s $(PYTHON_TEST_ROOT)
JAVA_CMD := java -ea
JAVAC_CMD := javac -encoding cp1252
RM := rm -Rf
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# vars
MAKEFILE = $(realpath $(lastword $(MAKEFILE_LIST)))
PY_SRCS = $(shell find $(PYTHON_ROOT) $(PYTHON_TEST_ROOT) -name "*.py")
JAVA_SRCS = $(shell find $(JAVA_ROOT) $(JAVA_TEST_ROOT) -name "*.java")
CLITEST_SRCS = $(shell find $(CLITEST_ROOT) -name "*.md")
SRCS = $(PY_SRCS) $(JAVA_SRCS) $(CLITEST_SRCS) $(MAKEFILE)
JAVA_LIBS = $(shell find $(JAVA_LIB_ROOT) -name "*.jar" -not -name "*-sources.jar")
JAVA_CP_LIBS = $(call to_path,$(JAVA_LIBS))

# functions
msg = (if [ -t 1 ]; then echo ${BLUE}"\n$1\n"${NC}; else echo "$1"; fi)

igrep = ($(GREP) --line-number --recursive --word-regexp --color=auto \
		--ignore-case $1 $2)

day = $(shell echo $1 | $(GAWK) --field-separator=, \
		'{print "AoC"$$1"_"$$2""$2""}')

to_path = $(shell echo $1 | $(GAWK) '{printf $$1; for (i = 2; i <= NF; i++) { if ($$i != "") { printf ":"$$i }; }}')

#: Default target - pre-push
.DEFAULT_GOAL := pre-push

#: Run Python (with ARGS=year,day)
py:
	@$(PYTHON_CMD) $(PYTHON_ROOT)/$(call day,$(ARGS),".py")

#: Run Java (with ARGS=year,day)
java:
	@$(JAVA_CMD) -cp $(JAVA_CP_LIBS):$(JAVA_DST) $(call day,$(ARGS),"")

#: Build Java
build_java:
	@$(JAVAC_CMD) -cp $(JAVA_CP_LIBS) -d $(JAVA_DST) $(JAVA_SRCS)

#: Run unit tests
unittest:
	@$(PYTHON_PATH) $(PYTHON_CMD) $(UNITTEST_CMD)

#: Run command line integration tests
clitest:
	@$(call msg,"Running clitest...")
	@$(CLITEST) $(CLITEST_SRCS)

#: Run all tests (unit tests, command line integration tests)
alltest: unittest clitest

#: Run Flake8 Python code linter
flake:
	@$(call msg,"Running Flake8 against Python source files...")
	@$(FLAKE) $(PY_SRCS)

#: Run Vulture - unused Python code checker
vulture:
	@$(call msg,"Running vulture against Python source files...")
	@$(VULTURE) $(PY_SRCS)

#: Run Bandit - Python code security linter
bandit:
	@$(call msg,"Running bandit against Python source files...")
	@$(BANDIT) $(PY_SRCS)

#: Run all linters (Flake8, Vulture, Bandit)
lint: flake vulture bandit

fixme todo:
	-@$(call igrep,"$@",$(PY_SRCS) $(JAVA_SRCS))

#: Show FIXMEs and TODOs in code files
tasks: fixme todo

#: git pre-push hook: show tasks, linters, unit tests
pre-push: tasks lint unittest

#: Show values of some selected make variables
dump:
	@echo "JAVA_LIBS: "$(JAVA_LIBS)""
	@echo "JAVA_SRCS: "$(JAVA_SRCS)""
	@echo "PY_SRCS: "$(PY_SRCS)""
	@echo "SRCS: "$(SRCS)""

#: Clean up generated files
clean:
	$(RM) $(JAVA_DST)

# https://stackoverflow.com/a/26339924
#: Show all targets in this Makefile
list:
	@$(MAKE) --print-data-base --no-builtin-variables --no-builtin-rules \
			 --question --makefile=$(MAKEFILE) : 2>/dev/null\
	   	| $(GAWK) --assign=RS= --field-separator=: \
			'/^# File/,/^# Finished Make data base/ \
	   		{if ($$1 !~ "^[#.]") {print $$1}}'\
	   	| $(SORT)\
	   	| $(GREP) --extended-regexp --invert-match \
	   		--regexp='^[^[:alnum:]]' --regexp='^$@$$'

# https://stackoverflow.com/a/59087509
#: Show all targets in this Makefile with help text
help:
	@$(GREP) --before-context=1 --extended-regexp "^[a-zA-Z0-9_-]+\:" \
			$(MAKEFILE) | $(GREP) --invert-match -- -- \
		| $(SED) 'N;s/\n/###/' \
		| $(SED) --quiet 's/^#: \(.*\)###\(.*\):.*/\2###\1/p' \
		| column -t -s '###' \
		| $(SORT)

.PHONY: flake vulture bandit fixme todo list help py java unittest clitest \
	build_java clean
