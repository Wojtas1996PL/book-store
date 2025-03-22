INSERT INTO users (id, email, password, first_name, last_name, is_deleted) VALUES
    (1, 'user1@example.com', 'password1', 'John', 'Doe', FALSE),
    (2, 'user2@example.com', 'password2', 'Jane', 'Smith', FALSE),
    (3, 'user3@example.com', 'password3', 'Alice', 'Johnson', FALSE);

INSERT INTO shopping_carts (id, user_id, is_deleted) VALUES
    (1, 1, FALSE),
    (2, 2, FALSE),
    (3, 3, FALSE);
