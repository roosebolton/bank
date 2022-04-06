USE piratebanktest;

INSERT INTO `user`
VALUES (1, 'nizar@nizar.com', 'password'),
       (2, 'tjardy@tjardy.com', 'qwerty');

INSERT INTO `asset`
VALUES  ('Bitcoin', 'BTC'),
        ('Ethereum', 'ETH'),
        ('Litecoin', 'LTC');


INSERT INTO `customer`
VALUES (1, 'Nizar', '', 'Kerdanian', '1990-01-01', 99999999, '1666OK', '3', '', 'XDStreet', 'HP', 'HDHD');

INSERT INTO `asset_rates`
VALUES  ('Bitcoin', '2022-03-10 02:00:00', '1146.52248301'),
        ('Bitcoin', '2022-03-09 02:00:00', '1116.52248301'),
        ('Bitcoin', '2022-03-03 02:00:00', '1029.08964093'),
        ('Bitcoin', '2022-02-10 02:00:00', '1133.76985475'),
        ('Bitcoin', '2021-12-10 02:00:00', '1172.76990000'),
        ('Ethereum', '2022-03-10 02:00:00', '47.08662654'),
        ('Ethereum', '2022-03-09 02:00:00', '41.00237094'),
        ('Ethereum', '2022-03-03 02:00:00', '37.12571377'),
        ('Ethereum', '2022-02-10 02:00:00', '30.02858584'),
        ('Ethereum', '2021-12-10 02:00:00', '31.86889930');

INSERT INTO `configdata`
VALUES (1, 0.075, 5000000, 1000)




