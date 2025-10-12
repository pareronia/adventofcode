# statics

bash_source_dir := "src/main/bash"
bash_srcs := "import pathlib; print(' '.join(map(str, list(pathlib.Path('src/main/bash').rglob('*.sh')))))"
cpp_dst := "build/cpp"
cpp_source_dir := "src/main/cpp"
cpp_srcs := "import pathlib; print(' '.join(map(str, list(pathlib.Path('src/main/cpp').rglob('*.[ch]pp')))))"
cpp_test_dir := "src/test/cpp"
java_agent := env("JAVA_AGENT")
java_cp_libs := env("CLASSPATH")
java_dst := "build/java/classes"
java_source_dir := "src/main/java"
java_srcs := "import pathlib; print(' '.join(map(str, list(pathlib.Path('src/main/java').rglob('*.java')))))"
java_test_dst := "build/java/test-classes"
java_test_srcs := "import pathlib; print(' '.join(map(str, list(pathlib.Path('src/test/java').rglob('*.java')))))"
java_unittest_cmd := "org.junit.platform.console.ConsoleLauncher"
julia_source_dir := "src/main/julia"
pmd_cache_dir := ".cache/pmd"
pmd_html_dir := "htmlpmd"
rust_source_dir := "src/main/rust"
rust_srcs := "import pathlib; print(' '.join(map(str, list(pathlib.Path('src/main/rust').rglob('*.rs')))))"
rust_dst := "build/rs"
source_dir := "src/main/python"
srcs := "import pathlib; print(' '.join(map(str, list(pathlib.Path('src/main/python').rglob('*.py')))))"
test_source_dir := "src/test/python"

# commands

bazel := "bazel"
cargo := "RUSTFLAGS='-C target-cpu=native' cargo"
grep := "grep"
java := env("JAVA_HOME") + "/bin/java -ea"
javac := env("JAVA_HOME") + "/bin/javac -encoding utf-8 -proc:full"
julia := "julia --optimize"
make := "make"
mkdir := "mkdir"
mypy := if os_family() == "windows" { "uvx mypy --python-executable='.venv\\Scripts\\python'" } else { "uvx mypy --python-executable='.venv/bin/python'" }
pmd := "pmd"
python := "uv run python -O"
python_debug := "uv run python"
rm := "rm -Rf"
ruff := "uvx ruff"
rustfmt := "rustfmt"
shellcheck := "shellcheck"
vulture := "uvx vulture"

# env vars

export PYTHONPATH := "src/main/python"

default:
    @just --choose

[private]
msg-blue msg:
    @echo {{ BLUE }}\\n{{ msg }}\\n{{ NORMAL }}

[group("vim")]
vim-file-run-dev file *type:
    #!/usr/bin/env sh
    clear
    case "{{ type }}" in
        "python")
            LOGLEVEL="DEBUG" {{ python_debug }} "{{ file }}"
            ;;
        "sh")
            DEBUG=1 "{{ file }}"
            ;;
        "julia")
            JULIA_DEBUG="{{ file_stem(file) }}" {{ julia }} "{{ file }}"
            ;; 
        "cpp")
            MAIN="{{ file_stem(file) }}" make --silent --directory "{{ parent_directory(file) }}" all || exit $?
            clear; "{{ cpp_dst }}/{{ file_stem(file) }}"
            ;;
        "rust")
            {{ cargo }} run --manifest-path "{{ parent_directory(parent_directory(file)) }}/Cargo.toml" --target-dir "{{ rust_dst }}"
            ;;
    esac

[group("vim")]
vim-file-run file *type:
    #!/usr/bin/env sh
    clear
    case "{{ type }}" in
        "python")
            {{ python }} "{{ file }}"
            ;;
        "sh")
            "{{ file }}"
            ;;
        "julia")
            {{ julia }} "{{ file }}"
            ;; 
        "cpp")
            MAIN="{{ file_stem(file) }}" make --silent --directory "{{ parent_directory(file) }}" all || exit $?
            clear; NDEBUG= "{{ cpp_dst }}/{{ file_stem(file) }}"
            ;;
        "rust")
            {{ cargo }} run --manifest-path "{{ parent_directory(parent_directory(file)) }}/Cargo.toml" --release --target-dir "{{ rust_dst }}"
            ;;
    esac

