#!/bin/sh
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
set -eu

if [ ! -d "venv" ]; then
  /usr/bin/env python3 -m venv venv
  venv/bin/pip3 install --progress-bar off --upgrade --require-hashes \
    -r requirements-pip.txt
fi
venv/bin/pip3 install --progress-bar off --require-hashes -r requirements.txt
venv/bin/python3 codegen.py
