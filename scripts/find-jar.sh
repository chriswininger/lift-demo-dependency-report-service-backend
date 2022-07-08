#!/usr/bin/env bash

target_dir='/service'
base_name='dependency-report-service'

jar_name="$(ls -l $target_dir | grep -oh "\s$base_name-.*.jar$")"
jar_name="$(echo -e "${jar_name}" | tr -d '[:space:]')"

echo "$jar_name"
