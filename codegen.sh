#!/bin/sh
set -eu

if [ ! -d "venv" ]; then
  /usr/bin/env python3 -m venv venv
fi
venv/bin/pip3 install --progress-bar off --require-hashes -r requirements.txt
venv/bin/python codegen.py
