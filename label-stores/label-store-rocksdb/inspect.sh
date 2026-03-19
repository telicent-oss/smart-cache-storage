#!/usr/bin/env bash
#
# Copyright (C) 2024-2025 Telicent Limited
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
