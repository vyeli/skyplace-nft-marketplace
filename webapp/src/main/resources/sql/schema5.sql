-- ADD Locale field to users
ALTER TABLE users ADD COLUMN locale TEXT DEFAULT 'en';