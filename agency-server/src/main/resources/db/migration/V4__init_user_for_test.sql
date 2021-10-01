INSERT INTO users (login,password,role) VALUES
('testAdmin','$2a$10$Wk5WuwHDYHJn63.DfrzLQeZB43Lg6XR.Xs8pAMDvz7q/fiHvPO1qO','ROLE_ADMIN'),
('testManager','$2a$10$Wk5WuwHDYHJn63.DfrzLQeZB43Lg6XR.Xs8pAMDvz7q/fiHvPO1qO','ROLE_MANAGER'),
('testUser','$2a$10$Wk5WuwHDYHJn63.DfrzLQeZB43Lg6XR.Xs8pAMDvz7q/fiHvPO1qO','ROLE_USER');
INSERT INTO users (id_user,login,password,role) VALUES
(100,'testModifyUser','$2a$10$Wk5WuwHDYHJn63.DfrzLQeZB43Lg6XR.Xs8pAMDvz7q/fiHvPO1qO','ROLE_USER')