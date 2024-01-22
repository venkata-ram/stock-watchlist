Ktor Evaluation Task.
1. Create two tables Watchlist and Recent Watchlist (Foreign key - id)

2. Create watchlist service should insert into watchlist table and return the id as the response

3. Update Recent watchlist service should insert into the recent watchlist table for that particular account Id only if is_active column is true in the watchlist table 

4. Delete watchlist service should update isActive column as false in the watchlist table and also it should look into the recent watchlist table if that id exists, then it should get replaced with the last updated watchlist id from the watchlist table. if that account id doesn't have any other watchlist, then update the recent watchlist id as null for that account id. 

Note:
1. Write Unit test cases also
2. Handle exception cases
