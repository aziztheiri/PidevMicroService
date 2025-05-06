👤 User Management Module
The User Management module is a core component of the Insurance Management System, ensuring secure and efficient handling of user-related operations. It includes:

🔐 Authentication & Security
JWT-based Authentication: Secures all endpoints using JSON Web Tokens.

Role-Based Access Control (RBAC): Differentiates between Admins, Agents, and Clients with appropriate access levels.

Password Encryption: All user credentials are securely hashed using BCrypt.

Spring Security Integration: Ensures robust protection against unauthorized access.

🔄 CRUD Operations
Create: Register new users via API or through the Angular UI.

Read: Fetch user details, with filters for roles and account status.

Update: Modify user profiles, including role changes and password updates.

Delete: Soft-delete or deactivate users to preserve data integrity.

🧠 Additional Features
Account Status Management: Enable/disable users, reset passwords, and manage lockouts.

Audit Logging: Tracks key user actions for transparency and debugging.

Email Notifications (optional integration): For account activation, password resets, etc.

This module ensures both functionality and security, forming a reliable foundation for the overall system.
