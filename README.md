# NC-STOCK-PORTFOLIA
## Start Up
1. Login http://localhost:8089/h2 H2 db and create position table

```
create table tbl_position 
(
symbol varchar(255),
position_size int
)
```
2. Import position csv in resource folder

## Fuction
UploadController is import csv api

StockProducer is stock price generator

StockConsumer is listener
