#!/usr/bin/env bash
#
# Copyright (C) 2024-2025 Telicent Limited
#


exec java -jar target/benchmarks.jar -rff target/label-store-benchmark-results.json -rf JSON
