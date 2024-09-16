CREATE TABLE credentials (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    category_name TEXT,
	email TEXT,
    username TEXT,
    password TEXT NOT NULL,
    FOREIGN KEY (category_name) REFERENCES categories(name),
    CHECK (email IS NOT NULL OR username IS NOT NULL)
);
