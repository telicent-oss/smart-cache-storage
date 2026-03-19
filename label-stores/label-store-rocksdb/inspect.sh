#!/usr/bin/env bash
#
# Copyright (C) Telicent Ltd
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#


DB=$1
if [ -z "${DB}" ]; then
  echo "No database directory supplied"
  exit 1
fi 

rocksdb_ldb --db=${DB} --column_family=ids_to_labels --hex scan > /tmp/labels.hex

while IFS= read -r LINE; do
  ID=$(echo "${LINE}" | awk '{print $1}')
  LABEL=$(echo "${LINE}" | awk '{print $3}')
  #echo -n "${ID}: "
  printf "Label ID %d:\n" "${ID}"
  echo -n "  "
  echo "${LABEL}" | xxd -r -p
  printf "\n\n"
done < /tmp/labels.hex
