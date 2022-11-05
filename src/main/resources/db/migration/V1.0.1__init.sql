
DROP TABLE IF EXISTS weather;

CREATE TABLE weather (
    id VARCHAR(36) NOT NULL,
    latitude FLOAT NOT NULL,
    longitude FLOAT NOT NULL,
    altitude INTEGER,
    temperature FLOAT NOT NULL,
    time_stamp datetime NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT uc_lat_lon_alt_time_stamp UNIQUE (latitude, longitude, altitude, time_stamp)
);

INSERT INTO weather VALUES ('1', 10, 10, 10, 10, '2022-11-04T05:00');
INSERT INTO weather VALUES ('2', 10, 10, 10, 10, '2022-11-04T06:00');
INSERT INTO weather VALUES ('3', 10, 10, 10, 10, '2022-11-04T07:00');
INSERT INTO weather VALUES ('4', 10, 10, 10, 10, '2022-11-04T08:00');
INSERT INTO weather VALUES ('5', 10, 10, 10, 10, '2022-11-04T09:00');
INSERT INTO weather VALUES ('6', 10, 10, 10, 10, '2022-11-04T10:00');
INSERT INTO weather VALUES ('7', 10, 10, 10, 10, '2022-11-04T11:00');
INSERT INTO weather VALUES ('8', 10, 10, 10, 10, '2022-11-04T12:00');
INSERT INTO weather VALUES ('9', 10, 10, 10, 10, '2022-11-04T13:00');
INSERT INTO weather VALUES ('10', 10, 10, 10, 10, '2022-11-04T14:00');
INSERT INTO weather VALUES ('11', 10, 10, 10, 10, '2022-11-04T15:00');

