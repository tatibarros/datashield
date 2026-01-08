-- Insert seed users
INSERT INTO users (username, password, email, role, active) VALUES
('admin', '$2a$10$slYQmyNdGzin7olVN3p5Be7DlH.PKZbv5H8KnzzVgXXbVxzy77ksm', 'admin@datashield.com', 'ADMIN', true),
('analyst', '$2a$10$xg7lX8Zm3/FzxuKP/vZ1Hu5pL1mW8nZ7bQ9nK2pM5r1vC3dE7fT5m', 'analyst@datashield.com', 'ANALYST', true),
('auditor', '$2a$10$aQ2bN3pR7sT8uV9wX1yZ2C4dE5fG6hI7jK8lM9nO0pQ1rS2tU3vW', 'auditor@datashield.com', 'AUDITOR', true);

-- Password hints (bcrypt):
-- admin: admin123
-- analyst: analyst123
-- auditor: auditor123
