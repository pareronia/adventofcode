echo off
setlocal enabledelayedexpansion
for %%v in (src\main\python\AoC2020_??.py) do (
    python -O "%%v"
    if !errorlevel! neq 0 break /b !errorlevel!
)
