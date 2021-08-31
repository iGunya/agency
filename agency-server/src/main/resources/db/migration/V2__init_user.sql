-- создание базовых пользавателей с паролями 100
INSERT INTO users (login,password,role) VALUES
('admin','$2a$10$Wk5WuwHDYHJn63.DfrzLQeZB43Lg6XR.Xs8pAMDvz7q/fiHvPO1qO','ROLE_ADMIN'),
('manager','$2a$10$Wk5WuwHDYHJn63.DfrzLQeZB43Lg6XR.Xs8pAMDvz7q/fiHvPO1qO','ROLE_MANAGER');