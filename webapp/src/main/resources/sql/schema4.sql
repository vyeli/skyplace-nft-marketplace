-- Delete properties column in nfts
ALTER TABLE nfts DROP COLUMN properties;
-- Drop chains and categories tables
ALTER TABLE nfts DROP CONSTRAINT nfts_chain_fkey;
ALTER TABLE users DROP CONSTRAINT users_wallet_chain_fkey;
ALTER TABLE sellorders DROP CONSTRAINT sellorders_category_fkey;
DROP TABLE chains;
DROP TABLE categories;
-- Add title field to reviews and unique constraint on reviewer, reviewee
ALTER TABLE reviews ADD title TEXT;
ALTER TABLE reviews ADD CONSTRAINT unique_reviewer_reviewee UNIQUE (id_reviewer, id_reviewee);