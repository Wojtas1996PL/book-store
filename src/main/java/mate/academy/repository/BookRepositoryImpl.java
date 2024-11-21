package mate.academy.repository;

import java.util.List;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.model.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public BookRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Book save(Book book) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(book);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Can't save book: " + book);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return book;
    }

    @Override
    public List<Book> findAll() {
        Session session = sessionFactory.openSession();
        return session.createQuery("select b from Book b", Book.class).getResultList();
    }

    @Override
    public Book getBookById(long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Book.class, id);
        } catch (RuntimeException ex) {
            throw new EntityNotFoundException("Could not get a book with id: " + id);
        }
    }
}
