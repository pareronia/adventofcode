load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

http_archive(
  name = "gtest",
  urls = ["https://github.com/google/googletest/archive/refs/tags/release-1.12.1.zip"],
  strip_prefix = "googletest-release-1.12.1",
)

# Hedron's Compile Commands Extractor for Bazel
# https://github.com/hedronvision/bazel-compile-commands-extractor
http_archive(
    name = "hedron_compile_commands",
    url = "https://github.com/hedronvision/bazel-compile-commands-extractor/archive/44503f9bc0b6b4ae4e57c3cb782c0e17b7476510.tar.gz",
    strip_prefix = "bazel-compile-commands-extractor-44503f9bc0b6b4ae4e57c3cb782c0e17b7476510",
	sha256 = "56bc111c75b5d2ee79d7fb109e613d4a744df0e11af6070a878ab12682924cec"
)
load("@hedron_compile_commands//:workspace_setup.bzl", "hedron_compile_commands_setup")
hedron_compile_commands_setup()