[group("vim")]
vim-file-debug file *type:
    #!/usr/bin/env sh
    clear
    case "{{ type }}" in
        "python")
            {{ python_debug }} -m pdb "{{ file }}"
            ;;
        "rust")
            {{ cargo }} test --manifest-path "{{ parent_directory(parent_directory(file)) }}/Cargo.toml" --target-dir "{{ rust_dst }}"
            ;;
    esac

# Linting: ruff check
[group("linting")]
ruff-check: (msg-blue "Running ruff check")
    @{{ ruff }} --quiet check "{{ source_dir }}"

# Linting: vulture - unused code
[group("linting")]
vulture: (msg-blue "Running vulture")
    @{{ vulture }} "{{ source_dir }}"

# Linting: ruff format check
[group("linting")]
ruff-format-check: (msg-blue "Running ruff format check")
    @{{ ruff }} format --quiet --check "{{ source_dir }}"

# Linting: mypy
[group("linting")]
mypy: (msg-blue "Running mypy")
    @{{ mypy }} --no-error-summary {{ source_dir }}

# Linting: shellcheck
[group("linting")]
shellcheck: (msg-blue "Running shellcheck against bash source files...")
    @{{ shellcheck }} $({{ python }} -c "{{ bash_srcs }}")

[group("linting")]
[private]
pmd-cache-dir:
    @{{ mkdir }} --parents {{ pmd_cache_dir }}

[group("linting")]
[private]
pmd-html-dir:
    @{{ mkdir }} --parents {{ pmd_html_dir }}

# Linting: PMD
[group("linting")]
pmd: pmd-cache-dir
    @{{ pmd }} check --rulesets "pmd-ruleset.xml" --cache "{{ pmd_cache_dir }}/cache" \
        --dir "{{ java_source_dir }}" --format textcolor

# Linting: PMD (HTML report)
[group("linting")]
pmd-html: pmd-cache-dir pmd-html-dir
    @{{ pmd }} check --rulesets "rulesets/java/quickstart.xml" --cache "{{ pmd_cache_dir }}/cache" \
        --dir "{{ java_source_dir }}" --format xslt --report-file "{{ pmd_html_dir }}/pmd.html"

# Linting: rustfmt check
[group("linting")]
rustfmt-check: (msg-blue "Running rustfmt check against rust source files...")
    @{{ rustfmt }} --check $({{ python }} -c "{{ rust_srcs }}")

# Linting: all
[group("linting")]
lint: vulture shellcheck rustfmt-check ruff-check

# Run python
[group("python")]
py year day:
    @{{ python }} $({{ python }} -c 'year={{ year }};day={{ day }};print(f"{{ source_dir }}/AoC{year}_{day:0>2}.py")')

# Run java
[group("java")]
java year day:
    @{{ java }} -cp "{{ java_cp_libs }}:{{ java_dst }}" \
            $({{ python }} -c 'year={{ year }};day={{ day }};print(f"AoC{year}_{day:0>2}")')

# Run bash
[group("bash")]
bash year day:
    @./$({{ python }} -c 'year={{ year }};day={{ day }};print(f"{{ bash_source_dir }}/AoC{year}_{day:0>2}.sh")')

# Run c++
[group("c++")]
cpp year day:
    @MAIN=$({{ python }} -c 'year={{ year }};day={{ day }};print(f"AoC{year}_{day:0>2}")') \
            {{ make }} --silent --directory $({{ python }} -c 'year={{ year }};day={{ day }};print(f"{{ cpp_source_dir }}/{year}/{day:0>2}")')
    @./$({{ python }} -c 'year={{ year }};day={{ day }};print(f"{{ cpp_dst }}/AoC{year}_{day:0>2}")')

