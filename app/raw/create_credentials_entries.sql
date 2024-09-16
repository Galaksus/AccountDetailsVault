CREATE TABLE credentials (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    category_id INTEGER,
	email TEXT,
    username TEXT,
    password TEXT NOT NULL,
    additional_information TEXT,
    FOREIGN KEY (category_id) REFERENCES categories(id),
    CHECK (email IS NOT NULL OR username IS NOT NULL)
);
