DROP TABLE IF EXISTS users, items, bookings, comments, requests;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT pk_user_id PRIMARY KEY (id),
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  description VARCHAR(512) NOT NULL,
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  requestor_id BIGINT NOT NULL,
  CONSTRAINT pk_request_id PRIMARY KEY (id),
  CONSTRAINT fk_requestor_id FOREIGN KEY (requestor_id) REFERENCES users (id) ON DELETE CASCADE
 );

CREATE TABLE IF NOT EXISTS items (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(512),
  available BOOLEAN,
  owner_id BIGINT NOT NULL,
  request_id BIGINT,
  CONSTRAINT pk_item_id PRIMARY KEY (id),
  CONSTRAINT fk_owner_id FOREIGN KEY (owner_id) REFERENCES users (id),
  CONSTRAINT fk_request_id FOREIGN KEY (request_id) REFERENCES requests (id) ON DELETE SET NULL,
  CONSTRAINT UQ_NAME UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS bookings (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  end_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  item_id BIGINT NOT NULL,
  booker_id BIGINT NOT NULL,
  status VARCHAR(30) NOT NULL,
  CONSTRAINT pk_booking_id PRIMARY KEY (id),
  CONSTRAINT fk_item_id FOREIGN KEY (item_id) REFERENCES items (id),
  CONSTRAINT fk_booker_id FOREIGN KEY (booker_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS comments (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  text VARCHAR(512) NOT NULL,
  item_id BIGINT NOT NULL,
  author VARCHAR(255) NOT NULL,
  created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  CONSTRAINT pk_comment_id PRIMARY KEY (id),
  CONSTRAINT fk_item_comments_id FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE CASCADE
);