# Run julia
[group("julia")]
julia year day:
    @{{ julia }} $({{ python }} -c 'year={{ year }};day={{ day }};print(f"{{ julia_source_dir }}/AoC{year}_{day:0>2}.jl")')

# Run rust
[group("rust")]
rust year day:
    @{{ cargo }} run --quiet --release --manifest-path \
        $({{ python }} -c 'year={{ year }};day={{ day }};print(f"{{ rust_source_dir }}/AoC{year}_{day:0>2}/Cargo.toml")') \
        --target-dir "{{ rust_dst }}"

# Build java
[group("java")]
build-java:
    @{{ javac }} -cp "{{ java_cp_libs }}" -d "{{ java_dst }}" $({{ python }} -c "{{ java_srcs }}")
    @{{ javac }} -cp "{{ java_cp_libs }}:{{ java_dst }}" -d "{{ java_test_dst }}" $({{ python }} -c "{{ java_test_srcs }}")

# Build c++
[group("c++")]
build-cpp: clean-cpp
    #!/usr/bin/env sh
    for f in $(find "{{ cpp_source_dir }}" -name "AoC*.cpp"); do
        MAIN="$(basename $f .cpp)" make --directory "$(dirname $(realpath $f))" all
    done

# Build rust
[group("rust")]
build-rust:
    @{{ cargo }} build --release --workspace --manifest-path "{{ rust_source_dir }}/Cargo.toml" --target-dir "{{ rust_dst }}"

# Run java unit tests
[group("java")]
unittest-java: (msg-blue "Running java unit tests...")
    @{{ java }} -javaagent:{{ java_agent }} -DNDEBUG \
            -cp "{{ java_cp_libs }}:{{ java_dst }}:{{ java_test_dst }}" \
            {{ java_unittest_cmd }} --details=summary --select-class=AllUnitTests

# Run python unit tests
[group("python")]
unittest-py: (msg-blue "Running python unit tests...")
    @{{ python }} -m unittest discover -s "{{ test_source_dir }}"

# Run c++ unit tests
[group("c++")]
unittest-cplusplus: (msg-blue "Running c++ unit tests...")
    @{{ bazel }} test --test_output=errors "{{ cpp_test_dir }}/..."

# Run rust unit tests
[group("rust")]
unittest-rust: (msg-blue "Running rust unit tests...")
    @{{ cargo }} test --quiet --workspace --manifest-path "{{ rust_source_dir }}/Cargo.toml" --target-dir "{{ rust_dst }}"

# Run all unit tests
unittest: unittest-py unittest-java unittest-rust

[private]
igrep tag:
    @{{ grep }} --line-number --recursive --word-regexp --color=auto --ignore-case "{{ tag }}" \
        $({{ python }} -c "{{ srcs }}") \
        $({{ python }} -c "{{ java_srcs }}") \
        $({{ python }} -c "{{ cpp_srcs }}") \
        $({{ python }} -c "{{ rust_srcs }}") \
        $({{ python }} -c "{{ bash_srcs }}")

# Show FIXMEs and TODOs in code files
tasks: (igrep "todo") (igrep "fixme")

# Clean
clean: clean-rust clean-java clean-cpp
    @{{ rm }} "{{ pmd_cache_dir }}" "{{ pmd_html_dir }}"

# Clean java
[group("java")]
clean-java:
    @{{ rm }} "{{ java_dst }}" "{{ java_test_dst }}"

# Clean c++
[group("c++")]
clean-cpp:
    @{{ rm }} "{{ cpp_dst }}"

# Clean rust
[group("rust")]
clean-rust:
    @{{ cargo }} clean --manifest-path "{{ rust_source_dir }}/Cargo.toml" --target-dir "{{ rust_dst }}"

# Stats
[group("admin")]
stats *year:
    @{{ python }} -m aoc.stats {{ year }}

# Update implementation tables
[group("admin")]
table:
    @{{ python }} -m aoc.implementation_tables README.md

# Generate from template
[group("admin")]
generate year day lang:
    @{{ python }} -m aoc.generator {{ year }} {{ day }} {{ lang }}

pre-commit: tasks lint unittest-py unittest-java
