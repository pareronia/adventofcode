import os
import shutil
from string import Template

if __name__ == "__main__":
    from config import config  # type:ignore
else:
    from .config import config


def main(args: list[str]) -> None:
    if len(args) != 3:
        raise ValueError(f"Usage: {__name__} <year> <day> <lang>")

    year = args[0]
    day = args[1]
    day2 = f"{args[1]:0>2}"
    conf = config.get_languages()[args[2]]
    destination = os.path.join(
        conf.base_dir, conf.pattern.format(year=year, day=day) + conf.ext
    )
    if os.path.exists(destination):
        print(f"'{destination}' already exists")
        return
    mappings = {"year": year, "day": day, "day2": day2}
    target = shutil.copyfile(conf.template, destination)
    with open(target, "r", encoding="utf-8") as f:
        t = Template(f.read())
        s = t.substitute(mappings)
    with open(target, "w", encoding="utf-8") as f:
        f.write(s)
    print(f"Generated '{target}'")
    return


if __name__ == "__main__":
    main(["2018", "1", "java"])
