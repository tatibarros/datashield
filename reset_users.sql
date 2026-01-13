-- Reset users with working bcrypt hashes
-- admin: admin123
-- analyst: analyst123
-- auditor: auditor123
DELETE FROM users;

INSERT INTO users (username, password, email, role, active) VALUES
('admin', '$2a$10$BccDSBJ8pCDM.cQNpVYQp.RQEKhY8OGfSfVUPkQN1Ql9QJOmHxbxq', 'admin@datashield.com', 'ADMIN', true),
('analyst', '$2a$10$mDRoRpwUH3GEOvR0.YhUcOKDN6VUV8hITSe6Dw9Z7JvlQF3Pjp7VO', 'analyst@datashield.com', 'ANALYST', true),
('auditor', '$2a$10$ZKcLr9H2HvyYLh8KFCnvU.KxBzFZgPlhLEjScbqIxJ9EXKzE/Sd5e', 'auditor@datashield.com', 'AUDITOR', true);

SELECT * FROM users;
