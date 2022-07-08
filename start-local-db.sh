docker run --name drs_db \
  --rm \
  -p 5436:5432 \
  -e POSTGRES_USER=drs \
  -e POSTGRES_PASSWORD=xxx \
  -e POSTGRES_DB=dep_rep_serv \
  -d postgres:9.6.2-alpine
