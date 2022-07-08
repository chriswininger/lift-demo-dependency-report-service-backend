#!/usr/bin/env bash

# ./wait_for_db.sh host port db_name db_user password

DB_HOST=$1
DB_PORT=$2
DB_NAME=$3
DB_USER=$4
DB_PASSWORD=$5


WAIT_FOR_DB_NUM_RETRIES=10

echo "wait_for_db_num_retries: $WAIT_FOR_DB_NUM_RETRIES"
echo "DB_HOST: $DB_HOST, DB_PORT: $DB_PORT, DB_NAME: $DB_NAME, DB_USER: $DB_USER"

connect_retries=$WAIT_FOR_DB_NUM_RETRIES
durration_for_wait=5s

until PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" \
  -p "$DB_PORT" \
  -d "$DB_NAME" \
  -U "$DB_USER" \
  -c "select 1" > /dev/null 2>&1 || [ $DB_PASSWORD -eq 0 ]; do

  echo "Waiting for db $DB_NAME -- Max Retries: $CONNECT_RETRIES"
  connect_retries=$((connect_retries-=1))

  sleep $durration_for_wait

  if [[ $connect_retries -eq 0 ]]
  then
    echo "timed out waiting on the database"
    exit 1
  fi
done
