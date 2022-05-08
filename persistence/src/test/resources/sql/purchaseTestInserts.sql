INSERT INTO users(id, username, wallet, email, password, wallet_chain, role)
VALUES(1, 'username', 'wallet', 'email', 'password', 'Ethereum', 'role');
INSERT INTO users(id, username, wallet, email, password, wallet_chain, role)
VALUES(2, 'username2', 'wallet2', 'email2', 'password2', 'Ethereum', 'role2');

INSERT INTO images(id_image, image) VALUES(1, '');

INSERT INTO nfts(id, nft_id, contract_addr, nft_name, chain, id_image, id_owner, collection, description, properties)
VALUES(1, 1, 'contractAddr', 'nftName', 'Ethereum', 1, 1, 'Collection', 'Description', null);