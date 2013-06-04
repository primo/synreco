CREATE OR REPLACE FUNCTION GET_CONTEXT_SIM (word1 VARCHAR, word2 VARCHAR, tau NUMERIC) RETURNS NUMERIC AS $$
DECLARE
    vCnt DOUBLE PRECISION;
BEGIN


	WITH QUERIES_1 AS (
		SELECT query FROM CLICK_LOG_01 
		WHERE query like '% '||word1||' %' or query like word1||' %' or query like '% '||word1 or query = word1
	), CONTEXTS_1 AS ( 
		SELECT DISTINCT c.context from (
			SELECT REPLACE(SUBSTRING( query from '\\m[a-z-]*\\M \\m'||word1||'\\M'), ' '||word1, '') as context from QUERIES_1
			UNION ALL 
			SELECT REPLACE(SUBSTRING( query from '\\m'||word1||'\\M \\m[a-z-]*\\M'), word1||' ', '') as context from QUERIES_1
		) c where c.context is not null

 	),QUERIES_2 AS (
		SELECT query FROM CLICK_LOG_01 
		WHERE query like '% '||word2||' %' or query like word2||' %' or query like '% '||word2 or query = word2
	), CONTEXTS_2 AS ( 
		SELECT DISTINCT c.context from (
			SELECT REPLACE(SUBSTRING( query from '\\m[a-z-]*\\M \\m'||word2||'\\M'), ' '||word2, '') as context from QUERIES_2
			UNION ALL 
			SELECT REPLACE(SUBSTRING( query from '\\m'||word2||'\\M \\m[a-z-]*\\M'), word2||' ', '') as context from QUERIES_2
		) c where c.context is not null

 	), int_tab as (
 		SELECT * FROM CONTEXTS_1 INTERSECT  SELECT * FROM CONTEXTS_2
 	), un_tab as (
		SELECT * FROM CONTEXTS_1 UNION  SELECT * FROM CONTEXTS_2
 	)
 	SELECT (select count(*) :: double precision  from int_tab)/  (select count(*) :: double precision  from un_tab)
    into vCnt;
    
 RETURN vCnt;
  
  
 END;

$$ LANGUAGE plpgsql;