DROP TABLE categories, chains, nfts, purchases, reviews, sellorders, users;

CREATE TABLE IF NOT EXISTS chains (
    chain TEXT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS categories (
    category TEXT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username TEXT NOT NULL,
    wallet TEXT NOT NULL,
    mail TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS reviews (
    id SERIAL PRIMARY KEY,
    id_reviewer INT NOT NULL,
    id_reviewee INT NOT NULL,
    score INT NOT NULL,
    comments TEXT,
    FOREIGN KEY (id_reviewer) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (id_reviewee) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS nfts (
    id INT,
    contract_addr TEXT,
    nft_name TEXT,
    chain TEXT NOT NULL,
    category TEXT,
    img BYTEA NOT NULL,
    PRIMARY KEY (id, contract_addr),
    FOREIGN KEY (chain) REFERENCES chains(chain) ON DELETE CASCADE,
    FOREIGN KEY (category) REFERENCES categories(category) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS SellOrders (
    id SERIAL PRIMARY KEY,
    seller_email TEXT NOT NULL,
    descr TEXT,
    price FLOAT NOT NULL,
    id_nft INT NOT NULL,
    nft_addr TEXT NOT NULL,
    FOREIGN KEY (id_nft, nft_addr) REFERENCES nfts(id, contract_addr) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Purchases (
    id SERIAL PRIMARY KEY,
    nft_addr TEXT NOT NULL,
    id_nft INT NOT NULL,
    id_buyer INT NOT NULL,
    id_seller INT NOT NULL,
    FOREIGN KEY (id_nft, nft_addr) REFERENCES nfts(id, contract_addr) ON DELETE SET NULL,
    FOREIGN KEY (id_buyer) REFERENCES users(id) ON DELETE SET NULL,
    FOREIGN KEY (id_seller) REFERENCES users(id) ON DELETE SET NULL
);
