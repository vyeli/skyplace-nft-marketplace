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

-- Add unique username
ALTER TABLE users ADD UNIQUE(username);

-- Add status buyorder and purchases
ALTER TABLE buyorders ADD COLUMN status TEXT NOT NULL DEFAULT 'NEW';
ALTER TABLE purchases ADD COLUMN status TEXT NOT NULL DEFAULT 'SUCCESS';

-- Add hash to purchases table
ALTER TABLE purchases ADD COLUMN tx TEXT;

-- Add timestamp for pending buyorders
ALTER TABLE buyorders ADD COLUMN pending_date TIMESTAMP;

-- Add soft delete to schema
ALTER TABLE nfts ADD COLUMN is_deleted BOOLEAN DEFAULT false;