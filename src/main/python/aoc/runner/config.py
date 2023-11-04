import os
import re
import yaml
import logging
from typing import Any


class Config:
    @property
    def default_timeout(self) -> Any:
        return self.runner["default_timeout"]  # type:ignore[attr-defined]

    @property
    def py(self) -> Any:
        return self.runner["plugins"]["py"]  # type:ignore[attr-defined]

    @property
    def java(self) -> Any:
        return self.runner["plugins"]["java"]  # type:ignore[attr-defined]

    @property
    def bash(self) -> Any:
        return self.runner["plugins"]["bash"]  # type:ignore[attr-defined]

    @property
    def cpp(self) -> Any:
        return self.runner["plugins"]["cpp"]  # type:ignore[attr-defined]

    @property
    def julia(self) -> Any:
        return self.runner["plugins"]["julia"]  # type:ignore[attr-defined]

    @property
    def rust(self) -> Any:
        return self.runner["plugins"]["rust"]  # type:ignore[attr-defined]

    @property
    def scratch_file(self) -> Any:
        return self.runner["scratch_file"]  # type:ignore[attr-defined]

    @property
    def root(self) -> Any:
        return self.runner["root"]  # type:ignore[attr-defined]


def path_constructor(
    _loader: yaml.Loader | yaml.FullLoader | yaml.UnsafeLoader, node: yaml.Node
) -> Any:
    return os.path.expandvars(node.value)


log = logging.getLogger(__name__)

# https://stackoverflow.com/q/52412297
path_matcher = re.compile(r".*\$\{([^}^{]+)\}.*")
yaml.add_implicit_resolver("!path", path_matcher)
yaml.add_constructor("!path", path_constructor)
with open(os.path.join(".", "setup.yml"), "r") as f:
    setup_yaml = f.read()
config = yaml.load(  # nosec
    "!!python/object:" + __name__ + ".Config\n" + setup_yaml,
    Loader=yaml.Loader,
)
log.debug(config.__dict__)


if __name__ == "__main__":
    print(config.runner)
