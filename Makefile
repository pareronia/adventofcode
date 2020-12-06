# statics
CFG := setup.cfg
BANDIT := bandit --silent --ini $(CFG)
FLAKE := flake8
VULTURE := vulture
GREP := grep
GAWK := awk
SORT := sort
SED := sed
BLUE='\033[0;34m'
NC='\033[0m' # No Color
PY_SRCS := **/*.py

# vars
MAKEFILE = $(realpath $(lastword $(MAKEFILE_LIST)))
SRCS = $(PY_SRCS)

# functions
msg = (if [ -t 1 ]; then echo ${BLUE}"\n$1\n"${NC}; else echo "$1"; fi)

#: Default target - pre-push
.DEFAULT_GOAL := pre-push

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
	-@$(call igrep,"$@",$(SRCS))

#: Show FIXMEs and TODOs in code files
tasks: fixme todo

#: git pre-push hook: show tasks, linters
pre-push: tasks lint

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

.PHONY: flake vulture bandit fixme todo list help
