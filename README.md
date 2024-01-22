Ktor Evaluation Task.
1. Create two tables Watchlist and Recent Watchlist (Foreign key - id)

2. Create watchlist service should insert into watchlist table and return the id as the response

3. Update Recent watchlist service should insert into the recent watchlist table for that particular account Id only if is_active column is true in the watchlist table 

4. Delete watchlist service should update isActive column as false in the watchlist table and also it should look into the recent watchlist table if that id exists, then it should get replaced with the last updated watchlist id from the watchlist table. if that account id doesn't have any other watchlist, then update the recent watchlist id as null for that account id. 

Note:
1. Write Unit test cases also
2. Handle exception cases


API END POINTS:
1. whatchlist/insert - to insert a watchlist

   request -> {"accountId": 1000, "stockSymbol": "MSFT", "isActive": true}

2. watchlist/delete/{id} - to delete a watchlist by id
  
3. watchlist/ -> to get all watchlist

4. watchlist/recent ->  to get all recent watchlist

5. watchlist/recent_table -> Get all recent watchlists with null values in the foreign key
