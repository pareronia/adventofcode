import os
import re
import yaml
import logging


class Config:
    @property
    def default_timeout(self):
        return self.runner['default_timeout']

    @property
    def py(self):
        return self.runner['plugins']['py']

    @property
    def java(self):
        return self.runner['plugins']['java']

    @property
    def bash(self):
        return self.runner['plugins']['bash']

    @property
    def cpp(self):
        return self.runner['plugins']['cpp']

    @property
    def julia(self):
        return self.runner['plugins']['julia']

    @property
    def rust(self):
        return self.runner['plugins']['rust']

    @property
    def scratch_file(self):
        return self.runner['scratch_file']

    @property
    def root(self):
        return self.runner['root']


def path_constructor(_loader, node):
    return os.path.expandvars(node.value)


log = logging.getLogger(__name__)

# https://stackoverflow.com/q/52412297
path_matcher = re.compile(r'.*\$\{([^}^{]+)\}.*')
yaml.add_implicit_resolver('!path', path_matcher)
yaml.add_constructor('!path', path_constructor)
with open(os.path.join('.', 'setup.yml'), 'r') as f:
    setup_yaml = f.read()
config = yaml.load(  # nosec
    "!!python/object:" + __name__ + ".Config\n" + setup_yaml,
    Loader=yaml.Loader)
log.debug(config.__dict__)


if __name__ == "__main__":
    print(config.runner)
