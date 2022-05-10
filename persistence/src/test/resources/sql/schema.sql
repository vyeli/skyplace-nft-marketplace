CREATE TABLE IF NOT EXISTS chains (
    chain TEXT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS categories (
    category TEXT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS images (
    id_image SERIAL PRIMARY KEY,
    image    BYTEA NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username TEXT NOT NULL,
    wallet TEXT NOT NULL,
    email TEXT NOT NULL,
    password TEXT NOT NULL,
    wallet_chain TEXT NOT NULL default 'Ethereum',
    role TEXT NOT NULL default 'User',
    UNIQUE(email),
    FOREIGN KEY (wallet_chain) REFERENCES chains(chain)
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
    id SERIAL,
    nft_id INT NOT NULL,
    contract_addr TEXT,
    nft_name TEXT,
    chain TEXT NOT NULL,
    id_image INT NOT NULL,
    id_owner INT NOT NULL,
    collection TEXT,
    description TEXT,
    properties TEXT ARRAY,
    PRIMARY KEY (id),
    FOREIGN KEY (id_image) REFERENCES images (id_image),
    FOREIGN KEY (chain) REFERENCES chains(chain) ON DELETE CASCADE,
    FOREIGN KEY (id_owner) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS SellOrders (
    id SERIAL PRIMARY KEY,
    price NUMERIC(36, 18) NOT NULL,
    id_nft INT NOT NULL,
    category TEXT,
    FOREIGN KEY(id_nft) REFERENCES nfts(id),
    FOREIGN KEY(category) REFERENCES categories(category),
    UNIQUE(id_nft)
);

CREATE TABLE IF NOT EXISTS Purchases (
    id SERIAL PRIMARY KEY,
    id_nft INT NOT NULL,
    id_buyer INT NOT NULL,
    id_seller INT NOT NULL,
    price NUMERIC(36, 18) NOT NULL default 0,
    buy_date TIMESTAMP NOT NULL default CURRENT_DATE,
    FOREIGN KEY (id_buyer) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (id_seller) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY(id_nft) REFERENCES nfts(id)
);

INSERT INTO categories VALUES('Collectible');
INSERT INTO categories VALUES('Art');
INSERT INTO categories VALUES('Utility');
INSERT INTO categories VALUES('Photography');
INSERT INTO categories VALUES('Other');

INSERT INTO chains VALUES('Ethereum');
INSERT INTO chains VALUES('Rinkeby');
INSERT INTO chains VALUES('Kovan');
INSERT INTO chains VALUES('BSC');


CREATE TABLE IF NOT EXISTS Favorited (
    user_id INT NOT NULL,
    id_nft INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY(id_nft) REFERENCES nfts(id),
    UNIQUE(user_id, id_nft)
);

CREATE TABLE IF NOT EXISTS Buyorders (
    id_sellorder INT NOT NULL,
    amount NUMERIC(36, 18) NOT NULL,
    id_buyer INT NOT NULL,
    FOREIGN KEY (id_sellorder) REFERENCES sellorders(id) ON DELETE CASCADE,
    FOREIGN KEY (id_buyer) REFERENCES Users(id) ON DELETE CASCADE,
    UNIQUE(id_sellorder, id_buyer)
);