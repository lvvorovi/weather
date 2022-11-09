
drop table IF EXISTS weather;

create TABLE weather (
    id VARCHAR(36) NOT NULL,
    latitude FLOAT NOT NULL,
    longitude FLOAT NOT NULL,
    altitude INTEGER,
    temperature FLOAT NOT NULL,
    time_stamp datetime NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT uc_lat_lon_alt_time_stamp UNIQUE (latitude, longitude, altitude, time_stamp)
);

insert into weather values ('1', 10, 10, 10, 10, '2022-11-04T05:00');
insert into weather values ('2', 10, 10, 10, 10, '2022-11-04T06:00');
insert into weather values ('3', 10, 10, 10, 10, '2022-11-04T07:00');
insert into weather values ('4', 10, 10, 10, 10, '2022-11-04T08:00');
insert into weather values ('5', 10, 10, 10, 10, '2022-11-04T09:00');
insert into weather values ('6', 10, 10, 10, 10, '2022-11-04T10:00');
insert into weather values ('7', 10, 10, 10, 10, '2022-11-04T11:00');
insert into weather values ('8', 10, 10, 10, 10, '2022-11-04T12:00');
insert into weather values ('9', 10, 10, 10, 10, '2022-11-04T13:00');
insert into weather values ('10', 10, 10, 10, 10, '2022-11-04T14:00');
