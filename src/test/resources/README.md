## Resources Directory

This directory is designed to hold all of the static data for your test cases in CSV files, as well as the associated queries to pull that data from your remote system stored in SQL files.

The queries should be reproducible independent of time - this means you should include a timespan for your data.

The data involved in the CSV should be limited to a certain number of rows in order to avoid adding too much size to your project and Git repository.
Therefore your queries should also have a row count limit.

Your CSV files should go into the `source_data` directory, whereas the SQL query should go into the `source_queries` directory.

When you add a new source table to your setup you need to add it to the `dfNames.txt` file in the format `schema.table_name\n`.
