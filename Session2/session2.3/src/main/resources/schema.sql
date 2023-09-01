CREATE TABLE news (
	id BIGINT NOT NULL,
	title VARCHAR(255),
	details VARCHAR(1000),
	tags VARCHAR(255),
    reported_Time DATETIME
);

ALTER TABLE news ADD CONSTRAINT news_pk PRIMARY KEY (id);