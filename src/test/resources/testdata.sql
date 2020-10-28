DELETE FROM web_check; 
DELETE FROM monitor; 

INSERT INTO web_check (id, active, check_frequency, check_name, frequency_unit, check_url) VALUES (1, TRUE, 1, 'GoogleApi', 'M', 'http://google.com');
INSERT INTO web_check (id, active, check_frequency, check_name, frequency_unit, check_url) VALUES (2, TRUE, 1, 'ApiYahoo', 'M', 'http://yahoo.com');
INSERT INTO web_check (id, active, check_frequency, check_name, frequency_unit, check_url) VALUES (3, TRUE, 2, 'Fake', 'M', 'http://fakesite.com');

INSERT INTO monitor (id, check_id, last, response_time, since, status, down_tracker) VALUES (101, 1, '2020-05-11 19:16:42.95', 350, '2020-05-11 19:16:42.95', 'UP', 0);
INSERT INTO monitor (id, check_id, last, response_time, since, status, down_tracker) VALUES (102, 1, '2020-05-11 19:17:42.95', 320, '2020-05-11 19:16:42.95', 'UP', 0);
INSERT INTO monitor (id, check_id, last, response_time, since, status, down_tracker) VALUES (103, 1, '2020-05-11 19:18:42.95', 290, '2020-05-11 19:16:42.95', 'UP', 0);
INSERT INTO monitor (id, check_id, last, response_time, since, status, down_tracker) VALUES (104, 1, '2020-05-11 19:19:42.95', 300, '2020-05-11 19:16:42.95', 'UP', 0);
INSERT INTO monitor (id, check_id, last, response_time, since, status, down_tracker) VALUES (105, 1, '2020-05-11 19:20:42.95', 300, '2020-05-11 19:16:42.95', 'UP', 0);

INSERT INTO monitor (id, check_id, last, response_time, since, status, down_tracker) VALUES (106, 2, '2020-05-11 20:16:42.95', 300, '2020-05-11 20:16:42.95', 'UP', 0);
INSERT INTO monitor (id, check_id, last, response_time, since, status, down_tracker) VALUES (107, 2, '2020-05-11 20:17:42.95', 298, '2020-05-11 20:16:42.95', 'UP', 0);

INSERT INTO monitor (id, check_id, last, response_time, since, status, down_tracker) VALUES (108, 3, '2020-05-11 21:16:42.95', 4, '2020-05-11 21:16:42.95', 'DOWN', 0);
INSERT INTO monitor (id, check_id, last, response_time, since, status, down_tracker) VALUES (109, 3, '2020-05-11 21:17:42.95', 3, '2020-05-11 21:16:42.95', 'DOWN', 1);
INSERT INTO monitor (id, check_id, last, response_time, since, status, down_tracker) VALUES (110, 3, '2020-05-11 21:18:42.95', 5, '2020-05-11 21:16:42.95', 'DOWN', 2);

