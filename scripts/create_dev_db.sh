#!/bin/bash


echo "Creating the user with username ${DEV_USER} and password **********"

psql -U "${POSTGRES_USER}" -c "CREATE USER ${DEV_USER} WITH PASSWORD '${DEV_PASSWORD}';"

echo "Creating dev database for the ${DEV_USER} with a DB name ${DEV_USER_DB_NAME}"

psql -U "${POSTGRES_USER}" -c "CREATE DATABASE ${DEV_USER_DB_NAME} WITH OWNER = ${DEV_USER};"

