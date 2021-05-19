#! /usr/bin/env python3
# Copyright 2021 Barend Garvelink
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

"""
Code generator for the country codes file.
"""
import os
import json
from pathlib import Path

from jinja2 import FileSystemLoader, Environment
from yaml import safe_load as yaml_load


def expand_templates(basedir: Path, context: dict):
    template_dir = basedir.joinpath("src/main/jinja2")
    target_dir = basedir.joinpath("target/generated-sources/jinja2")

    def flat_get(obj, path: str, def_val=None):
        for elem in path.split("."):
            if elem not in obj:
                return def_val if def_val is not None else environment.undefined(name=f"'{path}' (at '{elem}')")
            obj = obj[elem]
        return obj

    environment = Environment(loader=FileSystemLoader(template_dir), autoescape=False)
    environment.filters["date_time_format"] = lambda dt, pat: dt.strftime(pat)
    environment.filters["escape_java_string"] = lambda s: json.dumps(s).strip("\"")
    environment.filters["flat_get"] = flat_get

    for template in environment.list_templates():
        template_path = Path(template)
        output_path = Path(target_dir).joinpath(template_path.parent)
        output_path.mkdir(parents=True, exist_ok=True)
        output_fqfn = output_path.joinpath(template_path.with_suffix("").name)
        with open(output_fqfn, "w") as outfile:
            print(f"Render {template} to {output_fqfn}")
            print(environment.get_template(template).render(context), file=outfile)


def load_context(basedir: Path) -> dict:
    data_file = basedir.joinpath("src/main/resources/nl/garvelink/iban/IBAN.yml")
    with open(data_file, "r") as df:
        return yaml_load(df)


if __name__ == "__main__":
    basedir = Path(os.getcwd())
    context = load_context(basedir)
    expand_templates(basedir, context)
