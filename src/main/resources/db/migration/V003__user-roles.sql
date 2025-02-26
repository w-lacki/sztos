CREATE TABLE roles
(
    id   INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

INSERT INTO roles VALUES (0, 'ROLE_USER'), (1, 'ROLE_ADMIN'); -- Default roles

CREATE TABLE user_roles
(
    user_id UUID   NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);
