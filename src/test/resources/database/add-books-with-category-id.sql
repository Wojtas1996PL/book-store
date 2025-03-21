insert into books (id, title, author, isbn, price, is_deleted) values (4, 'Volkswagen', 'CC', '191', 15, false);
insert into books (id, title, author, isbn, price, is_deleted) values (5, 'Audi', 'R8', '219', 16, false);
insert into books (id, title, author, isbn, price, is_deleted) values (6, 'BMW', 'M3', '800', 17, false);
insert into categories (id, name, description, is_deleted) values (4, 'Cars', 'Car books', false);
insert into book_categories(book_id, category_id) values (4, 4);
insert into book_categories(book_id, category_id) values (5, 4);
insert into book_categories(book_id, category_id) values (6, 4);