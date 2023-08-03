# statics
SRC_ROOT := src
DST_ROOT := build
SRC_ROOT_MAIN := $(SRC_ROOT)/main
SRC_ROOT_TEST := $(SRC_ROOT)/test
PYTHON_ROOT := $(SRC_ROOT_MAIN)/python
PYTHON_TEST_ROOT := $(SRC_ROOT_TEST)/python
CLITEST_ROOT := $(SRC_ROOT_TEST)/clitest
PYTHON_PATH := PYTHONPATH=$(PYTHON_ROOT)
JAVA_ROOT := $(SRC_ROOT_MAIN)/java
JAVA_TEST_ROOT := $(SRC_ROOT_TEST)/java
JAVA_DST_ROOT := $(DST_ROOT)/java
JAVA_DST := $(JAVA_DST_ROOT)/classes
JAVA_TEST_DST := $(JAVA_DST_ROOT)/test-classes
BASH_ROOT := $(SRC_ROOT_MAIN)/bash
CPP_ROOT := $(SRC_ROOT_MAIN)/cpp
CPP_TEST_ROOT := $(SRC_ROOT_TEST)/cpp
CPP_DST_ROOT := $(DST_ROOT)/cpp
JULIA_ROOT := $(SRC_ROOT_MAIN)/julia
RUST_ROOT := $(SRC_ROOT_MAIN)/rust
CFG := pyproject.toml
SHELLCHECK := shellcheck -a -P SCRIPTDIR
BANDIT := bandit --silent --configfile $(CFG)
FLAKE := flake8
VULTURE := vulture
PMD := pmd pmd -R rulesets/java/quickstart.xml
PMD_CACHE_DIR := .cache/pmd
PMD_HTML_DIR := htmlpmd
PMD_HTML := $(PMD_HTML_DIR)/pmd.html
GREP := grep
GAWK := awk
SORT := sort
SED := sed
CLITEST := clitest
PYTHON_CMD := python -O
PYTHON_UNITTEST_CMD := -m unittest discover -s $(PYTHON_TEST_ROOT)
JAVA_CMD := $(JAVA_EXE) -ea
JAVAC_CMD := $(JAVAC_EXE) -encoding utf-8
JAVA_UNITTEST_CMD := org.junit.platform.console.ConsoleLauncher
JULIA_CMD := julia --optimize
CARGO_CMD := cargo
RUSTFMT := rustfmt
BAZEL := bazel
WSLPATH := wslpath
RM := rm -Rf
MKDIR := mkdir
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# vars
MAKEFILE = $(realpath $(lastword $(MAKEFILE_LIST)))
PY_SRCS = $(shell find $(PYTHON_ROOT) $(PYTHON_TEST_ROOT) -name "*.py")
JAVA_SRCS = $(shell find $(JAVA_ROOT) -name "*.java")
JAVA_TEST_SRCS = $(shell find $(JAVA_TEST_ROOT) -name "*.java")
CLITEST_SRCS = $(shell find $(CLITEST_ROOT) -name "*.md")
BASH_SRCS = $(shell find $(BASH_ROOT) -name "*.sh")
CPP_SRCS = $(shell find $(CPP_ROOT) -name "*.cpp" -or -name "*.hpp")
JULIA_SRCS = $(shell find $(JULIA_ROOT) -name "*.jl")
RUST_SRCS = $(shell find $(RUST_ROOT) -name "*.rs")
SRCS = $(PY_SRCS) $(JAVA_SRCS) $(JAVA_TEST_SRCS) $(CLITEST_SRCS) $(BASH_SRCS) \
	   $(CPP_SRCS) $(JULIA_SRCS) $(RUST_SRCS) $(MAKEFILE)
JAVA_CP_LIBS = $(CLASSPATH)

# functions
msg = (if [ -t 1 ]; then echo ${BLUE}"\n$1\n"${NC}; else echo "$1"; fi)

igrep = ($(GREP) --line-number --recursive --word-regexp --color=auto \
		--ignore-case $1 $2)

day = $(shell echo $1 | $(GAWK) --field-separator=, \
		'{print "AoC"$$1"_"$$2""$2""}')

subdir = $(shell echo $1 | $(GAWK) --field-separator=, '{print $$1"/"$$2}')

to_path = $(shell echo $1 | $(GAWK) '{printf $$1; for (i = 2; i <= NF; i++) { if ($$i != "") { printf ":"$$i }; }}')

open-in-browser = \
	$(if $(findstring true,$(IS_WSL)),\
	$(shell "$(CHROME)" --new-window --start-maximized \
		$$($(WSLPATH) -aw "$1") > /dev/null 2>&1 &),\
	$(shell $(BROWSER) "$1" > /dev/null 2>&1 &)\
)

#: Default target - pre-push
.DEFAULT_GOAL := pre-push

#: Run Python (with ARGS=year,day)
py:
	@$(PYTHON_CMD) $(PYTHON_ROOT)/$(call day,$(ARGS),".py")

#: Run Java (with ARGS=year,day)
java:
	@$(JAVA_CMD) -cp $(JAVA_CP_LIBS):$(JAVA_DST) $(call day,$(ARGS),"")

#: Run Bash (with ARGS=year,day)
bash:
	@./$(BASH_ROOT)/$(call day,$(ARGS),".sh")

#: Run C++ (with ARGS=year,day)
cpp:
	@MAIN=$(call day,$(ARGS),"") $(MAKE) -s -C $(CPP_ROOT)/$(call subdir,$(ARGS))
	@./$(CPP_DST_ROOT)/$(call day,$(ARGS),"")

