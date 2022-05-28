-- Delete properties column in nfts
ALTER TABLE nfts DROP COLUMN properties;
-- Drop chains and categories tables
ALTER TABLE nfts DROP CONSTRAINT nfts_chain_fkey;
ALTER TABLE users DROP CONSTRAINT users_wallet_chain_fkey;
ALTER TABLE sellorders DROP CONSTRAINT sellorders_category_fkey;
DROP TABLE chains;
DROP TABLE categories;