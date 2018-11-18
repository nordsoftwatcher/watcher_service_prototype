cd db
call mvn -Pdb-migration -Ddb.url=jdbc:postgresql://localhost:5432/siwatch -Ddb.user=postgres -Ddb.password=12345 liquibase:update
cd ..