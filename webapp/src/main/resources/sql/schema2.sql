-- Update users
ALTER TABLE users ADD COLUMN password TEXT NOT NULL;
ALTER TABLE users RENAME COLUMN mail to email;
ALTER TABLE users ADD CONSTRAINT uniqueMail UNIQUE(email);

-- Update sell orders and nfts
ALTER TABLE sellorders ADD COLUMN category TEXT;
ALTER TABLE sellorders ADD CONSTRAINT sellerFK FOREIGN KEY(seller_email) REFERENCES users(email) ON DELETE CASCADE;
ALTER TABLE sellorders ALTER COLUMN price TYPE NUMERIC(36, 18);

UPDATE sellorders SET category =
                          (SELECT category FROM nfts WHERE nfts.id = sellorders.id_nft AND nfts.contract_addr = sellorders.nft_addr);

ALTER TABLE nfts DROP COLUMN category;
ALTER TABLE sellorders DROP CONSTRAINT sellorders_id_nft_nft_addr_fkey;
ALTER TABLE purchases DROP CONSTRAINT purchases_id_nft_nft_addr_fkey;
ALTER TABLE nfts DROP CONSTRAINT nfts_pkey;
ALTER TABLE nfts ADD PRIMARY KEY(id, chain, contract_addr);

ALTER TABLE sellorders ADD COLUMN nft_chain TEXT NOT NULL default 'none';
UPDATE sellorders SET nft_chain =
                          (SELECT chain FROM nfts WHERE nfts.id = sellorders.id_nft AND nfts.contract_addr = sellorders.nft_addr);
ALTER TABLE sellorders ADD FOREIGN KEY(nft_addr, id_nft, nft_chain) REFERENCES nfts(contract_addr, id, chain);

ALTER TABLE purchases ADD COLUMN nft_chain TEXT NOT NULL default 'none';
UPDATE purchases SET nft_chain =
                         (SELECT chain FROM nfts WHERE nfts.id = purchases.id_nft AND nfts.contract_addr = purchases.nft_addr);
ALTER TABLE purchases ADD FOREIGN KEY(nft_addr, id_nft, nft_chain) REFERENCES nfts(contract_addr, id, chain);

-- Update category name

INSERT INTO categories VALUES('Collectible');
UPDATE sellorders SET category = 'Collectible' WHERE category = 'Collections';
DELETE FROM categories WHERE category = 'Collections';
