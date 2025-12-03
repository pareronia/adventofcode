import shutil
from pathlib import Path
from string import Template

if __name__ == "__main__":
    from config import config  # type:ignore[missing-imports]
else:
    from .config import config


def main(args: list[str]) -> None:
    if len(args) != 3:
        msg = f"Usage: {__name__} <year> <day> <lang>"
        raise ValueError(msg)

    year = args[0]
    day = args[1]
    day2 = f"{args[1]:0>2}"
    conf = config.get_languages()[args[2]]
    for template in conf.templates:
        destination = Path(template.destination.format(year=year, day=day))
        if destination.exists():
            print(f"'{destination}' already exists")
            continue
        destination.parent.mkdir(parents=True, exist_ok=True)
        mappings = {"year": year, "day": day, "day2": day2}
        target = Path(shutil.copyfile(template.source, destination))
        with target.open("r", encoding="utf-8") as f:
            t = Template(f.read())
            s = t.substitute(mappings)
        with target.open("w", encoding="utf-8") as f:
            f.write(s)
        print(f"Generated '{target}'")


if __name__ == "__main__":
    main(["2018", "1", "python"])
    main(["2018", "1", "java"])
    main(["2018", "1", "rust"])
    main(["2025", "4", "bash"])
