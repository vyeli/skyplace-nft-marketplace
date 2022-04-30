-- TABLA NFTS: agregar id_owner, collection, description, properties (string array default null)
ALTER TABLE nfts ADD COLUMN id_owner INTEGER NOT NULL default 0;
ALTER TABLE nfts ADD COLUMN collection TEXT;
ALTER TABLE nfts ADD COLUMN description TEXT;
ALTER TABLE nfts ADD COLUMN properties TEXT[];

-- SELLORDERS: sacar descripcion, seller_email
ALTER TABLE sellorders DROP COLUMN descr;
ALTER TABLE sellorders DROP COLUMN seller_email;

-- BUYORDERS: crear tabla. Campos: id_sellorder, amount, id_buyer
CREATE TABLE IF NOT EXISTS Buyorders (
    id_sellorder INT NOT NULL,
    amount NUMERIC(36, 18) NOT NULL,
    id_buyer INT NOT NULL,
    FOREIGN KEY (id_sellorder) REFERENCES sellorders(id) ON DELETE CASCADE,
    FOREIGN KEY (id_buyer) REFERENCES Users(id) ON DELETE CASCADE
);

-- USERS: agregar chain a la que pertenece la wallet, campo rol default 'user'
ALTER TABLE Users
    ADD COLUMN wallet_chain TEXT NOT NULL default 'Ethereum';
ALTER TABLE Users
    ADD FOREIGN KEY (wallet_chain) REFERENCES chains(chain);
ALTER TABLE Users
    ADD COLUMN role TEXT NOT NULL default 'User';

-- PURCHASES: agregar campo price, timestamp cuando se produjo la compra
ALTER TABLE Purchases
    ADD COLUMN price NUMERIC(36, 18) NOT NULL default 0;
ALTER TABLE Purchases
    ADD COLUMN buy_date TIMESTAMP NOT NULL default CURRENT_DATE;

-- Nfts, create id serial primary key and remove old primary key
ALTER TABLE sellorders DROP CONSTRAINT sellorders_nft_addr_id_nft_nft_chain_fkey;
ALTER TABLE sellorders DROP COLUMN nft_addr;
ALTER TABLE sellorders DROP COLUMN nft_chain;

ALTER TABLE purchases DROP CONSTRAINT purchases_nft_addr_id_nft_nft_chain_fkey;
ALTER TABLE purchases DROP COLUMN nft_addr;
ALTER TABLE purchases DROP COLUMN nft_chain;

ALTER TABLE nfts DROP CONSTRAINT nfts_pkey CASCADE;
ALTER TABLE nfts RENAME COLUMN id TO nft_id;
ALTER TABLE nfts ADD COLUMN id SERIAL;
ALTER TABLE nfts ADD PRIMARY KEY(id);

ALTER TABLE sellorders ADD FOREIGN KEY(id_nft) REFERENCES nfts(id);
ALTER TABLE purchases ADD FOREIGN KEY(id_nft) REFERENCES nfts(id);

-- Change favourites to nft
ALTER TABLE favorited DROP CONSTRAINT favorited_sellorder_id_fkey;
ALTER TABLE favorited RENAME COLUMN sellorder_id TO id_nft;
ALTER TABLE favorited ADD FOREIGN KEY(id_nft) REFERENCES nfts(id);