#: Run Julia (with ARGS=year,day)
julia:
	@$(JULIA_CMD) $(JULIA_ROOT)/$(call day,$(ARGS),".jl")

#: Run Rust (with ARGS=year,day)
rust:
	@$(CARGO_CMD) run --quiet --release --manifest-path \
		$(RUST_ROOT)/$(call day,$(ARGS),"")/Cargo.toml

#: Build Java
build.java:
	@$(JAVAC_CMD) -cp $(JAVA_CP_LIBS) -d $(JAVA_DST) $(JAVA_SRCS)
	@$(JAVAC_CMD) -cp $(JAVA_CP_LIBS):$(JAVA_DST) \
		-d $(JAVA_TEST_DST) $(JAVA_TEST_SRCS)

#: Build C++
build.cplusplus:
	@$(call msg,"Building C++")
	$(shell for f in $(find src/main/cpp/ -name "AoC*.cpp"); do rm -f "build/cpp/$(basename $f .cpp)" && MAIN=$(basename $f .cpp) make -C "$(dirname $(realpath $f))" all; done)

#: Build Rust
build.rust:
	@$(CARGO_CMD) build --release --workspace --manifest-path \
		$(RUST_ROOT)/Cargo.toml

#: Run Python unit tests
unittest.py:
	@$(call msg,"Running Python	unit tests...")
	@$(PYTHON_PATH) $(PYTHON_CMD) $(PYTHON_UNITTEST_CMD)

#: Run Java unit tests
unittest.java:
	@$(call msg,"Running Java unit tests...")
	@$(JAVA_CMD) -DNDEBUG -cp $(JAVA_CP_LIBS):$(JAVA_DST):$(JAVA_TEST_DST) \
		$(JAVA_UNITTEST_CMD) --details=summary --select-class=AllUnitTests

#: Run C++ unit tests
unittest.cplusplus:
	@$(call msg,"Running C++ unit tests...")
	@$(BAZEL) test --test_output=errors $(CPP_TEST_ROOT)/...

#: Run Rust unit tests
unittest.rust:
	@$(call msg,"Running Rust unit tests...")
	@$(CARGO_CMD) test --quiet --workspace --manifest-path \
		$(RUST_ROOT)/Cargo.toml

#: Run all unit tests
unittest: unittest.py unittest.java unittest.rust

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

#: Run shellcheck - Bash code linter
shellcheck:
	@$(call msg,"Running shellcheck against Bash source files...")
	@$(SHELLCHECK) $(BASH_SRCS)

#: Run PMD - Java static source code analyzer
pmd: $(PMD_CACHE_DIR)
	@$(PMD) -cache $(PMD_CACHE_DIR)/cache -dir $(JAVA_ROOT) -format textcolor

#: Run PMD - Java static source code analyzer (HTML report)
pmd.html: $(PMD_CACHE_DIR) $(PMD_HTML_DIR)
	-@$(PMD) -cache $(PMD_CACHE_DIR)/cache -dir $(JAVA_ROOT) -format xslt \
		-reportfile $(PMD_HTML)

#: Open PMD html report
pmd.html.open: pmd.html
	$(call open-in-browser,$(PMD_HTML))

$(PMD_CACHE_DIR):
	@$(MKDIR) --parents $(PMD_CACHE_DIR)

$(PMD_HTML_DIR):
	@$(MKDIR) --parents $(PMD_HTML_DIR)

#: Update docs
docs.update:
	@$(PYTHON_PATH) $(PYTHON_CMD) -m aoc.implementation_tables README.md

#: Run rustfmt - Rust code formating check
rustfmt.check:
	@$(call msg,"Running rustfmt check against Rust source files...")
	@$(RUSTFMT) --check --config max_width=80 $(RUST_SRCS)

#: Run all linters (Flake8, Vulture, Bandit, shellcheck, rustfmt.check)
lint: flake vulture bandit shellcheck rustfmt.check

fixme todo:
	-@$(call igrep,"$@",$(PY_SRCS) $(JAVA_SRCS) $(CPP_SRCS) $(RUST_SRCS))

#: Show FIXMEs and TODOs in code files
tasks: fixme todo

#: git pre-push hook: show tasks, linters, unit tests
pre-push: tasks lint unittest

#: Show values of some selected make variables
dump:
	@echo "JAVA_CP_LIBS: "$(JAVA_CP_LIBS)""
	@echo "JAVA_SRCS: "$(JAVA_SRCS)""
	@echo "PY_SRCS: "$(PY_SRCS)""
	@echo "SRCS: "$(SRCS)""

#: Clean up generated files
clean:
	$(RM) $(JAVA_DST) $(JAVA_TEST_DST) $(PMD_CACHE_DIR) $(PMD_HTML_DIR)

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
	@$(GREP) --before-context=1 --extended-regexp "^[.a-zA-Z0-9_-]+\:" \
			$(MAKEFILE) | $(GREP) --invert-match -- -- \
		| $(SED) 'N;s/\n/###/' \
		| $(SED) --quiet 's/^#: \(.*\)###\(.*\):.*/\2###\1/p' \
		| column -t -s '###' \
		| $(SORT)

.PHONY: flake vulture bandit fixme todo list help py java cpp julia \
	unittest.py clitest build.java clean unittest.java pmd pmd.html \
	pmd.html.open docs.update unittest.cplusplus build.cplusplus \
	build.rust rustfmt.check unittest.rust rust bash
