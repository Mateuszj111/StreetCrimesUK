#UK Street Crimes & Outcomes data analytics

### Architecture

layout:
- analytics - Spark Engine
- mongodb - refined data's database
- api - REST API for data access

Following solution contains 3 services orchestrated by Docker Compose:
- mongodb database storing data in a following manner:
    - database admin, collections:
        - final, which presents refined format of input data,
        - crimesShare, which presents percentage % of possible crime types,
        - crimesByOutcome, which presents a pivot of crime types vs outcome types,
        - crimesByLocation, which presents counts o possible crime types within possible districts


- Apache Spark engine

<b>
To have data processed it must be located in analytics/src/main/resources/dataset.zip by user.
File needs to have .zip extension and be named dataset.zip
</b>

Main table is calculated in a following manner:
1. Outcomes data is loaded from CSV directory and appropriate columns are selected.
2. Outcomes data is ranked over crimeID by month descending. 
3. For each outcomes' crimeID records with rank == 1 are selected (might be > 1), therefore data is grouped by
crimeID and set of outcome types is collected.
   
4. Street data is loaded from CSV directory and appropriate columns are selected.
5. districtName columns in calculated from input_file_name.
   
6. Auxiliary table called lastOutcomeFromStreetDataframe is calculated - determining set of last outcomes
   per each crimeID.
7. Street data is left joined with 3. and with 6. In case of outcomeType from 3. being null, last outcome from 6. is taken.
8. KPI tables are calculated.
9. Data is written to the mongodb database.
10. Index on admin.final.crimeID is created.


- REST API

Supplied REST API contains 2 routes:
- Crimes (crime_id path param)
- KPIs

Example of crime data retrieval:
```bash
curl -X 'GET' \
  'http://localhost:8000/crimes/98096d1a69205691a56b89c1182eadd6aaf15400ea18da134e0023f20aba5cdb' \
  -H 'accept: application/json'
  
[
  {
    "crimeID": "98096d1a69205691a56b89c1182eadd6aaf15400ea18da134e0023f20aba5cdb",
    "latitude": 51.409456,
    "longitude": -2.513308,
    "districtName": "avon-and-somerset",
    "crimeType": "Criminal damage and arson",
    "outcomeType": [
      "Status update unavailable"
    ]
  }
]

```

### Architecture

To run:

```bash
docker compose up --build
```