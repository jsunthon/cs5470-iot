drop database if exists cs5470;
create database cs5470;

use cs5470;

drop table if exists searches;

create table searches (
	id int primary key not null auto_increment,
    networkType varchar(1),
    nodes int,
    features int,
    sourceId int,
    searchedFeature int,
    isSuccess boolean,
    firstBandwidth int,
    totalBandwidth int,
    latency int,
    pathLength int
);

-- The query that should generate the graph for hops versus request number --
select pathLength as hops, count(*) as searches
from searches
group by networkType, nodes, features, pathLength; 
